package com.petmanagement.reminders.application.port.in;

import com.petmanagement.reminders.domain.model.enums.SourceType;
import com.petmanagement.reminders.domain.model.valueobject.ReminderId;

import java.time.LocalDate;
import java.util.UUID;

public interface CreateReminderUseCase {
    ReminderId execute(CreateReminderCommand command);

    record CreateReminderCommand(
            UUID sourceId,
            SourceType sourceType,
            UUID petId,
            String title,
            LocalDate dueDate
    ) {}
}
