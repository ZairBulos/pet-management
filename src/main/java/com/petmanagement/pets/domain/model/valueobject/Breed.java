package com.petmanagement.pets.domain.model.valueobject;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record Breed(String value) {

    private static final int MAX_LENGTH = 100;

    public Breed {
        if (value != null) {
            value = value.trim();

            if (value.isEmpty())
                throw new IllegalArgumentException("The breed cannot be empty if it is specified");
            if (value.length() > MAX_LENGTH)
                throw new IllegalArgumentException("The breed cannot exceed %d characters".formatted(MAX_LENGTH));
        }
    }

    public static Breed unknown() {
        return new Breed(null);
    }

    public boolean isUnknown() {
        return value == null;
    }

}
