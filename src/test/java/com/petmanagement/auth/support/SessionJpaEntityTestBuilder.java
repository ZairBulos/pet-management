package com.petmanagement.auth.support;

import com.petmanagement.auth.infrastructure.adapter.out.persistence.entity.SessionJpaEntity;

import java.time.Instant;
import java.util.UUID;

public final class SessionJpaEntityTestBuilder {

    private UUID id;
    private UUID ownerId;
    private String hashedRefreshToken;
    private Instant expiresAt;
    private Instant revokedAt;
    private String revocationReason;
    private Instant createdAt;

    private SessionJpaEntityTestBuilder() {
        this.id = TestSessionMother.DEFAULT_SESSION_ID.value();
        this.ownerId = TestSessionMother.EXISTING_OWNER_ID.value();
        this.hashedRefreshToken = TestSessionMother.DEFAULT_HASHED_REFRESH_TOKEN.value();
        this.expiresAt = TestSessionMother.DEFAULT_EXPIRES_AT.value();
        this.revokedAt = null;
        this.revocationReason = null;
        this.createdAt = Instant.now();
    }

    public static SessionJpaEntityTestBuilder aSessionJpaEntity() {
        return new SessionJpaEntityTestBuilder();
    }

    public SessionJpaEntityTestBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public SessionJpaEntityTestBuilder withOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public SessionJpaEntityTestBuilder withHashedRefreshToken(String hashedRefreshToken) {
        this.hashedRefreshToken = hashedRefreshToken;
        return this;
    }

    public SessionJpaEntityTestBuilder withExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
        return this;
    }

    public SessionJpaEntityTestBuilder withRevokedAt(Instant revokedAt) {
        this.revokedAt = revokedAt;
        return this;
    }

    public SessionJpaEntityTestBuilder withRevocationReason(String revocationReason) {
        this.revocationReason = revocationReason;
        return this;
    }

    public SessionJpaEntityTestBuilder withCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public SessionJpaEntity build() {
        return new SessionJpaEntity(
                id,
                ownerId,
                hashedRefreshToken,
                expiresAt,
                revokedAt,
                revocationReason,
                createdAt
        );
    }

}
