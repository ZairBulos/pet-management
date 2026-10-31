package com.petmanagement.reminders.application.port.out;

import com.petmanagement.reminders.domain.model.aggregate.Reminder;
import com.petmanagement.reminders.domain.model.valueobject.SourceId;

import java.util.Optional;

public interface ReminderRepositoryPort {
    Optional<Reminder> findBySourceId(SourceId  sourceId);
    void save(Reminder reminder);
}
