package com.petmanagement.health.domain.model.valueobject;

import org.jmolecules.ddd.annotation.ValueObject;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@ValueObject
public record NextDueDate(LocalDate value) {

    public NextDueDate {
        Objects.requireNonNull(value, "Next due date cannot be null");
    }

    public static NextDueDate after(LocalDate eventDate, LocalDate dueDate) {
        Objects.requireNonNull(eventDate, "Event date cannot be null");
        Objects.requireNonNull(dueDate, "Due date cannot be null");

        if (!dueDate.isAfter(eventDate))
            throw new IllegalArgumentException("Next due date must be after base date");

        return new NextDueDate(dueDate);
    }

    public long daysRemaining(LocalDate today) {
        Objects.requireNonNull(today, "Reference date cannot be null");
        return ChronoUnit.DAYS.between(today, value);
    }

    public boolean isOverdue(LocalDate today) {
        Objects.requireNonNull(today, "Reference date cannot be null");
        return value.isBefore(today);
    }

}
