package com.petmanagement.auth.support;

import com.petmanagement.auth.infrastructure.adapter.out.persistence.entity.AuthenticationJpaEntity;

import java.time.Instant;
import java.util.UUID;

public final class AuthenticationJpaEntityTestBuilder {

    private UUID id;
    private String email;
    private String hashedCode;
    private Instant expiresAt;
    private Instant authenticatedAt;
    private Instant createdAt;

    private AuthenticationJpaEntityTestBuilder() {
        this.id = TestAuthenticationMother.DEFAULT_AUTHENTICATION_ID.value();
        this.email = TestAuthenticationMother.EMAIL_JOHN.value();
        this.hashedCode = TestAuthenticationMother.DEFAULT_HASHED_CODE.value();
        this.expiresAt = TestAuthenticationMother.DEFAULT_EXPIRES_AT.value();
        this.authenticatedAt = null;
        this.createdAt = Instant.now();
    }

    public static AuthenticationJpaEntityTestBuilder aAuthenticationJpaEntity() {
        return new AuthenticationJpaEntityTestBuilder();
    }

    public AuthenticationJpaEntityTestBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public AuthenticationJpaEntityTestBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public AuthenticationJpaEntityTestBuilder withHashedCode(String hashedCode) {
        this.hashedCode = hashedCode;
        return this;
    }

    public AuthenticationJpaEntityTestBuilder withExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
        return this;
    }

    public AuthenticationJpaEntityTestBuilder withAuthenticatedAt(Instant authenticatedAt) {
        this.authenticatedAt = authenticatedAt;
        return this;
    }

    public AuthenticationJpaEntityTestBuilder withCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public AuthenticationJpaEntity build() {
        return new AuthenticationJpaEntity(
                id,
                email,
                hashedCode,
                expiresAt,
                authenticatedAt,
                createdAt
        );
    }

}
