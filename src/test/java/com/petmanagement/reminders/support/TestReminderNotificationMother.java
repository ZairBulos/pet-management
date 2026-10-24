package com.petmanagement.reminders.support;

import com.petmanagement.reminders.domain.model.valueobject.NotificationId;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;

public final class TestReminderNotificationMother {

    // === Notification IDs ===

    public static final NotificationId DEFAULT_NOTIFICATION_ID =
            NotificationId.of("6f6e2947-a94b-4439-880f-194efb2037ac");
    public static final NotificationId ANOTHER_NOTIFICATION_ID =
            NotificationId.of("ce9b4540-69ad-459e-935c-10c4695fd5b0");
    public static final NotificationId NON_EXISTENT_NOTIFICATION_ID =
            NotificationId.of("8a1560e0-f88f-4856-8dc3-23a192f74cb1");

    // === Scheduled dates ===

    public static final LocalDate DEFAULT_SCHEDULED_FOR =
            TestReminderMother.DUE_DATE_THREE_MONTHS.value().minusDays(3);
    public static final LocalDate PAST_SCHEDULED_FOR =
            LocalDate.now().minusDays(1);

    // === Delivery ===

    public static final int DEFAULT_MAX_ATTEMPTS = 3;

    public static final String DEFAULT_ERROR = "Sender unavailable";

    // === Timestamps ===

    public static final Instant DEFAULT_ATTEMPT_AT = Instant.now().minus(Duration.ofHours(1));

    private TestReminderNotificationMother() {
    }

}
