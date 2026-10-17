package com.petmanagement.auth.application.service;

import com.petmanagement.auth.application.port.out.TokenProviderPort;
import com.petmanagement.auth.domain.event.SessionCreated;
import com.petmanagement.auth.domain.exception.ActiveAuthenticationNotFoundException;
import com.petmanagement.auth.domain.exception.InvalidAuthenticationCodeException;
import com.petmanagement.auth.domain.exception.OwnerNotFoundException;
import com.petmanagement.auth.support.*;
import com.petmanagement.owners.api.OwnerApi;
import com.petmanagement.shared.application.port.out.EventPublisherPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VerifyAuthenticationServiceTest {

    private InMemoryAuthenticationRepository authenticationRepository;
    private InMemorySessionRepository sessionRepository;
    private TokenProviderPort tokenProvider;
    private BiPredicate<String, String> verifier;
    private EventPublisherPort publisher;
    private OwnerApi ownerApi;
    private VerifyAuthenticationService service;

    @BeforeEach
    void setUp() {
        authenticationRepository = new InMemoryAuthenticationRepository();
        sessionRepository = new InMemorySessionRepository();
        tokenProvider = new StubTokenProvider();
        verifier = TestAuthenticationMother.DEFAULT_VERIFIER;
        publisher = mock(EventPublisherPort.class);
        ownerApi = mock(OwnerApi.class);

        service = new VerifyAuthenticationService(
                authenticationRepository, sessionRepository, tokenProvider, verifier, publisher, ownerApi
        );
    }

    @Nested
    class WhenVerifyingAuthentication {

        @Test
        void shouldReturnAccessAndRefreshTokens() {
            // Given
            var authentication = AuthenticationTestBuilder.aAuthentication().build();
            authenticationRepository.save(authentication);

            var command = AuthenticationTestBuilder.VerifyAuthenticationCommandBuilder
                    .aVerifyAuthenticationCommand()
                    .build();

            when(ownerApi.getOwnerIdByEmail(command.email().value()))
                    .thenReturn(Optional.of(TestSessionMother.EXISTING_OWNER_ID.value()));

            // When
            var result = service.execute(command);

            // Then
            assertEquals(StubTokenProvider.DEFAULT_ACCESS_TOKEN, result.accessToken());
            assertEquals(TestSessionMother.DEFAULT_PLAIN_REFRESH_TOKEN.value(), result.refreshToken());
        }

        @Test
        void shouldMarkAuthenticationAsAuthenticated() {
            // Given
            var authentication = AuthenticationTestBuilder.aAuthentication().build();
            authenticationRepository.save(authentication);

            var command = AuthenticationTestBuilder.VerifyAuthenticationCommandBuilder
                    .aVerifyAuthenticationCommand()
                    .build();

            when(ownerApi.getOwnerIdByEmail(command.email().value()))
                    .thenReturn(Optional.of(TestSessionMother.EXISTING_OWNER_ID.value()));

            // When
            service.execute(command);

            // Then
            var authentications = authenticationRepository.findAllByEmail(command.email());
            assertEquals(1, authentications.size());
            assertTrue(authentications.getFirst().isAuthenticated());
        }

        @Test
        void shouldCreateSessionWithHashedRefreshToken() {
            // Given
            var authentication = AuthenticationTestBuilder.aAuthentication().build();
            authenticationRepository.save(authentication);

            var command = AuthenticationTestBuilder.VerifyAuthenticationCommandBuilder
                    .aVerifyAuthenticationCommand()
                    .build();

            when(ownerApi.getOwnerIdByEmail(command.email().value()))
                    .thenReturn(Optional.of(TestSessionMother.EXISTING_OWNER_ID.value()));

            // When
            service.execute(command);

            // Then
            assertEquals(1, sessionRepository.size());

            var sessions = sessionRepository.findAllByOwnerId(TestSessionMother.EXISTING_OWNER_ID);
            assertEquals(1, sessions.size());

            var session = sessions.getFirst();
            assertEquals(TestSessionMother.DEFAULT_HASHED_REFRESH_TOKEN, session.getHashedRefreshToken());
            assertTrue(session.isActive(Instant.now()));
        }

        @Test
        void shouldPublishSessionCreatedEvent() {
            // Given
            var authentication = AuthenticationTestBuilder.aAuthentication().build();
            authenticationRepository.save(authentication);

            var command = AuthenticationTestBuilder.VerifyAuthenticationCommandBuilder
                    .aVerifyAuthenticationCommand()
                    .build();

            when(ownerApi.getOwnerIdByEmail(command.email().value()))
                    .thenReturn(Optional.of(TestSessionMother.EXISTING_OWNER_ID.value()));

            // When
            service.execute(command);

            // Then
            var eventCaptor = ArgumentCaptor.forClass(List.class);
            verify(publisher).publishAll(eventCaptor.capture());

            var events = eventCaptor.getValue();
            assertEquals(1, events.size());
            assertInstanceOf(SessionCreated.class, events.getFirst());
        }

    }

    @Nested
    class WhenOwnerDoesNotExist {

        @Test
        void shouldThrowWhenOwnerDoesNotExist() {
            // Given
            var command = AuthenticationTestBuilder.VerifyAuthenticationCommandBuilder
                    .aVerifyAuthenticationCommand()
                    .withEmail(TestAuthenticationMother.EMAIL_MARIA)
                    .build();

            when(ownerApi.getOwnerIdByEmail(command.email().value()))
                    .thenReturn(Optional.empty());

            // When/Then
            assertThrows(
                    OwnerNotFoundException.class,
                    () -> service.execute(command)
            );
        }

        @Test
        void shouldNotConsumeAuthenticationWhenOwnerDoesNotExist() {
            // Given
            var authentication = AuthenticationTestBuilder.aAuthentication().build();
            authenticationRepository.save(authentication);

            var command = AuthenticationTestBuilder.VerifyAuthenticationCommandBuilder
                    .aVerifyAuthenticationCommand()
                    .build();

            when(ownerApi.getOwnerIdByEmail(command.email().value()))
                    .thenReturn(Optional.empty());

            // When/Then
            assertThrows(OwnerNotFoundException.class, () -> service.execute(command));
            assertFalse(authentication.isAuthenticated());
        }

        @Test
        void shouldNotCreateSessionWhenOwnerDoesNotExist() {
            // Given
            authenticationRepository.save(AuthenticationTestBuilder.aAuthentication().build());

            var command = AuthenticationTestBuilder.VerifyAuthenticationCommandBuilder
                    .aVerifyAuthenticationCommand()
                    .build();

            when(ownerApi.getOwnerIdByEmail(command.email().value()))
                    .thenReturn(Optional.empty());

            // When/Then
            assertThrows(OwnerNotFoundException.class, () -> service.execute(command));
            assertEquals(0, sessionRepository.size());
        }

        @Test
        void shouldNotPublishEventWhenOwnerDoesNotExist() {
            // Given
            authenticationRepository.save(AuthenticationTestBuilder.aAuthentication().build());

            var command = AuthenticationTestBuilder.VerifyAuthenticationCommandBuilder
                    .aVerifyAuthenticationCommand()
                    .build();

            when(ownerApi.getOwnerIdByEmail(command.email().value()))
                    .thenReturn(Optional.empty());

            // When/Then
            assertThrows(OwnerNotFoundException.class, () -> service.execute(command));
            verify(publisher, never()).publishAll(any());
        }

    }

    @Nested
    class WhenThereIsNoActiveAuthentication {

        @Test
        void shouldThrowWhenAuthenticationDoesNotExist() {
            // Given
            var command = AuthenticationTestBuilder.VerifyAuthenticationCommandBuilder
                    .aVerifyAuthenticationCommand()
                    .withEmail(TestAuthenticationMother.EMAIL_ROBERT)
                    .build();

            when(ownerApi.getOwnerIdByEmail(command.email().value()))
                    .thenReturn(Optional.of(TestSessionMother.EXISTING_OWNER_ID.value()));

            // When/Then
            assertThrows(
                    ActiveAuthenticationNotFoundException.class,
                    () -> service.execute(command)
            );
        }

        @Test
        void shouldThrowWhenAuthenticationIsExpired() {
            // Given
            var authentication = AuthenticationTestBuilder.aAuthentication()
                    .withExpiresAt(TestAuthenticationMother.EXPIRED_EXPIRES_AT)
                    .build();
            authenticationRepository.save(authentication);

            var command = AuthenticationTestBuilder.VerifyAuthenticationCommandBuilder
                    .aVerifyAuthenticationCommand()
                    .build();

            when(ownerApi.getOwnerIdByEmail(command.email().value()))
                    .thenReturn(Optional.of(TestSessionMother.EXISTING_OWNER_ID.value()));

            // When/Then
            assertThrows(
                    ActiveAuthenticationNotFoundException.class,
                    () -> service.execute(command)
            );
        }

        @Test
        void shouldThrowWhenAuthenticationWasAlreadyUsed() {
            // Given
            var authentication = AuthenticationTestBuilder.aAuthentication()
                    .withAuthenticatedAt(Instant.now())
                    .build();
            authenticationRepository.save(authentication);

            var command = AuthenticationTestBuilder.VerifyAuthenticationCommandBuilder
                    .aVerifyAuthenticationCommand()
                    .build();

            when(ownerApi.getOwnerIdByEmail(command.email().value()))
                    .thenReturn(Optional.of(TestSessionMother.EXISTING_OWNER_ID.value()));

            // When/Then
            assertThrows(
                    ActiveAuthenticationNotFoundException.class,
                    () -> service.execute(command)
            );
        }

        @Test
        void shouldNotCreateSessionWhenThereIsNoActiveAuthentication() {
            // Given
            var command = AuthenticationTestBuilder.VerifyAuthenticationCommandBuilder
                    .aVerifyAuthenticationCommand()
                    .withEmail(TestAuthenticationMother.EMAIL_ROBERT)
                    .build();

            when(ownerApi.getOwnerIdByEmail(command.email().value()))
                    .thenReturn(Optional.of(TestSessionMother.EXISTING_OWNER_ID.value()));

            // When/Then
            assertThrows(ActiveAuthenticationNotFoundException.class, () -> service.execute(command));
            assertEquals(0, sessionRepository.size());
        }

        @Test
        void shouldNotPublishEventWhenThereIsNoActiveAuthentication() {
            // Given
            var command = AuthenticationTestBuilder.VerifyAuthenticationCommandBuilder
                    .aVerifyAuthenticationCommand()
                    .withEmail(TestAuthenticationMother.EMAIL_ROBERT)
                    .build();

            when(ownerApi.getOwnerIdByEmail(command.email().value()))
                    .thenReturn(Optional.of(TestSessionMother.EXISTING_OWNER_ID.value()));

            // When/Then
            assertThrows(ActiveAuthenticationNotFoundException.class, () -> service.execute(command));
            verify(publisher, never()).publishAll(any());
        }

    }

    @Nested
    class WhenCodeIsInvalid {

        @Test
        void shouldThrowWhenCodeIsInvalid() {
            // Given
            var authentication = AuthenticationTestBuilder.aAuthentication().build();
            authenticationRepository.save(authentication);

            var command = AuthenticationTestBuilder.VerifyAuthenticationCommandBuilder
                    .aVerifyAuthenticationCommand()
                    .withCode(TestAuthenticationMother.INVALID_PLAIN_CODE)
                    .build();

            when(ownerApi.getOwnerIdByEmail(command.email().value()))
                    .thenReturn(Optional.of(TestSessionMother.EXISTING_OWNER_ID.value()));

            // When/Then
            assertThrows(
                    InvalidAuthenticationCodeException.class,
                    () -> service.execute(command)
            );
        }

        @Test
        void shouldNotConsumeAuthenticationWhenCodeIsInvalid() {
            // Given
            var authentication = AuthenticationTestBuilder.aAuthentication().build();
            authenticationRepository.save(authentication);

            var command = AuthenticationTestBuilder.VerifyAuthenticationCommandBuilder
                    .aVerifyAuthenticationCommand()
                    .withCode(TestAuthenticationMother.INVALID_PLAIN_CODE)
                    .build();

            when(ownerApi.getOwnerIdByEmail(command.email().value()))
                    .thenReturn(Optional.of(TestSessionMother.EXISTING_OWNER_ID.value()));

            // When/Then
            assertThrows(InvalidAuthenticationCodeException.class, () -> service.execute(command));
            assertFalse(authentication.isAuthenticated());
        }

        @Test
        void shouldNotCreateSessionWhenCodeIsInvalid() {
            // Given
            authenticationRepository.save(AuthenticationTestBuilder.aAuthentication().build());

            var command = AuthenticationTestBuilder.VerifyAuthenticationCommandBuilder
                    .aVerifyAuthenticationCommand()
                    .withCode(TestAuthenticationMother.INVALID_PLAIN_CODE)
                    .build();

            when(ownerApi.getOwnerIdByEmail(command.email().value()))
                    .thenReturn(Optional.of(TestSessionMother.EXISTING_OWNER_ID.value()));

            // When/Then
            assertThrows(InvalidAuthenticationCodeException.class, () -> service.execute(command));
            assertEquals(0, sessionRepository.size());
        }

        @Test
        void shouldNotPublishEventWhenCodeIsInvalid() {
            // Given
            authenticationRepository.save(AuthenticationTestBuilder.aAuthentication().build());

            var command = AuthenticationTestBuilder.VerifyAuthenticationCommandBuilder
                    .aVerifyAuthenticationCommand()
                    .withCode(TestAuthenticationMother.INVALID_PLAIN_CODE)
                    .build();

            when(ownerApi.getOwnerIdByEmail(command.email().value()))
                    .thenReturn(Optional.of(TestSessionMother.EXISTING_OWNER_ID.value()));

            // When/Then
            assertThrows(InvalidAuthenticationCodeException.class, () -> service.execute(command));
            verify(publisher, never()).publishAll(any());
        }

    }

}
