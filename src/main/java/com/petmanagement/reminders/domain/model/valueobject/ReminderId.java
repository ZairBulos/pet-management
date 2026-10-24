package com.petmanagement.reminders.domain.model.valueobject;

import org.jmolecules.ddd.annotation.ValueObject;

import java.util.Objects;
import java.util.UUID;

@ValueObject
public record ReminderId(UUID value) {

    public ReminderId {
        Objects.requireNonNull(value, "Reminder id cannot be null");
    }

    public static ReminderId generate() {
        return new ReminderId(UUID.randomUUID());
    }

    public static ReminderId of(UUID value) {
        return new ReminderId(value);
    }

    public static ReminderId of(String value) {
        Objects.requireNonNull(value, "Reminder id cannot be null");
        return new ReminderId(UUID.fromString(value));
    }

}
