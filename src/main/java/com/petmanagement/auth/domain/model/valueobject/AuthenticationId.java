package com.petmanagement.auth.domain.model.valueobject;

import org.jmolecules.ddd.annotation.ValueObject;

import java.util.Objects;
import java.util.UUID;

@ValueObject
public record AuthenticationId(UUID value) {

    public AuthenticationId {
        Objects.requireNonNull(value, "Deworming id cannot be null");
    }

    public static AuthenticationId generate() {
        return new AuthenticationId(UUID.randomUUID());
    }

    public static AuthenticationId of(UUID value) {
        return new AuthenticationId(value);
    }

    public static AuthenticationId of(String value) {
        Objects.requireNonNull(value, "UUID string cannot be null");
        return new AuthenticationId(UUID.fromString(value));
    }

}
