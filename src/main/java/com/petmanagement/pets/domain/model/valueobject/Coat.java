package com.petmanagement.pets.domain.model.valueobject;

import org.jmolecules.ddd.annotation.ValueObject;

import java.util.Objects;

@ValueObject
public record Coat(String value) {

    private static final int MAX_LENGTH = 100;

    public Coat {
        Objects.requireNonNull(value, "The coat cannot be null");

        value = value.trim();

        if (value.isEmpty())
            throw new IllegalArgumentException("The coat cannot be empty");
        if (value.length() > MAX_LENGTH)
            throw new IllegalArgumentException("The coat cannot exceed %d characters".formatted(MAX_LENGTH));
    }

}
