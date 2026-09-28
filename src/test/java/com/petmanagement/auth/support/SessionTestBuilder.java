package com.petmanagement.auth.support;

import com.petmanagement.auth.domain.model.aggregate.Session;
import com.petmanagement.auth.domain.model.enums.SessionRevocationReason;
import com.petmanagement.auth.domain.model.valueobject.OwnerId;
import com.petmanagement.auth.domain.model.valueobject.SessionExpiresAt;
import com.petmanagement.auth.domain.model.valueobject.SessionHashedRefreshToken;
import com.petmanagement.auth.domain.model.valueobject.SessionId;

import java.time.Instant;
import java.util.function.UnaryOperator;

public final class SessionTestBuilder {

    private SessionId id;
    private OwnerId ownerId;
    private SessionHashedRefreshToken hashedRefreshToken;
    private SessionExpiresAt expiresAt;
    private Instant revokedAt;
    private SessionRevocationReason revocationReason;
    private Instant createdAt;

    private SessionTestBuilder() {
        this.id = TestSessionMother.DEFAULT_SESSION_ID;
        this.ownerId = TestSessionMother.EXISTING_OWNER_ID;
        this.hashedRefreshToken = TestSessionMother.DEFAULT_HASHED_REFRESH_TOKEN;
        this.expiresAt = TestSessionMother.DEFAULT_EXPIRES_AT;
        this.revokedAt = null;
        this.revocationReason = null;
        this.createdAt = Instant.now();
    }

    public static SessionTestBuilder aSession() {
        return new SessionTestBuilder();
    }

    public SessionTestBuilder withId(SessionId id) {
        this.id = id;
        return this;
    }

    public SessionTestBuilder withId(String id) {
        this.id = SessionId.of(id);
        return this;
    }

    public SessionTestBuilder withOwnerId(OwnerId ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public SessionTestBuilder withOwnerId(String ownerId) {
        this.ownerId = OwnerId.of(ownerId);
        return this;
    }

    public SessionTestBuilder withHashedRefreshToken(SessionHashedRefreshToken hashedRefreshToken) {
        this.hashedRefreshToken = hashedRefreshToken;
        return this;
    }

    public SessionTestBuilder withHashedRefreshToken(String refreshToken, UnaryOperator<String> hasher) {
        this.hashedRefreshToken = SessionHashedRefreshToken.from(refreshToken, hasher);
        return this;
    }

    public SessionTestBuilder withExpiresAt(SessionExpiresAt expiresAt) {
        this.expiresAt = expiresAt;
        return this;
    }

    public SessionTestBuilder withRevokedAt(Instant revokedAt) {
        this.revokedAt = revokedAt;
        return this;
    }

    public SessionTestBuilder withRevocationReason(SessionRevocationReason revocationReason) {
        this.revocationReason = revocationReason;
        return this;
    }

    public SessionTestBuilder withCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Session build() {
        return Session.reconstitute(
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
