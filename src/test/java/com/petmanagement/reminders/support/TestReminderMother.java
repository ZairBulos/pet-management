package com.petmanagement.reminders.support;

import com.petmanagement.reminders.domain.model.enums.SourceType;
import com.petmanagement.reminders.domain.model.valueobject.*;

import java.time.LocalDate;

public final class TestReminderMother {

    // === Reminder IDs ===

    public static final ReminderId DEFAULT_REMINDER_ID =
            ReminderId.of("c00fd4ad-1777-4544-833f-84d1e81c249d");
    public static final ReminderId ANOTHER_REMINDER_ID =
            ReminderId.of("3d29dd0a-b5c0-407a-b34b-0942c93ff5ff");
    public static final ReminderId NON_EXISTENT_REMINDER_ID =
            ReminderId.of("1338d533-db44-4014-bee8-b4c99c6f11aa");

    // === Source IDs ===

    public static final SourceId EXISTING_SOURCE_ID =
            SourceId.of("023c108b-611b-41ef-9798-c25e5709376e");
    public static final SourceId ANOTHER_SOURCE_ID =
            SourceId.of("73f4988a-96fb-4e7a-bd92-a4a8319858d8");
    public static final SourceId NON_EXISTENT_SOURCE_ID =
            SourceId.of("252308ea-7738-45c5-93ba-898fbe066457");

    public static final SourceId EXISTING_DEWORMING_ID =
            SourceId.of("c9566006-7b81-4d9e-85c3-13e5e4813ec9");
    public static final SourceId ANOTHER_DEWORMING_ID =
            SourceId.of("1252e714-9f6c-488f-be49-8d64b4dfcbeb");
    public static final SourceId NON_EXISTENT_DEWORMING_ID =
            SourceId.of("5b7bc6a3-9b7e-429b-a075-9ebc6f409af3");

    public static final SourceId EXISTING_VACCINE_ID =
            SourceId.of("1354cd00-c3e1-4cca-a289-d39113f3fcf8");
    public static final SourceId ANOTHER_VACCINE_ID =
            SourceId.of("f1c060bf-369e-433b-938c-5fac5c1f44fc");
    public static final SourceId NON_EXISTENT_VACCINE_ID =
            SourceId.of("d6c47c3c-cab9-49d2-aeef-b3a58a85ab8d");

    // === Source Types ===

    public static final SourceType VACCINE_SOURCE_TYPE = SourceType.VACCINE;
    public static final SourceType DEWORMING_SOURCE_TYPE = SourceType.DEWORMING;

    // === Pet IDs ===

    public static final PetId EXISTING_PET_ID =
            PetId.of("f5a4adf1-160a-47eb-aa30-91af49fecb7a");
    public static final PetId NON_EXISTENT_PET_ID =
            PetId.of("180ae457-1a2a-42b5-80a4-5002423d3ff4");
    public static final PetId ANOTHER_PET_ID =
            PetId.of("20748e54-9b9d-46a3-ab1c-6822559141de");

    // === Reminders titles ===

    public static final ReminderTitle VACCINE_TITLE =
            new ReminderTitle("Vaccine");
    public static final ReminderTitle DEWORMING_TITLE =
            new ReminderTitle("Deworming");

    // === Reminder due dates ===

    private static final LocalDate TODAY =
            LocalDate.now();

    public static final ReminderDueDate DUE_DATE_THREE_MONTHS =
            new ReminderDueDate(TODAY.plusMonths(3));
    public static final ReminderDueDate DUE_DATE_SIX_MONTHS =
            new ReminderDueDate(TODAY.plusMonths(6));
    public static final ReminderDueDate DUE_DATE_ONE_YEAR =
            new ReminderDueDate(TODAY.plusYears(1));

    public static final ReminderDueDate NEAR_DUE_DATE =
            new ReminderDueDate(TODAY.plusDays(2));
    public static final ReminderDueDate PAST_DUE_DATE =
            new ReminderDueDate(TODAY.minusDays(1));

    private TestReminderMother() {
    }

}
