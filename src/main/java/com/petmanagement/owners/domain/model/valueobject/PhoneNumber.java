package com.petmanagement.owners.domain.model.valueobject;

import org.jmolecules.ddd.annotation.ValueObject;

import java.util.Objects;
import java.util.regex.Pattern;

@ValueObject
public record PhoneNumber(String value) {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?\\d{7,15}$");

    public PhoneNumber {
        Objects.requireNonNull(value, "The phone number cannot be null");

        value = value.trim().replaceAll("[\\s()\\-.]", "");

        if (value.isEmpty())
            throw new IllegalArgumentException("The phone number cannot be empty");
        if (!value.matches("^\\+?\\d+$"))
            throw new IllegalArgumentException("The phone number must contain only digits, spaces, hyphens, or parentheses");
        if (!PHONE_PATTERN.matcher(value).matches())
            throw new IllegalArgumentException("The phone number must have between 7 and 15 digits");
    }

}
