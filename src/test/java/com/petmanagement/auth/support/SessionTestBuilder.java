package com.petmanagement.auth.support;

import com.petmanagement.auth.application.port.in.RefreshSessionUseCase;
import com.petmanagement.auth.application.port.in.RevokeSessionUseCase;
import com.petmanagement.auth.domain.model.aggregate.Session;
import com.petmanagement.auth.domain.model.enums.SessionRevocationReason;
import com.petmanagement.auth.domain.model.valueobject.*;

import java.time.Instant;

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

    public static class RevokeSessionCommandBuilder {

        private SessionRefreshToken refreshToken;

        private RevokeSessionCommandBuilder() {
            this.refreshToken = TestSessionMother.DEFAULT_PLAIN_REFRESH_TOKEN;
        }

        public static RevokeSessionCommandBuilder aRevokeSessionCommand() {
            return new RevokeSessionCommandBuilder();
        }

        public RevokeSessionCommandBuilder withRefreshToken(SessionRefreshToken refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public RevokeSessionUseCase.RevokeSessionCommand build() {
            return new RevokeSessionUseCase.RevokeSessionCommand(refreshToken);
        }

    }

    public static class RefreshSessionCommandBuilder {

        private SessionRefreshToken refreshToken;

        private RefreshSessionCommandBuilder() {
            this.refreshToken = TestSessionMother.ANOTHER_PLAIN_REFRESH_TOKEN;
        }

        public static RefreshSessionCommandBuilder aRefreshSessionCommand() {
            return new RefreshSessionCommandBuilder();
        }

        public RefreshSessionCommandBuilder withRefreshToken(SessionRefreshToken refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public RefreshSessionUseCase.RefreshSessionCommand build() {
            return new RefreshSessionUseCase.RefreshSessionCommand(refreshToken);
        }

    }

}
