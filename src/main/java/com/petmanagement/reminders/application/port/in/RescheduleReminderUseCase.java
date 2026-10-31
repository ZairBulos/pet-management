package com.petmanagement.reminders.application.port.in;

import com.petmanagement.reminders.domain.model.enums.SourceType;

import java.time.LocalDate;
import java.util.UUID;

public interface RescheduleReminderUseCase {
    void execute(RescheduleReminderCommand command);

    record RescheduleReminderCommand(
            UUID sourceId,
            SourceType sourceType,
            LocalDate dueDate
    ) {}
}
