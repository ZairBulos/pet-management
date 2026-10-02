package com.petmanagement.auth.application.service;

import com.petmanagement.auth.domain.event.SessionRevoked;
import com.petmanagement.auth.domain.exception.SessionNotFoundException;
import com.petmanagement.auth.domain.model.enums.SessionRevocationReason;
import com.petmanagement.auth.support.InMemorySessionRepository;
import com.petmanagement.auth.support.SessionTestBuilder;
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

class RevokeSessionServiceTest {

    private InMemorySessionRepository repository;
    private EventPublisherPort publisher;
    private RevokeSessionService service;

    @BeforeEach
    void setUp() {
        repository = new InMemorySessionRepository();
        publisher = mock(EventPublisherPort.class);

        service = new RevokeSessionService(repository, publisher);
    }

    @Nested
    class WhenRevokingSession {

        @Test
        void shouldRevokeSessionWithLogoutReason() {
            // Given
            var session = SessionTestBuilder.aSession().build();
            repository.save(session);

            var command = SessionTestBuilder.RevokeSessionCommandBuilder
                    .aRevokeSessionCommand()
                    .build();

            // When
            service.execute(command);

            // Then
            var result = repository.findByHashedRefreshToken(session.getHashedRefreshToken());
            assertTrue(result.isPresent());
            assertTrue(result.get().isRevoked());
            assertEquals(SessionRevocationReason.LOGOUT, result.get().getRevocationReason());
        }

        @Test
        void shouldPublishSessionRevokedEvent() {
            // Given
            repository.save(SessionTestBuilder.aSession().build());

            var command = SessionTestBuilder.RevokeSessionCommandBuilder
                    .aRevokeSessionCommand()
                    .build();

            // When
            service.execute(command);

            // Then
            var eventCaptor = ArgumentCaptor.forClass(List.class);
            verify(publisher).publishAll(eventCaptor.capture());

            var events = eventCaptor.getValue();
            assertEquals(1, events.size());
            assertInstanceOf(SessionRevoked.class, events.getFirst());
        }

    }

    @Nested
    class WhenSessionDoesNotExist {

        @Test
        void shouldThrowWhenSessionDoesNotExist() {
            // Given
            var command = SessionTestBuilder.RevokeSessionCommandBuilder
                    .aRevokeSessionCommand()
                    .withRefreshToken(TestSessionMother.INVALID_PLAIN_REFRESH_TOKEN)
                    .build();

            // When/Then
            assertThrows(
                    SessionNotFoundException.class,
                    () -> service.execute(command)
            );
        }

        @Test
        void shouldNotPublishEventWhenSessionDoesNotExist() {
            // Given
            var command = SessionTestBuilder.RevokeSessionCommandBuilder
                    .aRevokeSessionCommand()
                    .withRefreshToken(TestSessionMother.INVALID_PLAIN_REFRESH_TOKEN)
                    .build();

            // When/Then
            assertThrows(
                    SessionNotFoundException.class,
                    () -> service.execute(command)
            );

            verify(publisher, never()).publishAll(any());
        }

    }

    @Nested
    class WhenSessionIsAlreadyRevoked {

        @Test
        void shouldKeepOriginalRevocationReason() {
            // Given
            var session = SessionTestBuilder.aSession()
                    .withRevokedAt(Instant.parse("2024-01-15T10:00:00Z"))
                    .withRevocationReason(SessionRevocationReason.ROTATED)
                    .build();
            repository.save(session);

            var command = SessionTestBuilder.RevokeSessionCommandBuilder
                    .aRevokeSessionCommand()
                    .build();

            // When
            service.execute(command);

            // Then
            var result = repository.findByHashedRefreshToken(session.getHashedRefreshToken());
            assertTrue(result.isPresent());
            assertEquals(Instant.parse("2024-01-15T10:00:00Z"), result.get().getRevokedAt());
            assertEquals(SessionRevocationReason.ROTATED, result.get().getRevocationReason());
        }

        @Test
        void shouldNotPublishSessionRevokedEvent() {
            // Given
            repository.save(
                    SessionTestBuilder.aSession()
                            .withRevokedAt(Instant.now())
                            .withRevocationReason(SessionRevocationReason.LOGOUT)
                            .build()
            );

            var command = SessionTestBuilder.RevokeSessionCommandBuilder
                    .aRevokeSessionCommand()
                    .build();

            // When
            service.execute(command);

            // Then
            var eventCaptor = ArgumentCaptor.forClass(List.class);
            verify(publisher).publishAll(eventCaptor.capture());
            assertTrue(eventCaptor.getValue().isEmpty());
        }

    }

    @Nested
    class WhenSessionIsExpired {

        @Test
        void shouldRevokeExpiredSession() {
            // Given
            var session = SessionTestBuilder.aSession()
                    .withExpiresAt(TestSessionMother.EXPIRED_EXPIRES_AT)
                    .build();
            repository.save(session);

            var command = SessionTestBuilder.RevokeSessionCommandBuilder
                    .aRevokeSessionCommand()
                    .build();

            // When
            service.execute(command);

            // Then
            var result = repository.findByHashedRefreshToken(session.getHashedRefreshToken());
            assertTrue(result.isPresent());
            assertTrue(result.get().isRevoked());
        }

    }

}
