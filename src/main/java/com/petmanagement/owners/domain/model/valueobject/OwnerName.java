package com.petmanagement.owners.domain.model.valueobject;

import org.jmolecules.ddd.annotation.ValueObject;

import java.util.Objects;

@ValueObject
public record OwnerName(String value) {

    private static final int MAX_LENGTH = 150;
    private static final int MIN_LENGTH = 2;

    public OwnerName {
        Objects.requireNonNull(value, "The owner's name cannot be null");

        value = value.trim();

        if (value.isEmpty())
            throw new IllegalArgumentException("The owner's name cannot be empty");
        if (value.length() < MIN_LENGTH)
            throw new IllegalArgumentException("The owner's name must be at least %d characters".formatted(MIN_LENGTH));
        if (value.length() > MAX_LENGTH)
            throw new IllegalArgumentException("The owner's name cannot exceed %d characters".formatted(MAX_LENGTH));
    }

}
