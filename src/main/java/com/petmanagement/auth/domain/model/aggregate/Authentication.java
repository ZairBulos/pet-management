package com.petmanagement.auth.domain.model.aggregate;

import com.petmanagement.auth.domain.event.AuthenticationCreated;
import com.petmanagement.auth.domain.exception.AuthenticationAlreadyUsedException;
import com.petmanagement.auth.domain.exception.AuthenticationExpiredException;
import com.petmanagement.auth.domain.exception.InvalidAuthenticationCodeException;
import com.petmanagement.auth.domain.model.valueobject.*;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.event.types.DomainEvent;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.UnaryOperator;

@AggregateRoot
public final class Authentication {

    private final AuthenticationId id;
    private final Email email;
    private final AuthenticationHashedCode hashedCode;
    private final AuthenticationExpiresAt expiresAt;
    private Instant authenticatedAt;
    private final Instant createdAt;

    private final List<DomainEvent> events = new ArrayList<>();

    private Authentication(
            AuthenticationId id,
            Email email,
            AuthenticationHashedCode hashedCode,
            AuthenticationExpiresAt expiresAt,
            Instant authenticatedAt,
            Instant createdAt
    ) {
        this.id = Objects.requireNonNull(id, "Authentication id cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.hashedCode = Objects.requireNonNull(hashedCode, "HashedCode cannot be null");
        this.expiresAt = Objects.requireNonNull(expiresAt, "ExpiresAt cannot be null");
        this.authenticatedAt = authenticatedAt;
        this.createdAt = Objects.requireNonNull(createdAt, "CreatedAt cannot be null");
    }

    // === Factory ===

    public static Authentication create(
            Email email,
            UnaryOperator<String> hasher
    ) {
        var now = Instant.now();
        var plainCode = AuthenticationCode.generate();
        var hashedCode = AuthenticationHashedCode.from(plainCode.value(), hasher);
        var expiresAt = AuthenticationExpiresAt.generate();

        var authentication = new Authentication(
                AuthenticationId.generate(),
                email,
                hashedCode,
                expiresAt,
                null,
                now
        );

        authentication.events.add(new AuthenticationCreated(
                authentication.id.value(),
                authentication.email.value(),
                plainCode.value(),
                now
        ));

        return authentication;
    }

    public static Authentication reconstitute(
            AuthenticationId id,
            Email email,
            AuthenticationHashedCode hashedCode,
            AuthenticationExpiresAt expiresAt,
            Instant authenticatedAt,
            Instant createdAt
    ) {
        return new Authentication(id, email, hashedCode, expiresAt, authenticatedAt, createdAt);
    }

    // === Business Operations ===

    public void authenticate(
            AuthenticationCode code,
            BiPredicate<String, String> verifier
    ) {
        if (isAuthenticated())
            throw new AuthenticationAlreadyUsedException();

        if (isExpired(Instant.now()))
            throw new AuthenticationExpiredException();

        if (!hashedCode.matches(code.value(), verifier))
            throw new InvalidAuthenticationCodeException();

        this.authenticatedAt = Instant.now();
    }

    public boolean isAuthenticated() {
        return authenticatedAt != null;
    }

    public boolean isExpired(Instant now) {
        return expiresAt.isExpired(now);
    }

    // === Events ===

    public List<DomainEvent> pullEvents() {
        var pendingEvents = List.copyOf(events);
        events.clear();
        return pendingEvents;
    }

    // === Getters ===

    public AuthenticationId getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public AuthenticationHashedCode getHashedCode() {
        return hashedCode;
    }

    public AuthenticationExpiresAt getExpiresAt() {
        return expiresAt;
    }

    public Instant getAuthenticatedAt() {
        return authenticatedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    // === Object Methods ===

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Authentication other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Authentication{" +
                "id=" + id +
                ", email=" + email +
                ", expiresAt=" + expiresAt +
                ", authenticatedAt=" + authenticatedAt +
                ", createdAt=" + createdAt +
                '}';
    }

}
