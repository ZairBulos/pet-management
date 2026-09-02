package com.petmanagement.health.domain.model.valueobject;

import org.jmolecules.ddd.annotation.ValueObject;

import java.util.Objects;
import java.util.UUID;

@ValueObject
public record WeightRecordId(UUID value) {

    public WeightRecordId {
        Objects.requireNonNull(value, "Weight record id cannot be null");
    }

    public static WeightRecordId generate() {
        return new WeightRecordId(UUID.randomUUID());
    }

    public static WeightRecordId of(UUID value) {
        return new WeightRecordId(value);
    }

    public static WeightRecordId of(String value) {
        Objects.requireNonNull(value, "UUID string cannot be null");
        return new WeightRecordId(UUID.fromString(value));
    }

}
