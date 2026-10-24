package com.petmanagement.reminders.domain.exception;

public class NotificationNotFoundException extends RuntimeException {
    public NotificationNotFoundException() {
        super("Notification not found");
    }
}
