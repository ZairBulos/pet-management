package com.petmanagement.auth.application.service;

import com.petmanagement.auth.application.port.out.TokenProviderPort;
import com.petmanagement.auth.domain.event.SessionCreated;
import com.petmanagement.auth.domain.event.SessionRevoked;
import com.petmanagement.auth.domain.exception.SessionExpiredException;
import com.petmanagement.auth.domain.exception.SessionNotFoundException;
import com.petmanagement.auth.domain.exception.SessionReuseDetectedException;
import com.petmanagement.auth.domain.exception.SessionRevokedException;
import com.petmanagement.auth.domain.model.enums.SessionRevocationReason;
import com.petmanagement.auth.support.InMemorySessionRepository;
import com.petmanagement.auth.support.SessionTestBuilder;
import com.petmanagement.auth.support.StubTokenProvider;
import com.petmanagement.auth.support.TestSessionMother;
import com.petmanagement.shared.application.port.out.EventPublisherPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RefreshSessionServiceTest {

    private InMemorySessionRepository repository;
    private TokenProviderPort tokenProvider;
    private EventPublisherPort publisher;
    private RefreshSessionService service;

    @BeforeEach
    void setUp() {
        repository = new InMemorySessionRepository();
        tokenProvider = new StubTokenProvider();
        publisher = mock(EventPublisherPort.class);

        service = new RefreshSessionService(repository, tokenProvider, publisher);
    }

    @Nested
    class WhenRefreshingSession {

        @Test
        void shouldReturnNewAccessAndRefreshTokens() {
            // Given
            var session = SessionTestBuilder.aSession()
                    .withHashedRefreshToken(TestSessionMother.ANOTHER_HASHED_REFRESH_TOKEN)
                    .build();
            repository.save(session);

            var command = SessionTestBuilder.RefreshSessionCommandBuilder
                    .aRefreshSessionCommand()
                    .build();

            // When
            var result = service.execute(command);

            // Then
            assertEquals(StubTokenProvider.DEFAULT_ACCESS_TOKEN, result.accessToken());
            assertEquals(StubTokenProvider.DEFAULT_REFRESH_TOKEN, result.refreshToken());
        }

        @Test
        void shouldRevokeCurrentSessionWithRotatedReason() {
            // Given
            var session = SessionTestBuilder.aSession()
                    .withHashedRefreshToken(TestSessionMother.ANOTHER_HASHED_REFRESH_TOKEN)
                    .build();
            repository.save(session);

            var command = SessionTestBuilder.RefreshSessionCommandBuilder
                    .aRefreshSessionCommand()
                    .build();

            // When
            service.execute(command);

            // Then
            var result = repository.findByHashedRefreshToken(session.getHashedRefreshToken());
            assertTrue(result.isPresent());
            assertTrue(result.get().isRevoked());
            assertEquals(SessionRevocationReason.ROTATED, result.get().getRevocationReason());
        }

        @Test
        void shouldCreateNewActiveSessionWithNewHashedRefreshToken() {
            // Given
            var session = SessionTestBuilder.aSession()
                    .withHashedRefreshToken(TestSessionMother.ANOTHER_HASHED_REFRESH_TOKEN)
                    .build();
            repository.save(session);

            var command = SessionTestBuilder.RefreshSessionCommandBuilder
                    .aRefreshSessionCommand()
                    .build();

            // When
            service.execute(command);

            // Then
            assertEquals(2, repository.size());

            var result = repository.findByHashedRefreshToken(TestSessionMother.DEFAULT_HASHED_REFRESH_TOKEN);
            assertTrue(result.isPresent());
            assertTrue(result.get().isActive(Instant.now()));
        }

        @Test
        void shouldPublishSessionRevokedAndSessionCreatedEvents() {
            // Given
            var session = SessionTestBuilder.aSession()
                    .withHashedRefreshToken(TestSessionMother.ANOTHER_HASHED_REFRESH_TOKEN)
                    .build();
            repository.save(session);

            var command = SessionTestBuilder.RefreshSessionCommandBuilder
                    .aRefreshSessionCommand()
                    .build();

            // When
            service.execute(command);

            // Then
            var eventCaptor = ArgumentCaptor.forClass(List.class);
            verify(publisher, times(2)).publishAll(eventCaptor.capture());

            var published = eventCaptor.getAllValues();
            assertInstanceOf(SessionRevoked.class, published.getFirst().getFirst());
            assertInstanceOf(SessionCreated.class, published.getLast().getFirst());
        }

    }

    @Nested
    class WhenSessionDoesNotExist {

        @Test
        void shouldThrowWhenSessionDoesNotExist() {
            // Given
            var command = SessionTestBuilder.RefreshSessionCommandBuilder
                    .aRefreshSessionCommand()
                    .withRefreshToken(TestSessionMother.INVALID_PLAIN_REFRESH_TOKEN)
                    .build();

            // When/Then
            assertThrows(
                    SessionNotFoundException.class,
                    () -> service.execute(command)
            );
        }

        @Test
        void shouldNotCreateSessionWhenSessionDoesNotExist() {
            // Given
            var command = SessionTestBuilder.RefreshSessionCommandBuilder
                    .aRefreshSessionCommand()
                    .withRefreshToken(TestSessionMother.INVALID_PLAIN_REFRESH_TOKEN)
                    .build();

            // When/Then
            assertThrows(SessionNotFoundException.class, () -> service.execute(command));
            assertEquals(0, repository.size());
        }

        @Test
        void shouldNotPublishEventWhenSessionDoesNotExist() {
            // Given
            var command = SessionTestBuilder.RefreshSessionCommandBuilder
                    .aRefreshSessionCommand()
                    .withRefreshToken(TestSessionMother.INVALID_PLAIN_REFRESH_TOKEN)
                    .build();

            // When/Then
            assertThrows(SessionNotFoundException.class, () -> service.execute(command));
            verify(publisher, never()).publishAll(any());
        }

    }

    @Nested
    class WhenSessionIsRevoked {

        @Test
        void shouldThrowWhenSessionWasRevokedByLogout() {
            // Given
            var session = SessionTestBuilder.aSession()
                    .withHashedRefreshToken(TestSessionMother.ANOTHER_HASHED_REFRESH_TOKEN)
                    .withRevokedAt(Instant.now())
                    .withRevocationReason(SessionRevocationReason.LOGOUT)
                    .build();
            repository.save(session);

            var command = SessionTestBuilder.RefreshSessionCommandBuilder
                    .aRefreshSessionCommand()
                    .build();

            // When/Then
            assertThrows(
                    SessionRevokedException.class,
                    () -> service.execute(command)
            );
        }

        @Test
        void shouldThrowWhenSessionWasRevokedByReuseDetection() {
            // Given
            var session = SessionTestBuilder.aSession()
                    .withHashedRefreshToken(TestSessionMother.ANOTHER_HASHED_REFRESH_TOKEN)
                    .withRevokedAt(Instant.now())
                    .withRevocationReason(SessionRevocationReason.REUSE_DETECTED)
                    .build();
            repository.save(session);

            var command = SessionTestBuilder.RefreshSessionCommandBuilder
                    .aRefreshSessionCommand()
                    .build();

            // When/Then
            assertThrows(
                    SessionRevokedException.class,
                    () -> service.execute(command)
            );
        }

        @Test
        void shouldNotCreateNewSessionWhenSessionWasRevokedByLogout() {
            // Given
            var session = SessionTestBuilder.aSession()
                    .withHashedRefreshToken(TestSessionMother.ANOTHER_HASHED_REFRESH_TOKEN)
                    .withRevokedAt(Instant.now())
                    .withRevocationReason(SessionRevocationReason.LOGOUT)
                    .build();
            repository.save(session);

            var command = SessionTestBuilder.RefreshSessionCommandBuilder
                    .aRefreshSessionCommand()
                    .build();

            // When/Then
            assertThrows(SessionRevokedException.class, () -> service.execute(command));
            assertEquals(1, repository.size());
        }

        @Test
        void shouldNotRevokeOtherActiveSessionsWhenSessionWasRevokedByLogout() {
            // Given
            var revokedSession = SessionTestBuilder.aSession()
                    .withHashedRefreshToken(TestSessionMother.ANOTHER_HASHED_REFRESH_TOKEN)
                    .withRevokedAt(Instant.now())
                    .withRevocationReason(SessionRevocationReason.LOGOUT)
                    .build();
            repository.save(revokedSession);

            var activeSession = SessionTestBuilder.aSession()
                    .withId(TestSessionMother.ANOTHER_SESSION_ID)
                    .withHashedRefreshToken(TestSessionMother.DEFAULT_HASHED_REFRESH_TOKEN)
                    .build();
            repository.save(activeSession);

            var command = SessionTestBuilder.RefreshSessionCommandBuilder
                    .aRefreshSessionCommand()
                    .build();

            // When
            assertThrows(SessionRevokedException.class, () -> service.execute(command));

            // Then
            var result = repository.findByHashedRefreshToken(activeSession.getHashedRefreshToken());
            assertTrue(result.isPresent());
            assertFalse(result.get().isRevoked());
        }

        @Test
        void shouldNotPublishEventWhenSessionWasRevokedByLogout() {
            // Given
            var session = SessionTestBuilder.aSession()
                    .withHashedRefreshToken(TestSessionMother.ANOTHER_HASHED_REFRESH_TOKEN)
                    .withRevokedAt(Instant.now())
                    .withRevocationReason(SessionRevocationReason.LOGOUT)
                    .build();
            repository.save(session);

            var command = SessionTestBuilder.RefreshSessionCommandBuilder
                    .aRefreshSessionCommand()
                    .build();

            // When/Then
            assertThrows(SessionRevokedException.class, () -> service.execute(command));
            verify(publisher, never()).publishAll(any());
        }

    }

    @Nested
    class WhenSessionIsExpired {

        @Test
        void shouldThrowWhenSessionIsExpired() {
            // Given
            var session = SessionTestBuilder.aSession()
                    .withHashedRefreshToken(TestSessionMother.ANOTHER_HASHED_REFRESH_TOKEN)
                    .withExpiresAt(TestSessionMother.EXPIRED_EXPIRES_AT)
                    .build();
            repository.save(session);

            var command = SessionTestBuilder.RefreshSessionCommandBuilder
                    .aRefreshSessionCommand()
                    .build();

            // When/Then
            assertThrows(
                    SessionExpiredException.class,
                    () -> service.execute(command)
            );
        }

        @Test
        void shouldNotCreateNewSessionWhenSessionIsExpired() {
            // Given
            var session = SessionTestBuilder.aSession()
                    .withHashedRefreshToken(TestSessionMother.ANOTHER_HASHED_REFRESH_TOKEN)
                    .withExpiresAt(TestSessionMother.EXPIRED_EXPIRES_AT)
                    .build();
            repository.save(session);

            var command = SessionTestBuilder.RefreshSessionCommandBuilder
                    .aRefreshSessionCommand()
                    .build();

            // When/Then
            assertThrows(SessionExpiredException.class, () -> service.execute(command));
            assertEquals(1, repository.size());
        }

        @Test
        void shouldNotRevokeExpiredSession() {
            // Given
            var session = SessionTestBuilder.aSession()
                    .withHashedRefreshToken(TestSessionMother.ANOTHER_HASHED_REFRESH_TOKEN)
                    .withExpiresAt(TestSessionMother.EXPIRED_EXPIRES_AT)
                    .build();
            repository.save(session);

            var command = SessionTestBuilder.RefreshSessionCommandBuilder
                    .aRefreshSessionCommand()
                    .build();

            // When
            assertThrows(SessionExpiredException.class, () -> service.execute(command));

            // Then
            var result = repository.findByHashedRefreshToken(session.getHashedRefreshToken());
            assertTrue(result.isPresent());
            assertFalse(result.get().isRevoked());
        }

        @Test
        void shouldNotPublishEventWhenSessionIsExpired() {
            // Given
            var session = SessionTestBuilder.aSession()
                    .withHashedRefreshToken(TestSessionMother.ANOTHER_HASHED_REFRESH_TOKEN)
                    .withExpiresAt(TestSessionMother.EXPIRED_EXPIRES_AT)
                    .build();
            repository.save(session);

            var command = SessionTestBuilder.RefreshSessionCommandBuilder
                    .aRefreshSessionCommand()
                    .build();

            // When/Then
            assertThrows(SessionExpiredException.class, () -> service.execute(command));
            verify(publisher, never()).publishAll(any());
        }

    }

    @Nested
    class WhenReuseIsDetected {

        @Test
        void shouldThrowWhenSessionWasAlreadyRotated() {
            // Given
            var rotatedSession = SessionTestBuilder.aSession()
                    .withHashedRefreshToken(TestSessionMother.ANOTHER_HASHED_REFRESH_TOKEN)
                    .withRevokedAt(Instant.now())
                    .withRevocationReason(SessionRevocationReason.ROTATED)
                    .build();
            repository.save(rotatedSession);

            var command = SessionTestBuilder.RefreshSessionCommandBuilder
                    .aRefreshSessionCommand()
                    .build();

            // When/Then
            assertThrows(
                    SessionReuseDetectedException.class,
                    () -> service.execute(command)
            );
        }

        @Test
        void shouldRevokeActiveSessionsOfOwnerWithReuseDetectedReason() {
            // Given
            var rotatedSession = SessionTestBuilder.aSession()
                    .withHashedRefreshToken(TestSessionMother.ANOTHER_HASHED_REFRESH_TOKEN)
                    .withRevokedAt(Instant.now())
                    .withRevocationReason(SessionRevocationReason.ROTATED)
                    .build();
            repository.save(rotatedSession);

            var activeSession = SessionTestBuilder.aSession()
                    .withId(TestSessionMother.ANOTHER_SESSION_ID)
                    .withHashedRefreshToken(TestSessionMother.DEFAULT_HASHED_REFRESH_TOKEN)
                    .build();
            repository.save(activeSession);

            var command = SessionTestBuilder.RefreshSessionCommandBuilder
                    .aRefreshSessionCommand()
                    .build();

            // When
            assertThrows(SessionReuseDetectedException.class, () -> service.execute(command));

            // Then
            var result = repository.findByHashedRefreshToken(activeSession.getHashedRefreshToken());
            assertTrue(result.isPresent());
            assertTrue(result.get().isRevoked());
            assertEquals(SessionRevocationReason.REUSE_DETECTED, result.get().getRevocationReason());
        }

        @Test
        void shouldKeepRotatedReasonOnPresentedSession() {
            // Given
            var rotatedSession = SessionTestBuilder.aSession()
                    .withHashedRefreshToken(TestSessionMother.ANOTHER_HASHED_REFRESH_TOKEN)
                    .withRevokedAt(Instant.now())
                    .withRevocationReason(SessionRevocationReason.ROTATED)
                    .build();
            repository.save(rotatedSession);

            var command = SessionTestBuilder.RefreshSessionCommandBuilder
                    .aRefreshSessionCommand()
                    .build();

            // When
            assertThrows(SessionReuseDetectedException.class, () -> service.execute(command));

            // Then
            var result = repository.findByHashedRefreshToken(rotatedSession.getHashedRefreshToken());
            assertTrue(result.isPresent());
            assertEquals(SessionRevocationReason.ROTATED, result.get().getRevocationReason());
        }

        @Test
        void shouldNotCreateNewSessionWhenReuseIsDetected() {
            // Given
            var rotatedSession = SessionTestBuilder.aSession()
                    .withHashedRefreshToken(TestSessionMother.ANOTHER_HASHED_REFRESH_TOKEN)
                    .withRevokedAt(Instant.now())
                    .withRevocationReason(SessionRevocationReason.ROTATED)
                    .build();
            repository.save(rotatedSession);

            var command = SessionTestBuilder.RefreshSessionCommandBuilder
                    .aRefreshSessionCommand()
                    .build();

            // When/Then
            assertThrows(SessionReuseDetectedException.class, () -> service.execute(command));
            assertEquals(1, repository.size());
        }

        @Test
        void shouldPublishSessionRevokedEventForEachRevokedActiveSession() {
            // Given
            var rotatedSession = SessionTestBuilder.aSession()
                    .withHashedRefreshToken(TestSessionMother.ANOTHER_HASHED_REFRESH_TOKEN)
                    .withRevokedAt(Instant.now())
                    .withRevocationReason(SessionRevocationReason.ROTATED)
                    .build();
            repository.save(rotatedSession);

            var firstActiveSession = SessionTestBuilder.aSession()
                    .withId(TestSessionMother.ANOTHER_SESSION_ID)
                    .withHashedRefreshToken(TestSessionMother.DEFAULT_HASHED_REFRESH_TOKEN)
                    .build();
            repository.save(firstActiveSession);

            var secondActiveSession = SessionTestBuilder.aSession()
                    .withId(TestSessionMother.THIRD_SESSION_ID)
                    .withHashedRefreshToken(TestSessionMother.THIRD_HASHED_REFRESH_TOKEN)
                    .build();
            repository.save(secondActiveSession);

            var command = SessionTestBuilder.RefreshSessionCommandBuilder
                    .aRefreshSessionCommand()
                    .build();

            // When
            assertThrows(SessionReuseDetectedException.class, () -> service.execute(command));

            // Then
            var eventCaptor = ArgumentCaptor.forClass(List.class);
            verify(publisher, times(2)).publishAll(eventCaptor.capture());
            eventCaptor.getAllValues()
                    .forEach(events -> assertInstanceOf(SessionRevoked.class, events.getFirst()));
        }

    }

}
