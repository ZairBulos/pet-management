package com.petmanagement.reminders.domain.exception;

public class ReminderNotFoundException extends RuntimeException {
    public ReminderNotFoundException() {
        super("Reminder not found");
    }
}
