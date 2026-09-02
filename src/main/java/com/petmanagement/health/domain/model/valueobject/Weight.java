package com.petmanagement.health.domain.model.valueobject;

import org.jmolecules.ddd.annotation.ValueObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@ValueObject
public record Weight(BigDecimal value) {

    private static final int SCALE = 2;

    public Weight {
        Objects.requireNonNull(value, "Weight cannot be null");

        value = value.setScale(SCALE, RoundingMode.HALF_UP);

        if (value.signum() <= 0)
            throw new IllegalArgumentException("Weight must be greater than zero");
    }

    public static Weight of(BigDecimal value) {
        return new Weight(value);
    }

    public static Weight of(double value) {
        return new Weight(BigDecimal.valueOf(value));
    }

}
