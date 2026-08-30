package com.petmanagement.pets.domain.model.valueobject;

import org.jmolecules.ddd.annotation.AggregateRoot;

import java.util.Objects;
import java.util.UUID;

@AggregateRoot
public record OwnerId(UUID value) {

    public OwnerId {
        Objects.requireNonNull(value, "The owner's ID cannot be null");
    }

    public static OwnerId of(UUID value) {
        return new OwnerId(value);
    }

    public static OwnerId of(String value) {
        Objects.requireNonNull(value, "UUID string cannot be null");
        return new OwnerId(UUID.fromString(value));
    }

}
