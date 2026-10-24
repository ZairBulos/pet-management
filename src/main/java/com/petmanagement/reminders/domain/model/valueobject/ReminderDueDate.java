package com.petmanagement.reminders.domain.model.valueobject;

import org.jmolecules.ddd.annotation.ValueObject;

import java.time.LocalDate;
import java.util.Objects;

@ValueObject
public record ReminderDueDate(LocalDate value) {

    public ReminderDueDate {
        Objects.requireNonNull(value, "Reminder due date cannot be null");
    }

}
