package com.petmanagement.auth.domain.model.valueobject;

import org.jmolecules.ddd.annotation.ValueObject;

import java.util.Objects;
import java.util.UUID;

@ValueObject
public record SessionId(UUID value) {

    public SessionId {
        Objects.requireNonNull(value, "Deworming id cannot be null");
    }

    public static SessionId generate() {
        return new SessionId(UUID.randomUUID());
    }

    public static SessionId of(UUID value) {
        return new SessionId(value);
    }

    public static SessionId of(String value) {
        Objects.requireNonNull(value, "UUID string cannot be null");
        return new SessionId(UUID.fromString(value));
    }

}
