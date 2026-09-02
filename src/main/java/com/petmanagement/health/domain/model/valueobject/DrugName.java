package com.petmanagement.health.domain.model.valueobject;

import org.jmolecules.ddd.annotation.ValueObject;

import java.util.Objects;

@ValueObject
public record DrugName(String value) {

    private static final int MAX_LENGTH = 100;

    public DrugName {
        Objects.requireNonNull(value, "Drug name cannot be null");

        value = value.trim();

        if (value.isEmpty())
            throw new IllegalArgumentException("Drug name cannot be empty");
        if (value.length() > MAX_LENGTH)
            throw new IllegalArgumentException("Drug name cannot exceed %d characters".formatted(MAX_LENGTH));
    }

}
