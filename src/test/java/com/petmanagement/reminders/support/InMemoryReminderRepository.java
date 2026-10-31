package com.petmanagement.reminders.support;

import com.petmanagement.reminders.application.port.out.ReminderRepositoryPort;
import com.petmanagement.reminders.domain.model.aggregate.Reminder;
import com.petmanagement.reminders.domain.model.valueobject.ReminderId;
import com.petmanagement.reminders.domain.model.valueobject.SourceId;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class InMemoryReminderRepository implements ReminderRepositoryPort {

    private final Map<ReminderId, Reminder> reminders = new HashMap<>();

    @Override
    public Optional<Reminder> findBySourceId(SourceId sourceId) {
        return reminders.values().stream()
                .filter(reminder -> reminder.getSourceId().equals(sourceId))
                .findFirst();
    }

    @Override
    public void save(Reminder reminder) {
        reminders.put(reminder.getId(), reminder);
    }

    // === Helpers ===

    public void clear() {
        reminders.clear();
    }

    public void saveAll(Reminder... reminders) {
        for (Reminder reminder : reminders) {
            save(reminder);
        }
    }

    public int count() {
        return reminders.size();
    }

}
