package com.petmanagement.pets.domain.model.valueobject;

import org.jmolecules.ddd.annotation.ValueObject;

import java.util.Objects;

@ValueObject
public record PetName(String value) {

    private static final int MAX_LENGTH = 100;
    private static final int MIN_LENGTH = 2;

    public PetName {
        Objects.requireNonNull(value, "The pet's name cannot be null");

        value = value.trim();

        if (value.isEmpty())
            throw new IllegalArgumentException("The pet's name cannot be empty");
        if (value.length() < MIN_LENGTH)
            throw new IllegalArgumentException("The pet's name must be at least %d characters".formatted(MIN_LENGTH));
        if (value.length() > MAX_LENGTH)
            throw new IllegalArgumentException("The pet's name cannot exceed %d characters".formatted(MAX_LENGTH));
    }

}
