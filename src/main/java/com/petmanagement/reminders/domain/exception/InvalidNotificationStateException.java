package com.petmanagement.reminders.domain.exception;

import com.petmanagement.reminders.domain.model.enums.NotificationStatus;

public class InvalidNotificationStateException extends RuntimeException {
    public InvalidNotificationStateException(String operation, NotificationStatus current) {
        super("Cannot perform operation " + operation + " with current status " + current);
    }
}
