package com.petmanagement.reminders.domain.model.aggregate;

import com.petmanagement.reminders.domain.exception.InvalidNotificationStateException;
import com.petmanagement.reminders.domain.exception.NotificationNotFoundException;
import com.petmanagement.reminders.domain.model.entity.ReminderNotification;
import com.petmanagement.reminders.domain.model.enums.NotificationStatus;
import com.petmanagement.reminders.domain.model.valueobject.DaysBefore;
import com.petmanagement.reminders.domain.model.valueobject.ReminderId;
import com.petmanagement.reminders.support.TestReminderMother;
import com.petmanagement.reminders.support.TestReminderNotificationMother;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.petmanagement.reminders.support.ReminderNotificationTestBuilder.aReminderNotification;
import static com.petmanagement.reminders.support.ReminderTestBuilder.aReminder;
import static org.junit.jupiter.api.Assertions.*;

class ReminderTest {

    @Nested
    class Creation {

        @Test
        void shouldCreateReminder() {
            var reminder = aReminder().build();

            assertNotNull(reminder.getId());
            assertEquals(TestReminderMother.EXISTING_VACCINE_ID, reminder.getSourceId());
            assertEquals(TestReminderMother.VACCINE_SOURCE_TYPE, reminder.getSourceType());
            assertEquals(TestReminderMother.EXISTING_PET_ID, reminder.getPetId());
            assertEquals(TestReminderMother.VACCINE_TITLE, reminder.getTitle());
            assertEquals(TestReminderMother.DUE_DATE_THREE_MONTHS, reminder.getDueDate());
            assertNotNull(reminder.getCreatedAt());
            assertNotNull(reminder.getUpdatedAt());
        }

        @Test
        void shouldCreateReminderWithSameCreatedAtAndUpdatedAt() {
            var reminder = aReminder().build();

            assertEquals(reminder.getCreatedAt(), reminder.getUpdatedAt());
        }

        @Test
        void shouldGenerateUniqueIdForEachReminder() {
            var reminder1 = aReminder().build();
            var reminder2 = aReminder().build();

            assertNotEquals(reminder1.getId(), reminder2.getId());
        }

        @Test
        void shouldCreateOneNotificationForEachDefaultDaysBefore() {
            var reminder = aReminder().build();

            var daysBefore = reminder.getNotifications().stream()
                    .map(ReminderNotification::getDaysBefore)
                    .toList();

            assertEquals(DaysBefore.defaults().size(), daysBefore.size());
            assertTrue(daysBefore.containsAll(DaysBefore.defaults()));
        }

        @Test
        void shouldGenerateUniqueIdForEachNotification() {
            var reminder = aReminder().build();

            var distinctIds = reminder.getNotifications().stream()
                    .map(ReminderNotification::getId)
                    .distinct()
                    .count();

            assertEquals(reminder.getNotifications().size(), distinctIds);
        }

        @Test
        void shouldScheduleNotificationsRelativeToDueDate() {
            var dueDate = TestReminderMother.DUE_DATE_THREE_MONTHS;

            var reminder = aReminder().withDueDate(dueDate).build();

            assertEquals(
                    dueDate.value().minusDays(3),
                    notificationOf(reminder, DaysBefore.THREE_DAYS).getScheduledFor()
            );
            assertEquals(
                    dueDate.value(),
                    notificationOf(reminder, DaysBefore.SAME_DAY).getScheduledFor()
            );
        }

        @Test
        void shouldCreateNotificationsAsPending() {
            var reminder = aReminder().build();

            assertTrue(reminder.getNotifications().stream().allMatch(ReminderNotification::isPending));
        }

        @Test
        void shouldSkipOverdueNotificationsWhenDueDateIsNear() {
            var reminder = aReminder().withDueDate(TestReminderMother.NEAR_DUE_DATE).build();

            assertTrue(notificationOf(reminder, DaysBefore.THREE_DAYS).isSkipped());
            assertTrue(notificationOf(reminder, DaysBefore.SAME_DAY).isPending());
        }

        @Test
        void shouldSkipAllNotificationsWhenDueDateHasPassed() {
            var reminder = aReminder().withDueDate(TestReminderMother.PAST_DUE_DATE).build();

            assertTrue(reminder.getNotifications().stream().allMatch(ReminderNotification::isSkipped));
        }

        @Test
        void shouldThrowWhenCreatingWithNullSourceId() {
            assertThrows(
                    NullPointerException.class,
                    () -> Reminder.create(
                            null,
                            TestReminderMother.VACCINE_SOURCE_TYPE,
                            TestReminderMother.EXISTING_PET_ID,
                            TestReminderMother.VACCINE_TITLE,
                            TestReminderMother.DUE_DATE_THREE_MONTHS
                    )
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullSourceType() {
            assertThrows(
                    NullPointerException.class,
                    () -> Reminder.create(
                            TestReminderMother.EXISTING_VACCINE_ID,
                            null,
                            TestReminderMother.EXISTING_PET_ID,
                            TestReminderMother.VACCINE_TITLE,
                            TestReminderMother.DUE_DATE_THREE_MONTHS
                    )
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullPetId() {
            assertThrows(
                    NullPointerException.class,
                    () -> Reminder.create(
                            TestReminderMother.EXISTING_VACCINE_ID,
                            TestReminderMother.VACCINE_SOURCE_TYPE,
                            null,
                            TestReminderMother.VACCINE_TITLE,
                            TestReminderMother.DUE_DATE_THREE_MONTHS
                    )
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullTitle() {
            assertThrows(
                    NullPointerException.class,
                    () -> Reminder.create(
                            TestReminderMother.EXISTING_VACCINE_ID,
                            TestReminderMother.VACCINE_SOURCE_TYPE,
                            TestReminderMother.EXISTING_PET_ID,
                            null,
                            TestReminderMother.DUE_DATE_THREE_MONTHS
                    )
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullDueDate() {
            assertThrows(
                    NullPointerException.class,
                    () -> Reminder.create(
                            TestReminderMother.EXISTING_VACCINE_ID,
                            TestReminderMother.VACCINE_SOURCE_TYPE,
                            TestReminderMother.EXISTING_PET_ID,
                            TestReminderMother.VACCINE_TITLE,
                            null
                    )
            );
        }

    }

    @Nested
    class Reconstitution {

        @Test
        void shouldReconstituteReminder() {
            var id = ReminderId.generate();
            var notifications = List.of(aReminderNotification().build());
            var createdAt = Instant.now().minus(Duration.ofDays(2));
            var updatedAt = Instant.now().minus(Duration.ofDays(1));

            var reminder = Reminder.reconstitute(
                    id,
                    TestReminderMother.EXISTING_DEWORMING_ID,
                    TestReminderMother.DEWORMING_SOURCE_TYPE,
                    TestReminderMother.EXISTING_PET_ID,
                    TestReminderMother.DEWORMING_TITLE,
                    TestReminderMother.DUE_DATE_SIX_MONTHS,
                    notifications,
                    createdAt,
                    updatedAt
            );

            assertEquals(id, reminder.getId());
            assertEquals(TestReminderMother.EXISTING_DEWORMING_ID, reminder.getSourceId());
            assertEquals(TestReminderMother.DEWORMING_SOURCE_TYPE, reminder.getSourceType());
            assertEquals(TestReminderMother.EXISTING_PET_ID, reminder.getPetId());
            assertEquals(TestReminderMother.DEWORMING_TITLE, reminder.getTitle());
            assertEquals(TestReminderMother.DUE_DATE_SIX_MONTHS, reminder.getDueDate());
            assertEquals(notifications, reminder.getNotifications());
            assertEquals(createdAt, reminder.getCreatedAt());
            assertEquals(updatedAt, reminder.getUpdatedAt());
        }

        @Test
        void shouldNotSkipOverdueNotificationsWhenReconstituting() {
            var overdue = aReminderNotification()
                    .withScheduledFor(TestReminderNotificationMother.PAST_SCHEDULED_FOR)
                    .build();

            var reminder = Reminder.reconstitute(
                    ReminderId.generate(),
                    TestReminderMother.EXISTING_VACCINE_ID,
                    TestReminderMother.VACCINE_SOURCE_TYPE,
                    TestReminderMother.EXISTING_PET_ID,
                    TestReminderMother.VACCINE_TITLE,
                    TestReminderMother.DUE_DATE_THREE_MONTHS,
                    List.of(overdue),
                    Instant.now(),
                    Instant.now()
            );

            assertTrue(reminder.getNotifications().getFirst().isPending());
        }

        @Test
        void shouldNotBeAffectedByLaterChangesToSourceList() {
            var notifications = new ArrayList<>(List.of(aReminderNotification().build()));

            var reminder = Reminder.reconstitute(
                    ReminderId.generate(),
                    TestReminderMother.EXISTING_VACCINE_ID,
                    TestReminderMother.VACCINE_SOURCE_TYPE,
                    TestReminderMother.EXISTING_PET_ID,
                    TestReminderMother.VACCINE_TITLE,
                    TestReminderMother.DUE_DATE_THREE_MONTHS,
                    notifications,
                    Instant.now(),
                    Instant.now()
            );
            notifications.clear();

            assertEquals(1, reminder.getNotifications().size());
        }

        @Test
        void shouldThrowWhenReconstituteWithNullId() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Reminder.reconstitute(
                            null,
                            TestReminderMother.EXISTING_VACCINE_ID,
                            TestReminderMother.VACCINE_SOURCE_TYPE,
                            TestReminderMother.EXISTING_PET_ID,
                            TestReminderMother.VACCINE_TITLE,
                            TestReminderMother.DUE_DATE_THREE_MONTHS,
                            List.of(),
                            now,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullSourceId() {
            var id = ReminderId.generate();
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Reminder.reconstitute(
                            id,
                            null,
                            TestReminderMother.VACCINE_SOURCE_TYPE,
                            TestReminderMother.EXISTING_PET_ID,
                            TestReminderMother.VACCINE_TITLE,
                            TestReminderMother.DUE_DATE_THREE_MONTHS,
                            List.of(),
                            now,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullSourceType() {
            var id = ReminderId.generate();
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Reminder.reconstitute(
                            id,
                            TestReminderMother.EXISTING_VACCINE_ID,
                            null,
                            TestReminderMother.EXISTING_PET_ID,
                            TestReminderMother.VACCINE_TITLE,
                            TestReminderMother.DUE_DATE_THREE_MONTHS,
                            List.of(),
                            now,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullPetId() {
            var id = ReminderId.generate();
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Reminder.reconstitute(
                            id,
                            TestReminderMother.EXISTING_VACCINE_ID,
                            TestReminderMother.VACCINE_SOURCE_TYPE,
                            null,
                            TestReminderMother.VACCINE_TITLE,
                            TestReminderMother.DUE_DATE_THREE_MONTHS,
                            List.of(),
                            now,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullTitle() {
            var id = ReminderId.generate();
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Reminder.reconstitute(
                            id,
                            TestReminderMother.EXISTING_VACCINE_ID,
                            TestReminderMother.VACCINE_SOURCE_TYPE,
                            TestReminderMother.EXISTING_PET_ID,
                            null,
                            TestReminderMother.DUE_DATE_THREE_MONTHS,
                            List.of(),
                            now,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullDueDate() {
            var id = ReminderId.generate();
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Reminder.reconstitute(
                            id,
                            TestReminderMother.EXISTING_VACCINE_ID,
                            TestReminderMother.VACCINE_SOURCE_TYPE,
                            TestReminderMother.EXISTING_PET_ID,
                            TestReminderMother.VACCINE_TITLE,
                            null,
                            List.of(),
                            now,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullNotifications() {
            var id = ReminderId.generate();
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Reminder.reconstitute(
                            id,
                            TestReminderMother.EXISTING_VACCINE_ID,
                            TestReminderMother.VACCINE_SOURCE_TYPE,
                            TestReminderMother.EXISTING_PET_ID,
                            TestReminderMother.VACCINE_TITLE,
                            TestReminderMother.DUE_DATE_THREE_MONTHS,
                            null,
                            now,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullCreatedAt() {
            var id = ReminderId.generate();
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Reminder.reconstitute(
                            id,
                            TestReminderMother.EXISTING_VACCINE_ID,
                            TestReminderMother.VACCINE_SOURCE_TYPE,
                            TestReminderMother.EXISTING_PET_ID,
                            TestReminderMother.VACCINE_TITLE,
                            TestReminderMother.DUE_DATE_THREE_MONTHS,
                            List.of(),
                            null,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullUpdatedAt() {
            var id = ReminderId.generate();
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Reminder.reconstitute(
                            id,
                            TestReminderMother.EXISTING_VACCINE_ID,
                            TestReminderMother.VACCINE_SOURCE_TYPE,
                            TestReminderMother.EXISTING_PET_ID,
                            TestReminderMother.VACCINE_TITLE,
                            TestReminderMother.DUE_DATE_THREE_MONTHS,
                            List.of(),
                            now,
                            null
                    )
            );
        }

    }

    @Nested
    class Reschedule {

        @Test
        void shouldUpdateDueDate() {
            var reminder = aReminder().build();

            reminder.reschedule(TestReminderMother.DUE_DATE_SIX_MONTHS);

            assertEquals(TestReminderMother.DUE_DATE_SIX_MONTHS, reminder.getDueDate());
        }

        @Test
        void shouldKeepIdentityAndOtherFieldsWhenRescheduling() {
            var reminder = aReminder().build();
            var id = reminder.getId();
            var createdAt = reminder.getCreatedAt();

            reminder.reschedule(TestReminderMother.DUE_DATE_SIX_MONTHS);

            assertEquals(id, reminder.getId());
            assertEquals(TestReminderMother.EXISTING_VACCINE_ID, reminder.getSourceId());
            assertEquals(TestReminderMother.VACCINE_SOURCE_TYPE, reminder.getSourceType());
            assertEquals(TestReminderMother.EXISTING_PET_ID, reminder.getPetId());
            assertEquals(TestReminderMother.VACCINE_TITLE, reminder.getTitle());
            assertEquals(createdAt, reminder.getCreatedAt());
        }

        @Test
        void shouldRecalculateScheduledDateOfPendingNotifications() {
            var reminder = aReminder().build();
            var newDueDate = TestReminderMother.DUE_DATE_SIX_MONTHS;

            reminder.reschedule(newDueDate);

            assertEquals(
                    newDueDate.value().minusDays(3),
                    notificationOf(reminder, DaysBefore.THREE_DAYS).getScheduledFor()
            );
            assertEquals(
                    newDueDate.value(),
                    notificationOf(reminder, DaysBefore.SAME_DAY).getScheduledFor()
            );
        }

        @Test
        void shouldNotRescheduleSentNotifications() {
            var reminder = aReminder().build();
            var sent = notificationOf(reminder, DaysBefore.THREE_DAYS);
            var scheduledFor = sent.getScheduledFor();
            reminder.markNotificationSent(sent.getId());

            reminder.reschedule(TestReminderMother.DUE_DATE_SIX_MONTHS);

            assertTrue(sent.isSent());
            assertEquals(scheduledFor, sent.getScheduledFor());
            assertEquals(
                    TestReminderMother.DUE_DATE_SIX_MONTHS.value(),
                    notificationOf(reminder, DaysBefore.SAME_DAY).getScheduledFor()
            );
        }

        @Test
        void shouldNotRescheduleFailedNotifications() {
            var reminder = aReminder().build();
            var failed = notificationOf(reminder, DaysBefore.THREE_DAYS);
            var scheduledFor = failed.getScheduledFor();
            reminder.markNotificationFailed(failed.getId(), TestReminderNotificationMother.DEFAULT_ERROR, 1);

            reminder.reschedule(TestReminderMother.DUE_DATE_SIX_MONTHS);

            assertTrue(failed.isFailed());
            assertEquals(scheduledFor, failed.getScheduledFor());
        }

        @Test
        void shouldSkipOverdueNotificationsWhenRescheduledToNearDate() {
            var reminder = aReminder().build();

            reminder.reschedule(TestReminderMother.NEAR_DUE_DATE);

            assertTrue(notificationOf(reminder, DaysBefore.THREE_DAYS).isSkipped());
            assertTrue(notificationOf(reminder, DaysBefore.SAME_DAY).isPending());
        }

        @Test
        void shouldReactivateSkippedNotificationsWhenRescheduledToFutureDate() {
            var reminder = aReminder().withDueDate(TestReminderMother.PAST_DUE_DATE).build();

            reminder.reschedule(TestReminderMother.DUE_DATE_THREE_MONTHS);

            assertTrue(reminder.getNotifications().stream().allMatch(ReminderNotification::isPending));
        }

        @Test
        void shouldThrowWhenReschedulingWithNullDueDate() {
            var reminder = aReminder().build();

            assertThrows(
                    NullPointerException.class,
                    () -> reminder.reschedule(null)
            );
        }

    }

    @Nested
    class MarkNotificationSent {

        @Test
        void shouldMarkNotificationAsSent() {
            var reminder = aReminder().build();
            var notification = notificationOf(reminder, DaysBefore.THREE_DAYS);

            reminder.markNotificationSent(notification.getId());

            assertEquals(NotificationStatus.SENT, notification.getStatus());
            assertNotNull(notification.getSentAt());
        }

        @Test
        void shouldNotAffectOtherNotifications() {
            var reminder = aReminder().build();

            reminder.markNotificationSent(notificationOf(reminder, DaysBefore.THREE_DAYS).getId());

            assertTrue(notificationOf(reminder, DaysBefore.SAME_DAY).isPending());
        }

        @Test
        void shouldThrowWhenNotificationDoesNotExist() {
            var reminder = aReminder().build();

            assertThrows(
                    NotificationNotFoundException.class,
                    () -> reminder.markNotificationSent(TestReminderNotificationMother.NON_EXISTENT_NOTIFICATION_ID)
            );
        }

        @Test
        void shouldThrowWhenNotificationIdIsNull() {
            var reminder = aReminder().build();

            assertThrows(
                    NullPointerException.class,
                    () -> reminder.markNotificationSent(null)
            );
        }

        @Test
        void shouldThrowWhenNotificationIsAlreadySent() {
            var reminder = aReminder().build();
            var id = notificationOf(reminder, DaysBefore.THREE_DAYS).getId();
            reminder.markNotificationSent(id);

            assertThrows(
                    InvalidNotificationStateException.class,
                    () -> reminder.markNotificationSent(id)
            );
        }

    }

    @Nested
    class MarkNotificationFailed {

        @Test
        void shouldRegisterFailedAttemptAndKeepPendingBelowMaxAttempts() {
            var reminder = aReminder().build();
            var notification = notificationOf(reminder, DaysBefore.THREE_DAYS);

            reminder.markNotificationFailed(
                    notification.getId(),
                    TestReminderNotificationMother.DEFAULT_ERROR,
                    TestReminderNotificationMother.DEFAULT_MAX_ATTEMPTS
            );

            assertTrue(notification.isPending());
            assertEquals(1, notification.getAttempts());
            assertEquals(TestReminderNotificationMother.DEFAULT_ERROR, notification.getLastError());
        }

        @Test
        void shouldMarkNotificationAsFailedWhenReachingMaxAttempts() {
            var reminder = aReminder().build();
            var notification = notificationOf(reminder, DaysBefore.THREE_DAYS);

            reminder.markNotificationFailed(notification.getId(), TestReminderNotificationMother.DEFAULT_ERROR, 1);

            assertTrue(notification.isFailed());
        }

        @Test
        void shouldNotAffectOtherNotifications() {
            var reminder = aReminder().build();
            var notification = notificationOf(reminder, DaysBefore.THREE_DAYS);

            reminder.markNotificationFailed(notification.getId(), TestReminderNotificationMother.DEFAULT_ERROR, 1);

            assertTrue(notificationOf(reminder, DaysBefore.SAME_DAY).isPending());
        }

        @Test
        void shouldThrowWhenNotificationDoesNotExist() {
            var reminder = aReminder().build();

            assertThrows(
                    NotificationNotFoundException.class,
                    () -> reminder.markNotificationFailed(
                            TestReminderNotificationMother.NON_EXISTENT_NOTIFICATION_ID,
                            TestReminderNotificationMother.DEFAULT_ERROR,
                            TestReminderNotificationMother.DEFAULT_MAX_ATTEMPTS
                    )
            );
        }

        @Test
        void shouldThrowWhenNotificationIdIsNull() {
            var reminder = aReminder().build();

            assertThrows(
                    NullPointerException.class,
                    () -> reminder.markNotificationFailed(
                            null,
                            TestReminderNotificationMother.DEFAULT_ERROR,
                            TestReminderNotificationMother.DEFAULT_MAX_ATTEMPTS
                    )
            );
        }

        @Test
        void shouldThrowWhenNotificationIsAlreadySent() {
            var reminder = aReminder().build();
            var id = notificationOf(reminder, DaysBefore.THREE_DAYS).getId();
            reminder.markNotificationSent(id);

            assertThrows(
                    InvalidNotificationStateException.class,
                    () -> reminder.markNotificationFailed(
                            id,
                            TestReminderNotificationMother.DEFAULT_ERROR,
                            TestReminderNotificationMother.DEFAULT_MAX_ATTEMPTS
                    )
            );
        }

    }

    @Nested
    class MarkNotificationSkipped {

        @Test
        void shouldMarkNotificationAsSkipped() {
            var reminder = aReminder().build();
            var notification = notificationOf(reminder, DaysBefore.THREE_DAYS);

            reminder.markNotificationSkipped(notification.getId());

            assertTrue(notification.isSkipped());
        }

        @Test
        void shouldNotAffectOtherNotifications() {
            var reminder = aReminder().build();

            reminder.markNotificationSkipped(notificationOf(reminder, DaysBefore.THREE_DAYS).getId());

            assertTrue(notificationOf(reminder, DaysBefore.SAME_DAY).isPending());
        }

        @Test
        void shouldThrowWhenNotificationDoesNotExist() {
            var reminder = aReminder().build();

            assertThrows(
                    NotificationNotFoundException.class,
                    () -> reminder.markNotificationSkipped(TestReminderNotificationMother.NON_EXISTENT_NOTIFICATION_ID)
            );
        }

        @Test
        void shouldThrowWhenNotificationIdIsNull() {
            var reminder = aReminder().build();

            assertThrows(
                    NullPointerException.class,
                    () -> reminder.markNotificationSkipped(null)
            );
        }

        @Test
        void shouldThrowWhenNotificationIsAlreadySkipped() {
            var reminder = aReminder().build();
            var id = notificationOf(reminder, DaysBefore.THREE_DAYS).getId();
            reminder.markNotificationSkipped(id);

            assertThrows(
                    InvalidNotificationStateException.class,
                    () -> reminder.markNotificationSkipped(id)
            );
        }

    }

    // === Helpers ===

    private static ReminderNotification notificationOf(Reminder reminder, DaysBefore daysBefore) {
        return reminder.getNotifications().stream()
                .filter(notification -> notification.getDaysBefore().equals(daysBefore))
                .findFirst()
                .orElseThrow();
    }

}
