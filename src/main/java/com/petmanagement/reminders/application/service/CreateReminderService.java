package com.petmanagement.reminders.application.service;

import com.petmanagement.reminders.application.port.in.CreateReminderUseCase;
import com.petmanagement.reminders.application.port.out.ReminderRepositoryPort;
import com.petmanagement.reminders.domain.model.aggregate.Reminder;
import com.petmanagement.reminders.domain.model.valueobject.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class CreateReminderService implements CreateReminderUseCase {

    private final ReminderRepositoryPort repository;

    CreateReminderService(ReminderRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public ReminderId execute(CreateReminderCommand command) {
        var sourceId = SourceId.of(command.sourceId());

        var existing = repository.findBySourceId(sourceId);
        if (existing.isPresent())
            return existing.get().getId();

        var reminder = Reminder.create(
                sourceId,
                command.sourceType(),
                PetId.of(command.petId()),
                new ReminderTitle(command.title()),
                new ReminderDueDate(command.dueDate())
        );

        repository.save(reminder);

        return reminder.getId();
    }

}
