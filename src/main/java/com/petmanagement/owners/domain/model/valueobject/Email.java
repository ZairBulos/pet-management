package com.petmanagement.owners.domain.model.valueobject;

import org.jmolecules.ddd.annotation.ValueObject;

import java.util.Objects;
import java.util.regex.Pattern;

@ValueObject
public record Email(String value) {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final int MAX_LENGTH = 254;

    public Email {
        Objects.requireNonNull(value, "The email cannot be null");

        value = value.trim().toLowerCase();

        if (value.isEmpty())
            throw new IllegalArgumentException("The email cannot be empty");
        if (value.length() > MAX_LENGTH)
            throw new IllegalArgumentException("The email cannot exceed %d characters".formatted(MAX_LENGTH));
        if (!EMAIL_PATTERN.matcher(value).matches())
            throw new IllegalArgumentException("The email '%s' is not in a valid format".formatted(value));
    }

}
