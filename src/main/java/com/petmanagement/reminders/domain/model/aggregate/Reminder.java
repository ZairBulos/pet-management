package com.petmanagement.reminders.domain.model.aggregate;

import com.petmanagement.reminders.domain.exception.NotificationNotFoundException;
import com.petmanagement.reminders.domain.model.enums.SourceType;
import com.petmanagement.reminders.domain.model.valueobject.*;
import org.jmolecules.ddd.annotation.AggregateRoot;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@AggregateRoot
public final class Reminder {

    private final ReminderId id;
    private final SourceId sourceId;
    private final SourceType sourceType;
    private final PetId petId;
    private final ReminderTitle title;
    private ReminderDueDate dueDate;
    private final List<ReminderNotification> notifications;
    private final Instant createdAt;
    private Instant updatedAt;

    private Reminder(
            ReminderId id,
            SourceId sourceId,
            SourceType sourceType,
            PetId petId,
            ReminderTitle title,
            ReminderDueDate dueDate,
            List<ReminderNotification> notifications,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = Objects.requireNonNull(id, "Reminder id cannot be null");
        this.sourceId = Objects.requireNonNull(sourceId, "Source id cannot be null");
        this.sourceType = Objects.requireNonNull(sourceType, "Source type cannot be null");
        this.petId = Objects.requireNonNull(petId, "Pet id cannot be null");
        this.title = Objects.requireNonNull(title, "Title cannot be null");
        this.dueDate = Objects.requireNonNull(dueDate, "Due date cannot be null");
        this.notifications = List.copyOf(Objects.requireNonNull(notifications, "Notifications cannot be null"));
        this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "Updated at cannot be null");
    }

    // === Factory ===

    public static Reminder create(
            SourceId sourceId,
            SourceType sourceType,
            PetId petId,
            ReminderTitle title,
            ReminderDueDate dueDate
    ) {
        var now = Instant.now();

        var notifications = DaysBefore.defaults().stream()
                .map(daysBefore -> ReminderNotification.create(daysBefore, dueDate))
                .toList();

        var reminder = new Reminder(
                ReminderId.generate(),
                sourceId,
                sourceType,
                petId,
                title,
                dueDate,
                notifications,
                now,
                now
        );

        reminder.skipOverdueNotifications(LocalDate.now());

        return reminder;
    }

    public static Reminder reconstitute(
            ReminderId id,
            SourceId sourceId,
            SourceType sourceType,
            PetId petId,
            ReminderTitle title,
            ReminderDueDate dueDate,
            List<ReminderNotification> notifications,
            Instant createdAt,
            Instant updatedAt
    ) {
        return new Reminder(id, sourceId, sourceType, petId, title, dueDate, notifications, createdAt, updatedAt);
    }

    // === Business Operations ===

    public void reschedule(ReminderDueDate newDueDate) {
        Objects.requireNonNull(newDueDate, "Reminder due date cannot be null");

        if (this.dueDate.equals(newDueDate))
            return;

        this.dueDate = newDueDate;
        this.notifications.forEach(notification -> notification.reschedule(newDueDate));
        skipOverdueNotifications(LocalDate.now());
        touch();
    }

    public void markNotificationSent(NotificationId notificationId) {
        findNotification(notificationId).markSent();
    }

    public void markNotificationFailed(NotificationId notificationId, String error, int maxAttempts) {
        findNotification(notificationId).markFailed(error, maxAttempts);
    }

    public void markNotificationSkipped(NotificationId notificationId) {
        findNotification(notificationId).markSkipped();
    }

    private void skipOverdueNotifications(LocalDate today) {
        notifications.stream()
                .filter(ReminderNotification::isPending)
                .filter(notification -> notification.isOverdue(today))
                .forEach(ReminderNotification::markSkipped);
    }

    private ReminderNotification findNotification(NotificationId notificationId) {
        Objects.requireNonNull(notificationId, "Notification id cannot be null");

        return notifications.stream()
                .filter(notification -> notification.getId().equals(notificationId))
                .findFirst()
                .orElseThrow(NotificationNotFoundException::new);
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }

    // === Getters ===

    public ReminderId getId() {
        return id;
    }

    public SourceId getSourceId() {
        return sourceId;
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    public PetId getPetId() {
        return petId;
    }

    public ReminderTitle getTitle() {
        return title;
    }

    public ReminderDueDate getDueDate() {
        return dueDate;
    }

    public List<ReminderNotification> getNotifications() {
        return notifications;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    // === Object Methods ===

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reminder reminder)) return false;
        return id.equals(reminder.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Reminder{" +
                "id=" + id +
                ", sourceId=" + sourceId +
                ", sourceType=" + sourceType +
                ", petId=" + petId +
                ", title=" + title +
                ", dueDate=" + dueDate +
                ", notifications=" + notifications +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

}
