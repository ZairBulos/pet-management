package com.petmanagement.reminders.infrastructure.adapter.out.persistence.entity;

import com.petmanagement.reminders.domain.model.enums.SourceType;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "reminders", schema = "reminders_schema")
public class ReminderJpaEntity {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", nullable = false)
    private SourceType sourceType;

    @Column(name = "source_id", nullable = false, length = 20)
    private UUID sourceId;

    @Column(name = "pet_id", nullable = false)
    private UUID petId;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "reminder", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("daysBefore DESC")
    private List<ReminderNotificationJpaEntity> notifications = new ArrayList<>();

    // === Constructors ===

    protected ReminderJpaEntity() {
        // required by JPA
    }

    public ReminderJpaEntity(
            UUID id,
            SourceType sourceType,
            UUID sourceId,
            UUID petId,
            String title,
            LocalDate dueDate,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.sourceType = sourceType;
        this.sourceId = sourceId;
        this.petId = petId;
        this.title = title;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // === Notifications ===

    public void addNotification(ReminderNotificationJpaEntity notification) {
        notification.setReminder(this);
        this.notifications.add(notification);
    }

    public Optional<ReminderNotificationJpaEntity> findNotificationById(UUID notificationId) {
        return notifications.stream()
                .filter(notification -> notification.getId().equals(notificationId))
                .findFirst();
    }

    // === Getters & Setters ===

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
    }

    public UUID getSourceId() {
        return sourceId;
    }

    public void setSourceId(UUID sourceId) {
        this.sourceId = sourceId;
    }

    public UUID getPetId() {
        return petId;
    }

    public void setPetId(UUID petId) {
        this.petId = petId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<ReminderNotificationJpaEntity> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<ReminderNotificationJpaEntity> notifications) {
        this.notifications = notifications;
    }

    // === Object Methods ===

    @Override
    public String toString() {
        return "ReminderJpaEntity{" +
                "id=" + id +
                ", sourceType=" + sourceType +
                ", sourceId=" + sourceId +
                ", petId=" + petId +
                ", title='" + title + '\'' +
                ", dueDate=" + dueDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

}
