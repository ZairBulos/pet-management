package com.petmanagement.reminders.support;

import com.petmanagement.reminders.domain.model.enums.SourceType;
import com.petmanagement.reminders.infrastructure.adapter.out.persistence.entity.ReminderJpaEntity;
import com.petmanagement.reminders.infrastructure.adapter.out.persistence.entity.ReminderNotificationJpaEntity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class ReminderJpaEntityTestBuilder {

    private UUID id;
    private SourceType sourceType;
    private UUID sourceId;
    private UUID petId;
    private String title;
    private LocalDate dueDate;
    private Instant createdAt;
    private Instant updatedAt;
    private List<ReminderNotificationJpaEntity> notifications;

    private ReminderJpaEntityTestBuilder() {
        this.id = TestReminderMother.DEFAULT_REMINDER_ID.value();
        this.sourceType = TestReminderMother.VACCINE_SOURCE_TYPE;
        this.sourceId = TestReminderMother.EXISTING_VACCINE_ID.value();
        this.petId = TestReminderMother.EXISTING_PET_ID.value();
        this.title = TestReminderMother.VACCINE_TITLE.value();
        this.dueDate = TestReminderMother.DUE_DATE_THREE_MONTHS.value();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.notifications = new ArrayList<>();
    }

    public static ReminderJpaEntityTestBuilder aReminderJpaEntity() {
        return new ReminderJpaEntityTestBuilder();
    }

    public ReminderJpaEntityTestBuilder vaccine() {
        this.sourceType = SourceType.VACCINE;
        this.sourceId = TestReminderMother.EXISTING_VACCINE_ID.value();
        this.title = TestReminderMother.VACCINE_TITLE.value();
        return this;
    }

    public ReminderJpaEntityTestBuilder deworming() {
        this.sourceType = SourceType.DEWORMING;
        this.sourceId = TestReminderMother.EXISTING_DEWORMING_ID.value();
        this.title = TestReminderMother.DEWORMING_TITLE.value();
        return this;
    }

    public ReminderJpaEntityTestBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public ReminderJpaEntityTestBuilder withSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
        return this;
    }

    public ReminderJpaEntityTestBuilder withSourceId(UUID sourceId) {
        this.sourceId = sourceId;
        return this;
    }

    public ReminderJpaEntityTestBuilder withPetId(UUID petId) {
        this.petId = petId;
        return this;
    }

    public ReminderJpaEntityTestBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public ReminderJpaEntityTestBuilder withDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public ReminderJpaEntityTestBuilder withCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public ReminderJpaEntityTestBuilder withUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public ReminderJpaEntityTestBuilder withNotifications(ReminderNotificationJpaEntity... notifications) {
        this.notifications = new ArrayList<>(List.of(notifications));
        return this;
    }

    public ReminderJpaEntity build() {
        var entity = new ReminderJpaEntity(
                id,
                sourceType,
                sourceId,
                petId,
                title,
                dueDate,
                createdAt,
                updatedAt
        );

        notifications.forEach(entity::addNotification);

        return entity;
    }

}
