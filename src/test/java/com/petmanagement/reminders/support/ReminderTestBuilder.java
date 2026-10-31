package com.petmanagement.reminders.support;

import com.petmanagement.reminders.application.port.in.CreateReminderUseCase;
import com.petmanagement.reminders.domain.model.aggregate.Reminder;
import com.petmanagement.reminders.domain.model.enums.SourceType;
import com.petmanagement.reminders.domain.model.valueobject.*;

import java.time.LocalDate;
import java.util.UUID;

public final class ReminderTestBuilder {

    private SourceId sourceId;
    private SourceType sourceType;
    private PetId petId;
    private ReminderTitle title;
    private ReminderDueDate dueDate;

    private ReminderTestBuilder() {
        this.sourceId = TestReminderMother.EXISTING_VACCINE_ID;
        this.sourceType = TestReminderMother.VACCINE_SOURCE_TYPE;
        this.petId = TestReminderMother.EXISTING_PET_ID;
        this.title = TestReminderMother.VACCINE_TITLE;
        this.dueDate = TestReminderMother.DUE_DATE_THREE_MONTHS;
    }

    public static ReminderTestBuilder aReminder() {
        return new ReminderTestBuilder();
    }

    public ReminderTestBuilder vaccine() {
        this.sourceId = TestReminderMother.EXISTING_VACCINE_ID;
        this.sourceType = SourceType.VACCINE;
        this.title = TestReminderMother.VACCINE_TITLE;
        return this;
    }

    public ReminderTestBuilder deworming() {
        this.sourceId = TestReminderMother.EXISTING_DEWORMING_ID;
        this.sourceType = SourceType.DEWORMING;
        this.title = TestReminderMother.DEWORMING_TITLE;
        return this;
    }

    public ReminderTestBuilder withSourceId(SourceId sourceId) {
        this.sourceId = sourceId;
        return this;
    }

    public ReminderTestBuilder withSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
        return this;
    }

    public ReminderTestBuilder withPetId(PetId petId) {
        this.petId = petId;
        return this;
    }

    public ReminderTestBuilder withTitle(ReminderTitle title) {
        this.title = title;
        return this;
    }

    public ReminderTestBuilder withDueDate(ReminderDueDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public Reminder build() {
        return Reminder.create(sourceId, sourceType, petId, title, dueDate);
    }

    public static class CreateReminderCommandBuilder {

        private UUID sourceId;
        private SourceType sourceType;
        private UUID petId;
        private String title;
        private LocalDate dueDate;

        public static CreateReminderCommandBuilder aCreateReminderCommand() {
            return new CreateReminderCommandBuilder();
        }

        private CreateReminderCommandBuilder() {
            this.sourceId = TestReminderMother.EXISTING_VACCINE_ID.value();
            this.sourceType = TestReminderMother.VACCINE_SOURCE_TYPE;
            this.petId = TestReminderMother.EXISTING_PET_ID.value();
            this.title = TestReminderMother.VACCINE_TITLE.value();
            this.dueDate = TestReminderMother.DUE_DATE_THREE_MONTHS.value();
        }

        public CreateReminderCommandBuilder vaccine() {
            this.sourceId = TestReminderMother.EXISTING_VACCINE_ID.value();
            this.sourceType = SourceType.VACCINE;
            this.title = TestReminderMother.VACCINE_TITLE.value();
            return this;
        }

        public CreateReminderCommandBuilder deworming() {
            this.sourceId = TestReminderMother.EXISTING_DEWORMING_ID.value();
            this.sourceType = SourceType.DEWORMING;
            this.title = TestReminderMother.DEWORMING_TITLE.value();
            return this;
        }

        public CreateReminderCommandBuilder withSourceId(UUID sourceId) {
            this.sourceId = sourceId;
            return this;
        }

        public CreateReminderCommandBuilder withSourceId(SourceId sourceId) {
            this.sourceId = sourceId.value();
            return this;
        }

        public CreateReminderCommandBuilder withSourceType(SourceType sourceType) {
            this.sourceType = sourceType;
            return this;
        }

        public CreateReminderCommandBuilder withPetId(UUID petId) {
            this.petId = petId;
            return this;
        }

        public CreateReminderCommandBuilder withPetId(PetId petId) {
            this.petId = petId.value();
            return this;
        }

        public CreateReminderCommandBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public CreateReminderCommandBuilder withTitle(ReminderTitle title) {
            this.title = title.value();
            return this;
        }

        public CreateReminderCommandBuilder withDueDate(LocalDate dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public CreateReminderCommandBuilder withDueDate(ReminderDueDate dueDate) {
            this.dueDate = dueDate.value();
            return this;
        }

        public CreateReminderUseCase.CreateReminderCommand build() {
            return new CreateReminderUseCase.CreateReminderCommand(
                    sourceId,
                    sourceType,
                    petId,
                    title,
                    dueDate
            );
        }
    }

}
