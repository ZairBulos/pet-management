package com.petmanagement.auth.domain.model.valueobject;

import org.jmolecules.ddd.annotation.ValueObject;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

@ValueObject
public record SessionExpiresAt(Instant value) {

    private static final Duration DEFAULT_TTL = Duration.ofDays(7);

    public SessionExpiresAt {
        Objects.requireNonNull(value, "Authentication expires at cannot be null");
    }

    public static SessionExpiresAt generate() {
        return new SessionExpiresAt(Instant.now().plus(DEFAULT_TTL));
    }

    public static SessionExpiresAt generate(Duration ttl) {
        Objects.requireNonNull(ttl, "Authentication TTL cannot be null");
        return new SessionExpiresAt(Instant.now().plus(ttl));
    }

    public boolean isExpired(Instant now) {
        return now.isAfter(value);
    }

}
