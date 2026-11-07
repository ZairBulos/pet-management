package com.petmanagement.reminders.infrastructure.adapter.out.persistence.mapper;

import com.petmanagement.reminders.domain.model.aggregate.Reminder;
import com.petmanagement.reminders.domain.model.entity.ReminderNotification;
import com.petmanagement.reminders.domain.model.valueobject.*;
import com.petmanagement.reminders.infrastructure.adapter.out.persistence.entity.ReminderJpaEntity;
import com.petmanagement.reminders.infrastructure.adapter.out.persistence.entity.ReminderNotificationJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ReminderMapper {

    public ReminderJpaEntity toJpaEntity(Reminder reminder) {
        var entity = new ReminderJpaEntity(
                reminder.getId().value(),
                reminder.getSourceType(),
                reminder.getSourceId().value(),
                reminder.getPetId().value(),
                reminder.getTitle().value(),
                reminder.getDueDate().value(),
                reminder.getCreatedAt(),
                reminder.getUpdatedAt()
        );

        reminder.getNotifications()
                .forEach(notification -> entity.addNotification(toJpaEntity(notification)));

        return entity;
    }

    private ReminderNotificationJpaEntity toJpaEntity(ReminderNotification notification) {
        return new ReminderNotificationJpaEntity(
                notification.getId().value(),
                (short) notification.getDaysBefore().value(),
                notification.getScheduledFor(),
                notification.getStatus(),
                notification.getSentAt(),
                notification.getLastAttemptAt(),
                notification.getAttempts(),
                notification.getLastError()
        );
    }

    public Reminder toDomain(ReminderJpaEntity entity) {
        var notifications = entity.getNotifications().stream()
                .map(this::toDomain)
                .toList();

        return Reminder.reconstitute(
                ReminderId.of(entity.getId()),
                SourceId.of(entity.getSourceId()),
                entity.getSourceType(),
                PetId.of(entity.getPetId()),
                new ReminderTitle(entity.getTitle()),
                new ReminderDueDate(entity.getDueDate()),
                notifications,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    private ReminderNotification toDomain(ReminderNotificationJpaEntity entity) {
        return ReminderNotification.reconstitute(
                NotificationId.of(entity.getId()),
                new DaysBefore(entity.getDaysBefore()),
                entity.getScheduledFor(),
                entity.getStatus(),
                entity.getSentAt(),
                entity.getLastAttemptAt(),
                entity.getAttempts(),
                entity.getLastError()
        );
    }

}
