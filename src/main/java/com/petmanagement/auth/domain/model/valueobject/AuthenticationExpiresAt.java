package com.petmanagement.auth.domain.model.valueobject;

import org.jmolecules.ddd.annotation.ValueObject;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

@ValueObject
public record AuthenticationExpiresAt(Instant value) {

    private static final Duration DEFAULT_TTL = Duration.ofMinutes(5);

    public AuthenticationExpiresAt {
        Objects.requireNonNull(value, "Authentication expires at cannot be null");
    }

    public static AuthenticationExpiresAt generate() {
        return new AuthenticationExpiresAt(Instant.now().plus(DEFAULT_TTL));
    }

    public static AuthenticationExpiresAt generate(Duration ttl) {
        Objects.requireNonNull(ttl, "Authentication TTL cannot be null");
        return new AuthenticationExpiresAt(Instant.now().plus(ttl));
    }

    public boolean isExpired(Instant now) {
        return now.isAfter(value);
    }

}
