package com.petmanagement.reminders.support;

import com.petmanagement.reminders.domain.model.enums.NotificationStatus;
import com.petmanagement.reminders.infrastructure.adapter.out.persistence.entity.ReminderNotificationJpaEntity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public final class ReminderNotificationJpaEntityTestBuilder {

    private UUID id;
    private short daysBefore;
    private LocalDate scheduledFor;
    private NotificationStatus status;
    private Instant sentAt;
    private Instant lastAttemptAt;
    private int attempts;
    private String lastError;

    private ReminderNotificationJpaEntityTestBuilder() {
        this.id = TestReminderNotificationMother.DEFAULT_NOTIFICATION_ID.value();
        this.daysBefore = 3;
        this.scheduledFor = TestReminderNotificationMother.DEFAULT_SCHEDULED_FOR;
        this.status = NotificationStatus.PENDING;
        this.sentAt = null;
        this.lastAttemptAt = null;
        this.attempts = 0;
        this.lastError = null;
    }

    public static ReminderNotificationJpaEntityTestBuilder aReminderNotificationJpaEntity() {
        return new ReminderNotificationJpaEntityTestBuilder();
    }

    public ReminderNotificationJpaEntityTestBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public ReminderNotificationJpaEntityTestBuilder withDaysBefore(int daysBefore) {
        this.daysBefore = (short) daysBefore;
        return this;
    }

    public ReminderNotificationJpaEntityTestBuilder withScheduledFor(LocalDate scheduledFor) {
        this.scheduledFor = scheduledFor;
        return this;
    }

    public ReminderNotificationJpaEntityTestBuilder withStatus(NotificationStatus status) {
        this.status = status;
        return this;
    }

    public ReminderNotificationJpaEntityTestBuilder withSentAt(Instant sentAt) {
        this.sentAt = sentAt;
        return this;
    }

    public ReminderNotificationJpaEntityTestBuilder withLastAttemptAt(Instant lastAttemptAt) {
        this.lastAttemptAt = lastAttemptAt;
        return this;
    }

    public ReminderNotificationJpaEntityTestBuilder withAttempts(int attempts) {
        this.attempts = attempts;
        return this;
    }

    public ReminderNotificationJpaEntityTestBuilder withLastError(String lastError) {
        this.lastError = lastError;
        return this;
    }

    public ReminderNotificationJpaEntity build() {
        return new ReminderNotificationJpaEntity(
                id,
                daysBefore,
                scheduledFor,
                status,
                sentAt,
                lastAttemptAt,
                attempts,
                lastError
        );
    }

}
