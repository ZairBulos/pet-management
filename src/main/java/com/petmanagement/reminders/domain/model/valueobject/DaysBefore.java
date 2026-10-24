package com.petmanagement.reminders.domain.model.valueobject;

import org.jmolecules.ddd.annotation.ValueObject;

import java.util.List;

@ValueObject
public record DaysBefore(int value) {

    public static final DaysBefore SAME_DAY = new DaysBefore(0);
    public static final DaysBefore THREE_DAYS = new DaysBefore(3);

    public DaysBefore {
        if (value < 0)
            throw new IllegalArgumentException("Days before cannot be negative");
    }

    public static List<DaysBefore> defaults() {
        return List.of(SAME_DAY, THREE_DAYS);
    }

}
