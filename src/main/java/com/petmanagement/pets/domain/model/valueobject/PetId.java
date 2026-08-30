package com.petmanagement.pets.domain.model.valueobject;

import org.jmolecules.ddd.annotation.ValueObject;

import java.util.Objects;
import java.util.UUID;

@ValueObject
public record PetId(UUID value) {

    public PetId {
        Objects.requireNonNull(value, "The pet ID cannot be null");
    }

    public static PetId generate() {
        return new PetId(UUID.randomUUID());
    }

    public static PetId of(UUID value) {
        return new PetId(value);
    }

    public static PetId of(String value) {
        Objects.requireNonNull(value, "UUID string cannot be null");
        return new PetId(UUID.fromString(value));
    }

}
