package com.petmanagement.auth.support;

import com.petmanagement.auth.application.port.in.RequestAuthenticationUseCase;
import com.petmanagement.auth.domain.model.aggregate.Authentication;
import com.petmanagement.auth.domain.model.valueobject.AuthenticationExpiresAt;
import com.petmanagement.auth.domain.model.valueobject.AuthenticationHashedCode;
import com.petmanagement.auth.domain.model.valueobject.AuthenticationId;
import com.petmanagement.auth.domain.model.valueobject.Email;

import java.time.Instant;
import java.util.function.UnaryOperator;

public final class AuthenticationTestBuilder {

    private AuthenticationId id;
    private Email email;
    private AuthenticationHashedCode hashedCode;
    private AuthenticationExpiresAt expiresAt;
    private Instant authenticatedAt;
    private Instant createdAt;

    private AuthenticationTestBuilder() {
        this.id = TestAuthenticationMother.DEFAULT_AUTHENTICATION_ID;
        this.email = TestAuthenticationMother.EMAIL_JOHN;
        this.hashedCode = TestAuthenticationMother.DEFAULT_HASHED_CODE;
        this.expiresAt = TestAuthenticationMother.DEFAULT_EXPIRES_AT;
        this.authenticatedAt = null;
        this.createdAt = Instant.now();
    }

    public static AuthenticationTestBuilder aAuthentication() {
        return new AuthenticationTestBuilder();
    }

    public AuthenticationTestBuilder withId(AuthenticationId id) {
        this.id = id;
        return this;
    }

    public AuthenticationTestBuilder withId(String id) {
        this.id = AuthenticationId.of(id);
        return this;
    }

    public AuthenticationTestBuilder withEmail(Email email) {
        this.email = email;
        return this;
    }

    public AuthenticationTestBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    public AuthenticationTestBuilder withHashedCode(AuthenticationHashedCode hashedCode) {
        this.hashedCode = hashedCode;
        return this;
    }

    public AuthenticationTestBuilder withHashedCode(String plainCode, UnaryOperator<String> hasher) {
        this.hashedCode = AuthenticationHashedCode.from(plainCode, hasher);
        return this;
    }

    public AuthenticationTestBuilder withExpiresAt(AuthenticationExpiresAt expiresAt) {
        this.expiresAt = expiresAt;
        return this;
    }

    public AuthenticationTestBuilder withAuthenticatedAt(Instant authenticatedAt) {
        this.authenticatedAt = authenticatedAt;
        return this;
    }

    public AuthenticationTestBuilder withCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Authentication build() {
        return Authentication.reconstitute(
                id,
                email,
                hashedCode,
                expiresAt,
                authenticatedAt,
                createdAt
        );
    }

    public static class RequestAuthenticationCommandBuilder {

        private Email email;

        private RequestAuthenticationCommandBuilder() {
            this.email = TestAuthenticationMother.EMAIL_JOHN;
        }

        public static RequestAuthenticationCommandBuilder aRequestAuthenticationCommand() {
            return new RequestAuthenticationCommandBuilder();
        }

        public RequestAuthenticationCommandBuilder withEmail(Email email) {
            this.email = email;
            return this;
        }

        public RequestAuthenticationCommandBuilder withEmail(String email) {
            this.email = new Email(email);
            return this;
        }

        public RequestAuthenticationUseCase.RequestAuthenticationCommand build() {
            return new RequestAuthenticationUseCase.RequestAuthenticationCommand(email);
        }

    }

}
