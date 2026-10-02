package com.petmanagement.auth.infrastructure.adapter.out.persistence.repository;

import com.petmanagement.RepositoryTest;
import com.petmanagement.auth.support.SessionJpaEntityTestBuilder;
import com.petmanagement.auth.support.TestSessionMother;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@RepositoryTest
class SessionJpaRepositoryIntegrationTest {

    @Autowired
    private SessionJpaRepository repository;

    @Nested
    class WhenFindingByHashedRefreshToken {

        @Test
        void shouldReturnSessionWhenHashedRefreshTokenExists() {
            // Given
            var entity = SessionJpaEntityTestBuilder.aSessionJpaEntity().build();
            repository.save(entity);

            // When
            var result = repository.findByHashedRefreshToken(entity.getHashedRefreshToken());

            // Then
            assertTrue(result.isPresent());
            assertEquals(entity.getId(), result.get().getId());
        }

        @Test
        void shouldReturnEmptyWhenHashedRefreshTokenDoesNotExist() {
            // Given
            var hashedRefreshToken = TestSessionMother.ANOTHER_HASHED_REFRESH_TOKEN.value();

            // When
            var result = repository.findByHashedRefreshToken(hashedRefreshToken);

            // Then
            assertTrue(result.isEmpty());
        }

        @Test
        void shouldReturnRevokedSession() {
            // Given
            var entity = SessionJpaEntityTestBuilder.aSessionJpaEntity()
                    .withRevokedAt(Instant.now())
                    .withRevocationReason("ROTATED")
                    .build();
            repository.save(entity);

            // When
            var result = repository.findByHashedRefreshToken(entity.getHashedRefreshToken());

            // Then
            assertTrue(result.isPresent());
            assertNotNull(result.get().getRevokedAt());
        }

        @Test
        void shouldReturnExpiredSession() {
            // Given
            var entity = SessionJpaEntityTestBuilder.aSessionJpaEntity()
                    .withExpiresAt(TestSessionMother.EXPIRED_EXPIRES_AT.value())
                    .build();
            repository.save(entity);

            // When
            var result = repository.findByHashedRefreshToken(entity.getHashedRefreshToken());

            // Then
            assertTrue(result.isPresent());
        }

    }

}
