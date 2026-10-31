package com.petmanagement.reminders.application.service;

import com.petmanagement.reminders.application.port.in.RescheduleReminderUseCase;
import com.petmanagement.reminders.application.port.out.ReminderRepositoryPort;
import com.petmanagement.reminders.domain.exception.ReminderNotFoundException;
import com.petmanagement.reminders.domain.model.valueobject.ReminderDueDate;
import com.petmanagement.reminders.domain.model.valueobject.SourceId;
import org.springframework.stereotype.Service;

@Service
class RescheduleReminderService implements RescheduleReminderUseCase {

    private final ReminderRepositoryPort repository;

    RescheduleReminderService(ReminderRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public void execute(RescheduleReminderCommand command) {
        var sourceId = SourceId.of(command.sourceId());

        var reminder = repository.findBySourceId(sourceId)
                .orElseThrow(ReminderNotFoundException::new);

        reminder.reschedule(new ReminderDueDate(command.dueDate()));

        repository.save(reminder);
    }

}
