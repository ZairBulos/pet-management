package com.petmanagement.pets.domain.model.valueobject;

import org.jmolecules.ddd.annotation.ValueObject;

import java.util.Objects;

@ValueObject
public record Species(String value) {

    private static final int MAX_LENGTH = 60;

    public Species {
        Objects.requireNonNull(value, "The species cannot be null");

        value = value.trim();

        if (value.isEmpty())
            throw new IllegalArgumentException("The species cannot be empty");
        if (value.length() > MAX_LENGTH)
            throw new IllegalArgumentException("The species name cannot exceed %d characters".formatted(MAX_LENGTH));
    }

}
