package com.petmanagement.health.domain.model.valueobject;

import org.jmolecules.ddd.annotation.ValueObject;

import java.util.Objects;
import java.util.UUID;

@ValueObject
public record DewormingId(UUID value) {

    public DewormingId {
        Objects.requireNonNull(value, "Deworming id cannot be null");
    }

    public static DewormingId generate() {
        return new DewormingId(UUID.randomUUID());
    }

    public static DewormingId of(UUID value) {
        return new DewormingId(value);
    }

    public static DewormingId of(String value) {
        Objects.requireNonNull(value, "UUID string cannot be null");
        return new DewormingId(UUID.fromString(value));
    }

}
