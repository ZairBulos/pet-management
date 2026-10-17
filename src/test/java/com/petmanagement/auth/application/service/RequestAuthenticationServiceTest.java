package com.petmanagement.auth.application.service;

import com.petmanagement.auth.domain.event.AuthenticationCreated;
import com.petmanagement.auth.domain.exception.OwnerNotFoundException;
import com.petmanagement.auth.support.AuthenticationTestBuilder;
import com.petmanagement.auth.support.InMemoryAuthenticationRepository;
import com.petmanagement.auth.support.TestAuthenticationMother;
import com.petmanagement.owners.api.OwnerApi;
import com.petmanagement.shared.application.port.out.EventPublisherPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RequestAuthenticationServiceTest {

    private InMemoryAuthenticationRepository repository;
    private EventPublisherPort publisher;
    private UnaryOperator<String> hasher;
    private OwnerApi ownerApi;
    private RequestAuthenticationService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryAuthenticationRepository();
        publisher = mock(EventPublisherPort.class);
        hasher = TestAuthenticationMother.DEFAULT_HASHER;
        ownerApi = mock(OwnerApi.class);

        service = new RequestAuthenticationService(repository, publisher, hasher, ownerApi);
    }

    @Nested
    class WhenRequestingAuthentication {

        @Test
        void shouldCreateAuthentication() {
            // Given
            var command = AuthenticationTestBuilder.RequestAuthenticationCommandBuilder
                    .aRequestAuthenticationCommand()
                    .build();

            when(ownerApi.existsByEmail(command.email().value()))
                    .thenReturn(true);

            // When
            service.execute(command);

            // Then
            assertEquals(1, repository.size());
        }

        @Test
        void shouldPublishAuthenticationCreatedEvent() {
            // Given
            var command = AuthenticationTestBuilder.RequestAuthenticationCommandBuilder
                    .aRequestAuthenticationCommand()
                    .build();

            when(ownerApi.existsByEmail(command.email().value()))
                    .thenReturn(true);

            // When
            service.execute(command);

            // Then
            var eventCaptor = ArgumentCaptor.forClass(List.class);
            verify(publisher).publishAll(eventCaptor.capture());

            var events = eventCaptor.getValue();
            assertEquals(1, events.size());
            assertInstanceOf(AuthenticationCreated.class, events.getFirst());
        }

    }

    @Nested
    class WhenOwnerHasAnActiveAuthentication {

        @Test
        void shouldDisableActiveAuthenticationAndCreateNewOne() {
            // Given
            var oldAuthentication = AuthenticationTestBuilder.aAuthentication()
                    .withEmail(TestAuthenticationMother.EMAIL_ROBERT)
                    .build();
            repository.save(oldAuthentication);

            var command = AuthenticationTestBuilder.RequestAuthenticationCommandBuilder
                    .aRequestAuthenticationCommand()
                    .withEmail(oldAuthentication.getEmail())
                    .build();

            when(ownerApi.existsByEmail(command.email().value()))
                    .thenReturn(true);

            // When
            service.execute(command);

            // Then
            assertEquals(2, repository.size());

            var authentications = repository.findAllByEmail(oldAuthentication.getEmail());
            assertEquals(2, authentications.size());

            var oldAuth = authentications.getFirst();
            var newAuth = authentications.getLast();

            assertNotNull(oldAuth.getAuthenticatedAt());
            assertNull(newAuth.getAuthenticatedAt());
        }

    }

    @Nested
    class WhenOwnerDoesNotExist {

        @Test
        void shouldThrowWhenOwnerDoesNotExist() {
            // Given
            var command = AuthenticationTestBuilder.RequestAuthenticationCommandBuilder
                    .aRequestAuthenticationCommand()
                    .withEmail(TestAuthenticationMother.EMAIL_MARIA)
                    .build();

            when(ownerApi.existsByEmail(command.email().value()))
                    .thenReturn(false);

            // When/Then
            assertThrows(
                    OwnerNotFoundException.class,
                    () -> service.execute(command)
            );
        }

        @Test
        void shouldNotPublishEventWhenOwnerDoesNotExist() {
            // Given
            var command = AuthenticationTestBuilder.RequestAuthenticationCommandBuilder
                    .aRequestAuthenticationCommand()
                    .withEmail(TestAuthenticationMother.EMAIL_MARIA)
                    .build();

            when(ownerApi.existsByEmail(command.email().value()))
                    .thenReturn(false);

            // When/Then
            assertThrows(
                    OwnerNotFoundException.class,
                    () -> service.execute(command)
            );

            verify(publisher, never()).publish(any());
        }

    }

}
