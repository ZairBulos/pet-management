package com.petmanagement.reminders.support;

import com.petmanagement.reminders.domain.model.aggregate.Reminder;
import com.petmanagement.reminders.domain.model.enums.SourceType;
import com.petmanagement.reminders.domain.model.valueobject.*;

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

}
