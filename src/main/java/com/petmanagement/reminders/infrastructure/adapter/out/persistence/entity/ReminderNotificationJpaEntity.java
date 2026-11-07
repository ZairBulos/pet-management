package com.petmanagement.reminders.infrastructure.adapter.out.persistence.entity;

import com.petmanagement.reminders.domain.model.enums.NotificationStatus;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "reminder_notifications", schema = "reminders_schema")
public class ReminderNotificationJpaEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reminder_id", nullable = false)
    private ReminderJpaEntity reminder;

    @Column(name = "days_before", nullable = false)
    private short daysBefore;

    @Column(name = "scheduled_for", nullable = false)
    private LocalDate scheduledFor;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private NotificationStatus status;

    @Column(name = "sent_at")
    private Instant sentAt;

    @Column(name = "last_attempt_at")
    private Instant lastAttemptAt;

    @Column(name = "attempts", nullable = false)
    private int attempts;

    @Column(name = "last_error", columnDefinition = "TEXT")
    private String lastError;

    // === Constructors ===

    protected ReminderNotificationJpaEntity() {
        // required by JPA
    }

    public ReminderNotificationJpaEntity(
            UUID id,
            short daysBefore,
            LocalDate scheduledFor,
            NotificationStatus status,
            Instant sentAt,
            Instant lastAttemptAt,
            int attempts,
            String lastError
    ) {
        this.id = id;
        this.daysBefore = daysBefore;
        this.scheduledFor = scheduledFor;
        this.status = status;
        this.sentAt = sentAt;
        this.lastAttemptAt = lastAttemptAt;
        this.attempts = attempts;
        this.lastError = lastError;
    }

    // === Getters & Setters ===

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ReminderJpaEntity getReminder() {
        return reminder;
    }

    public void setReminder(ReminderJpaEntity reminder) {
        this.reminder = reminder;
    }

    public short getDaysBefore() {
        return daysBefore;
    }

    public void setDaysBefore(short daysBefore) {
        this.daysBefore = daysBefore;
    }

    public LocalDate getScheduledFor() {
        return scheduledFor;
    }

    public void setScheduledFor(LocalDate scheduledFor) {
        this.scheduledFor = scheduledFor;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public Instant getSentAt() {
        return sentAt;
    }

    public void setSentAt(Instant sentAt) {
        this.sentAt = sentAt;
    }

    public Instant getLastAttemptAt() {
        return lastAttemptAt;
    }

    public void setLastAttemptAt(Instant lastAttemptAt) {
        this.lastAttemptAt = lastAttemptAt;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public String getLastError() {
        return lastError;
    }

    public void setLastError(String lastError) {
        this.lastError = lastError;
    }

    // === Object Methods ===

    @Override
    public String toString() {
        return "ReminderNotificationJpaEntity{" +
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
