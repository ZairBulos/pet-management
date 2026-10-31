package com.petmanagement.reminders.domain.model.entity;

import com.petmanagement.reminders.domain.exception.InvalidNotificationStateException;
import com.petmanagement.reminders.domain.model.enums.NotificationStatus;
import com.petmanagement.reminders.domain.model.valueobject.DaysBefore;
import com.petmanagement.reminders.domain.model.valueobject.NotificationId;
import com.petmanagement.reminders.domain.model.valueobject.ReminderDueDate;
import org.jmolecules.ddd.annotation.Entity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public final class ReminderNotification {

    private final NotificationId id;
    private final DaysBefore daysBefore;
    private LocalDate scheduledFor;
    private NotificationStatus status;
    private Instant sentAt;
    private Instant lastAttemptAt;
    private int attempts;
    private String lastError;

    private ReminderNotification(
            NotificationId id,
            DaysBefore daysBefore,
            LocalDate scheduledFor,
            NotificationStatus status,
            Instant sentAt,
            Instant lastAttemptAt,
            int attempts,
            String lastError
    ) {
        this.id = Objects.requireNonNull(id, "Notification id cannot be null");
        this.daysBefore = Objects.requireNonNull(daysBefore, "Days before cannot be null");
        this.scheduledFor = Objects.requireNonNull(scheduledFor, "Scheduled for cannot be null");
        this.status = Objects.requireNonNull(status, "Notification status cannot be null");
        this.sentAt = sentAt;
        this.lastAttemptAt = lastAttemptAt;
        this.attempts = attempts;
        this.lastError = lastError;
    }

    // === Factory ===

    public static ReminderNotification create(
            DaysBefore daysBefore,
            ReminderDueDate dueDate
    ) {
        Objects.requireNonNull(dueDate, "Reminder due date cannot be null");

        return new ReminderNotification(
                NotificationId.generate(),
                daysBefore,
                calculateScheduledFor(daysBefore, dueDate),
                NotificationStatus.PENDING,
                null,
                null,
                0,
                null
        );
    }

    public static ReminderNotification reconstitute(
            NotificationId id,
            DaysBefore daysBefore,
            LocalDate scheduledFor,
            NotificationStatus status,
            Instant sentAt,
            Instant lastAttemptAt,
            int attempts,
            String lastError
    ) {
        return new ReminderNotification(id, daysBefore, scheduledFor, status, sentAt, lastAttemptAt, attempts, lastError);
    }

    // === Business Operations ===

    public void reschedule(ReminderDueDate newDueDate) {
        Objects.requireNonNull(newDueDate, "Reminder due date cannot be null");

        if (isSent() || isFailed())
            return;

        this.scheduledFor = calculateScheduledFor(daysBefore, newDueDate);
        this.status = NotificationStatus.PENDING;
        this.attempts = 0;
        this.lastAttemptAt = null;
        this.lastError = null;
    }

    public void markSent() {
        requirePending("mark as sent");

        var now = Instant.now();

        this.status = NotificationStatus.SENT;
        this.sentAt = now;
        this.lastAttemptAt = now;
        this.attempts++;
        this.lastError = null;
    }

    public void markFailed(String error, int maxAttempts) {
        requirePending("mark as failed");

        this.attempts++;
        this.lastAttemptAt = Instant.now();
        this.lastError = error;

        if (attempts >= maxAttempts)
            this.status = NotificationStatus.FAILED;
    }

    public void markSkipped() {
        requirePending("skip");

        this.status = NotificationStatus.SKIPPED;
    }

    public boolean isOverdue(LocalDate today) {
        return scheduledFor.isBefore(today);
    }

    public boolean isPending() {
        return status == NotificationStatus.PENDING;
    }

    public boolean isSent() {
        return status == NotificationStatus.SENT;
    }

    public boolean isFailed() {
        return status == NotificationStatus.FAILED;
    }

    public boolean isSkipped() {
        return status == NotificationStatus.SKIPPED;
    }

    private void requirePending(String operation) {
        if (!isPending())
            throw new InvalidNotificationStateException(operation, status);
    }

    private static LocalDate calculateScheduledFor(
            DaysBefore daysBefore,
            ReminderDueDate dueDate
    ) {
        return dueDate.value().minusDays(daysBefore.value());
    }

    // === Getters ===

    public NotificationId getId() {
        return id;
    }

    public DaysBefore getDaysBefore() {
        return daysBefore;
    }

    public LocalDate getScheduledFor() {
        return scheduledFor;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public Instant getSentAt() {
        return sentAt;
    }

    public Instant getLastAttemptAt() {
        return lastAttemptAt;
    }

    public int getAttempts() {
        return attempts;
    }

    public String getLastError() {
        return lastError;
    }

    // === Object Methods ===

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReminderNotification that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "ReminderNotification{" +
                "id=" + id +
                ", daysBefore=" + daysBefore +
                ", scheduledFor=" + scheduledFor +
                ", status=" + status +
                ", sentAt=" + sentAt +
                ", lastAttemptAt=" + lastAttemptAt +
                ", attempts=" + attempts +
                ", lastError='" + lastError + '\'' +
                '}';
    }

}
