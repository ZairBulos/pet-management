package com.petmanagement.health.domain.model.valueobject;

import org.jmolecules.ddd.annotation.ValueObject;

import java.util.Objects;

@ValueObject
public record DrugDose(String value) {

    private static final int MAX_LENGTH = 100;

    public DrugDose {
        Objects.requireNonNull(value, "Drug dose cannot be null");

        value = value.trim();

        if (value.isEmpty())
            throw new IllegalArgumentException("Drug dose cannot be empty");
        if (value.length() > MAX_LENGTH)
            throw new IllegalArgumentException("Drug dose cannot exceed %d characters".formatted(MAX_LENGTH));
    }

}
