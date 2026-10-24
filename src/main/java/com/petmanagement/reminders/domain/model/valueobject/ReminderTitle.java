package com.petmanagement.reminders.domain.model.valueobject;

import org.jmolecules.ddd.annotation.ValueObject;

import java.util.Objects;

@ValueObject
public record ReminderTitle(String value) {

    private static final int MAX_LENGTH = 100;

    public ReminderTitle {
        Objects.requireNonNull(value, "Reminder title cannot be null");

        value = value.trim();

        if (value.isEmpty())
            throw new IllegalArgumentException("Reminder title cannot be empty");
        if (value.length() > MAX_LENGTH)
            throw new IllegalArgumentException("Reminder title cannot exceed %d characters".formatted(MAX_LENGTH));
    }

}
