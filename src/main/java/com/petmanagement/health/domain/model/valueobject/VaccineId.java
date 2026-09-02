package com.petmanagement.health.domain.model.valueobject;

import org.jmolecules.ddd.annotation.ValueObject;

import java.util.Objects;
import java.util.UUID;

@ValueObject
public record VaccineId(UUID value) {

    public VaccineId {
        Objects.requireNonNull(value, "Vaccine id cannot be null");
    }

    public static VaccineId generate() {
        return new VaccineId(UUID.randomUUID());
    }

    public static VaccineId of(UUID value) {
        return new VaccineId(value);
    }

    public static VaccineId of(String value) {
        Objects.requireNonNull(value, "UUID string cannot be null");
        return new VaccineId(UUID.fromString(value));
    }

}
