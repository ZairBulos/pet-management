package com.petmanagement.reminders.support;

import com.petmanagement.reminders.domain.model.entity.ReminderNotification;
import com.petmanagement.reminders.domain.model.enums.NotificationStatus;
import com.petmanagement.reminders.domain.model.valueobject.DaysBefore;
import com.petmanagement.reminders.domain.model.valueobject.NotificationId;

import java.time.Instant;
import java.time.LocalDate;

public final class ReminderNotificationTestBuilder {

    private NotificationId id;
    private DaysBefore daysBefore;
    private LocalDate scheduledFor;
    private NotificationStatus status;
    private Instant sentAt;
    private Instant lastAttemptAt;
    private int attempts;
    private String lastError;

    private ReminderNotificationTestBuilder() {
        this.id = TestReminderNotificationMother.DEFAULT_NOTIFICATION_ID;
        this.daysBefore = DaysBefore.THREE_DAYS;
        this.scheduledFor = TestReminderNotificationMother.DEFAULT_SCHEDULED_FOR;
        this.status = NotificationStatus.PENDING;
        this.sentAt = null;
        this.lastAttemptAt = null;
        this.attempts = 0;
        this.lastError = null;
    }

    public static ReminderNotificationTestBuilder aReminderNotification() {
        return new ReminderNotificationTestBuilder();
    }

    public ReminderNotificationTestBuilder pending() {
        this.status = NotificationStatus.PENDING;
        this.sentAt = null;
        this.lastAttemptAt = null;
        this.attempts = 0;
        this.lastError = null;
        return this;
    }

    public ReminderNotificationTestBuilder sent() {
        this.status = NotificationStatus.SENT;
        this.sentAt = TestReminderNotificationMother.DEFAULT_ATTEMPT_AT;
        this.lastAttemptAt = TestReminderNotificationMother.DEFAULT_ATTEMPT_AT;
        this.attempts = 1;
        this.lastError = null;
        return this;
    }

    public ReminderNotificationTestBuilder failed() {
        this.status = NotificationStatus.FAILED;
        this.sentAt = null;
        this.lastAttemptAt = TestReminderNotificationMother.DEFAULT_ATTEMPT_AT;
        this.attempts = TestReminderNotificationMother.DEFAULT_MAX_ATTEMPTS;
        this.lastError = TestReminderNotificationMother.DEFAULT_ERROR;
        return this;
    }

    public ReminderNotificationTestBuilder skipped() {
        this.status = NotificationStatus.SKIPPED;
        this.sentAt = null;
        return this;
    }

    public ReminderNotificationTestBuilder withId(NotificationId id) {
        this.id = id;
        return this;
    }

    public ReminderNotificationTestBuilder withId(String id) {
        this.id = NotificationId.of(id);
        return this;
    }

    public ReminderNotificationTestBuilder withDaysBefore(DaysBefore daysBefore) {
        this.daysBefore = daysBefore;
        return this;
    }

    public ReminderNotificationTestBuilder withScheduledFor(LocalDate scheduledFor) {
        this.scheduledFor = scheduledFor;
        return this;
    }

    public ReminderNotificationTestBuilder withStatus(NotificationStatus status) {
        this.status = status;
        return this;
    }

    public ReminderNotificationTestBuilder withSentAt(Instant sentAt) {
        this.sentAt = sentAt;
        return this;
    }

    public ReminderNotificationTestBuilder withLastAttemptAt(Instant lastAttemptAt) {
        this.lastAttemptAt = lastAttemptAt;
        return this;
    }

    public ReminderNotificationTestBuilder withAttempts(int attempts) {
        this.attempts = attempts;
        return this;
    }

    public ReminderNotificationTestBuilder withLastError(String lastError) {
        this.lastError = lastError;
        return this;
    }

    public ReminderNotification build() {
        return ReminderNotification.reconstitute(
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
