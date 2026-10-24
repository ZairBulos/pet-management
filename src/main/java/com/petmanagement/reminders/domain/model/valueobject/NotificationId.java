package com.petmanagement.reminders.domain.model.valueobject;

import org.jmolecules.ddd.annotation.ValueObject;

import java.util.Objects;
import java.util.UUID;

@ValueObject
public record NotificationId(UUID value) {

    public NotificationId {
        Objects.requireNonNull(value, "Notification id cannot be null");
    }

    public static NotificationId generate() {
        return new NotificationId(UUID.randomUUID());
    }

    public static NotificationId of(UUID value) {
        return new NotificationId(value);
    }

    public static NotificationId of(String value) {
        Objects.requireNonNull(value, "Notification id cannot be null");
        return new NotificationId(UUID.fromString(value));
    }

}
