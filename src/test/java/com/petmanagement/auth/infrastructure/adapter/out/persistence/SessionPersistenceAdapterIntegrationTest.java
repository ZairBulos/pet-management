package com.petmanagement.auth.infrastructure.adapter.out.persistence;

import com.petmanagement.RepositoryTest;
import com.petmanagement.auth.domain.model.enums.SessionRevocationReason;
import com.petmanagement.auth.infrastructure.adapter.out.persistence.mapper.SessionMapper;
import com.petmanagement.auth.support.SessionTestBuilder;
import com.petmanagement.auth.support.TestSessionMother;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@RepositoryTest
@Import({
        SessionPersistenceAdapter.class,
        SessionMapper.class,
})
class SessionPersistenceAdapterIntegrationTest {

    @Autowired
    private SessionPersistenceAdapter adapter;

    @Nested
    class WhenFindingSessionByHashedRefreshToken {

        @Test
        void shouldReturnSessionWhenExists() {
            // Given
            var session = SessionTestBuilder.aSession().build();
            adapter.save(session);

            // When
            var result = adapter.findByHashedRefreshToken(session.getHashedRefreshToken());

            // Then
            assertTrue(result.isPresent());
            assertEquals(session.getId(), result.get().getId());
            assertEquals(session.getOwnerId(), result.get().getOwnerId());
        }

        @Test
        void shouldReturnEmptyWhenHashedRefreshTokenDoesNotExist() {
            // Given
            var hashedRefreshToken = TestSessionMother.ANOTHER_HASHED_REFRESH_TOKEN;

            // When
            var result = adapter.findByHashedRefreshToken(hashedRefreshToken);

            // Then
            assertTrue(result.isEmpty());
        }

        @Test
        void shouldReturnRevokedSession() {
            // Given
            var session = SessionTestBuilder.aSession()
                    .withRevokedAt(Instant.now())
                    .withRevocationReason(SessionRevocationReason.ROTATED)
                    .build();
            adapter.save(session);

            // When
            var result = adapter.findByHashedRefreshToken(session.getHashedRefreshToken());

            // Then
            assertTrue(result.isPresent());
            assertTrue(result.get().isRevoked());
            assertEquals(SessionRevocationReason.ROTATED, result.get().getRevocationReason());
        }

        @Test
        void shouldReturnExpiredSession() {
            // Given
            var session = SessionTestBuilder.aSession()
                    .withExpiresAt(TestSessionMother.EXPIRED_EXPIRES_AT)
                    .build();
            adapter.save(session);

            // When
            var result = adapter.findByHashedRefreshToken(session.getHashedRefreshToken());

            // Then
            assertTrue(result.isPresent());
            assertTrue(result.get().isExpired(Instant.now()));
        }

    }

    @Nested
    class WhenSavingSession {

        @Test
        void shouldPersistSessionToDatabase() {
            // Given
            var session = SessionTestBuilder.aSession().build();

            // When
            adapter.save(session);

            // Then
            var result = adapter.findByHashedRefreshToken(session.getHashedRefreshToken());
            assertTrue(result.isPresent());
            assertEquals(session.getOwnerId(), result.get().getOwnerId());
            assertFalse(result.get().isRevoked());
            assertNull(result.get().getRevocationReason());
        }

        @Test
        void shouldUpdateSessionWhenSavingExisting() {
            // Given
            var session = SessionTestBuilder.aSession().build();
            adapter.save(session);

            session.revoke(TestSessionMother.LOGOUT_REASON);

            // When
            adapter.save(session);

            // Then
            var result = adapter.findByHashedRefreshToken(session.getHashedRefreshToken());
            assertTrue(result.isPresent());
            assertTrue(result.get().isRevoked());
            assertEquals(SessionRevocationReason.LOGOUT, result.get().getRevocationReason());
        }

    }

}
