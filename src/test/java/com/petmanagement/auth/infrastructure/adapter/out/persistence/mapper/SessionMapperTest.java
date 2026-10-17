package com.petmanagement.auth.infrastructure.adapter.out.persistence.mapper;

import com.petmanagement.auth.domain.model.enums.SessionRevocationReason;
import com.petmanagement.auth.support.SessionJpaEntityTestBuilder;
import com.petmanagement.auth.support.SessionTestBuilder;
import com.petmanagement.auth.support.TestSessionMother;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class SessionMapperTest {

    private final SessionMapper mapper = new SessionMapper();

    @Nested
    class WhenMappingDomainToJpaEntity {

        @Test
        void shouldMapDomainToJpaEntity() {
            // Given
            var session = SessionTestBuilder.aSession().build();

            // When
            var result = mapper.toJpaEntity(session);

            // Then
            assertNotNull(result);
            assertEquals(session.getId().value(), result.getId());
            assertEquals(session.getOwnerId().value(), result.getOwnerId());
            assertEquals(session.getHashedRefreshToken().value(), result.getHashedRefreshToken());
            assertEquals(session.getExpiresAt().value(), result.getExpiresAt());
            assertNull(result.getRevokedAt());
            assertNull(result.getRevocationReason());
            assertEquals(session.getCreatedAt(), result.getCreatedAt());
        }

        @Test
        void shouldMapRevocationWhenSessionIsRevoked() {
            // Given
            var revokedAt = Instant.parse("2024-01-15T10:00:00Z");
            var session = SessionTestBuilder.aSession()
                    .withRevokedAt(revokedAt)
                    .withRevocationReason(SessionRevocationReason.LOGOUT)
                    .build();

            // When
            var result = mapper.toJpaEntity(session);

            // Then
            assertEquals(revokedAt, result.getRevokedAt());
            assertEquals("LOGOUT", result.getRevocationReason());
        }

    }

    @Nested
    class WhenMappingJpaEntityToDomain {

        @Test
        void shouldMapJpaEntityToDomain() {
            // Given
            var entity = SessionJpaEntityTestBuilder.aSessionJpaEntity().build();

            // When
            var result = mapper.toDomain(entity);

            // Then
            assertNotNull(result);
            assertEquals(TestSessionMother.DEFAULT_SESSION_ID, result.getId());
            assertEquals(TestSessionMother.EXISTING_OWNER_ID, result.getOwnerId());
            assertEquals(TestSessionMother.DEFAULT_HASHED_REFRESH_TOKEN, result.getHashedRefreshToken());
            assertEquals(TestSessionMother.DEFAULT_EXPIRES_AT, result.getExpiresAt());
            assertNull(result.getRevokedAt());
            assertNull(result.getRevocationReason());
            assertEquals(entity.getCreatedAt(), result.getCreatedAt());
        }

        @Test
        void shouldMapRevocationWhenEntityIsRevoked() {
            // Given
            var revokedAt = Instant.parse("2024-01-15T10:00:00Z");
            var entity = SessionJpaEntityTestBuilder.aSessionJpaEntity()
                    .withRevokedAt(revokedAt)
                    .withRevocationReason("ROTATED")
                    .build();

            // When
            var result = mapper.toDomain(entity);

            // Then
            assertEquals(revokedAt, result.getRevokedAt());
            assertEquals(SessionRevocationReason.ROTATED, result.getRevocationReason());
        }

    }

}
