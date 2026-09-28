package com.petmanagement.auth.domain.model.valueobject;

import org.jmolecules.ddd.annotation.ValueObject;

import java.util.Objects;
import java.util.function.UnaryOperator;

@ValueObject
public record SessionHashedRefreshToken(String value) {

    public SessionHashedRefreshToken {
        Objects.requireNonNull(value, "Session hashed refresh token cannot be null");
    }

    public static SessionHashedRefreshToken from(String plainToken, UnaryOperator<String> hasher) {
        Objects.requireNonNull(plainToken, "Refresh token cannot be null");
        Objects.requireNonNull(hasher, "Hasher cannot be null");

        return new SessionHashedRefreshToken(hasher.apply(plainToken));
    }

}
