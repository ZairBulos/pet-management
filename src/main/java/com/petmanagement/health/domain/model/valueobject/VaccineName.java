package com.petmanagement.health.domain.model.valueobject;

import org.jmolecules.ddd.annotation.ValueObject;

import java.util.Objects;

@ValueObject
public record VaccineName(String value) {

    private static final int MAX_LENGTH = 100;

    public VaccineName {
        Objects.requireNonNull(value, "Vaccine name cannot be null");

        value = value.trim();

        if (value.isEmpty())
            throw new IllegalArgumentException("Vaccine name cannot be empty");
        if (value.length() > MAX_LENGTH)
            throw new IllegalArgumentException("Vaccine name cannot exceed %d characters".formatted(MAX_LENGTH));
    }

}
