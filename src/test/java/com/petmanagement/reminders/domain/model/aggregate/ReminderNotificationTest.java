package com.petmanagement.reminders.domain.model.aggregate;

import com.petmanagement.reminders.domain.exception.InvalidNotificationStateException;
import com.petmanagement.reminders.domain.model.enums.NotificationStatus;
import com.petmanagement.reminders.domain.model.valueobject.DaysBefore;
import com.petmanagement.reminders.domain.model.valueobject.NotificationId;
import com.petmanagement.reminders.support.TestReminderMother;
import com.petmanagement.reminders.support.TestReminderNotificationMother;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;

import static com.petmanagement.reminders.support.ReminderNotificationTestBuilder.aReminderNotification;
import static org.junit.jupiter.api.Assertions.*;

class ReminderNotificationTest {

    @Nested
    class Creation {

        @Test
        void shouldCreateNotification() {
            // When
            var notification = ReminderNotification.create(
                    DaysBefore.THREE_DAYS,
                    TestReminderMother.DUE_DATE_THREE_MONTHS
            );

            // Then
            assertNotNull(notification.getId());
            assertEquals(DaysBefore.THREE_DAYS, notification.getDaysBefore());
            assertEquals(NotificationStatus.PENDING, notification.getStatus());
            assertNull(notification.getSentAt());
            assertNull(notification.getLastAttemptAt());
            assertEquals(0, notification.getAttempts());
            assertNull(notification.getLastError());
        }

        @Test
        void shouldScheduleThreeDaysBeforeDueDate() {
            // Given
            var dueDate = TestReminderMother.DUE_DATE_THREE_MONTHS;

            // When
            var notification = ReminderNotification.create(DaysBefore.THREE_DAYS, dueDate);

            // Then
            assertEquals(dueDate.value().minusDays(3), notification.getScheduledFor());
        }

        @Test
        void shouldScheduleOnDueDateWhenSameDay() {
            // Given
            var dueDate = TestReminderMother.DUE_DATE_THREE_MONTHS;

            // When
            var notification = ReminderNotification.create(DaysBefore.SAME_DAY, dueDate);

            // Then
            assertEquals(dueDate.value(), notification.getScheduledFor());
        }

        @Test
        void shouldGenerateUniqueIdForEachNotification() {
            // When
            var notification1 = ReminderNotification.create(
                    DaysBefore.THREE_DAYS,
                    TestReminderMother.DUE_DATE_THREE_MONTHS
            );
            var notification2 = ReminderNotification.create(
                    DaysBefore.THREE_DAYS,
                    TestReminderMother.DUE_DATE_THREE_MONTHS
            );

            // Then
            assertNotEquals(notification1.getId(), notification2.getId());
        }

        @Test
        void shouldThrowWhenCreatingWithNullDaysBefore() {
            // When/Then
            assertThrows(
                    NullPointerException.class,
                    () -> ReminderNotification.create(
                            null,
                            TestReminderMother.DUE_DATE_THREE_MONTHS
                    )
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullDueDate() {
            // When/Then
            assertThrows(
                    NullPointerException.class,
                    () -> ReminderNotification.create(
                            DaysBefore.THREE_DAYS,
                            null
                    )
            );
        }

    }

    @Nested
    class Reconstitution {

        @Test
        void shouldReconstituteNotification() {
            // Given
            var id = NotificationId.generate();
            var sentAt = Instant.now().minus(Duration.ofHours(2));
            var lastAttemptAt = Instant.now().minus(Duration.ofHours(1));

            // When
            var notification = ReminderNotification.reconstitute(
                    id,
                    DaysBefore.SAME_DAY,
                    TestReminderNotificationMother.DEFAULT_SCHEDULED_FOR,
                    NotificationStatus.SENT,
                    sentAt,
                    lastAttemptAt,
                    2,
                    TestReminderNotificationMother.DEFAULT_ERROR
            );

            // Then
            assertEquals(id, notification.getId());
            assertEquals(DaysBefore.SAME_DAY, notification.getDaysBefore());
            assertEquals(TestReminderNotificationMother.DEFAULT_SCHEDULED_FOR, notification.getScheduledFor());
            assertEquals(NotificationStatus.SENT, notification.getStatus());
            assertEquals(sentAt, notification.getSentAt());
            assertEquals(lastAttemptAt, notification.getLastAttemptAt());
            assertEquals(2, notification.getAttempts());
            assertEquals(TestReminderNotificationMother.DEFAULT_ERROR, notification.getLastError());
        }

        @Test
        void shouldThrowWhenReconstituteWithNullId() {
            // When/Then
            assertThrows(
                    NullPointerException.class,
                    () -> ReminderNotification.reconstitute(
                            null,
                            DaysBefore.THREE_DAYS,
                            TestReminderNotificationMother.DEFAULT_SCHEDULED_FOR,
                            NotificationStatus.PENDING,
                            null,
                            null,
                            0,
                            null
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullDaysBefore() {
            // Given
            var id = NotificationId.generate();

            // When/Then
            assertThrows(
                    NullPointerException.class,
                    () -> ReminderNotification.reconstitute(
                            id,
                            null,
                            TestReminderNotificationMother.DEFAULT_SCHEDULED_FOR,
                            NotificationStatus.PENDING,
                            null,
                            null,
                            0,
                            null
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullScheduledFor() {
            // Given
            var id = NotificationId.generate();

            // When/Then
            assertThrows(
                    NullPointerException.class,
                    () -> ReminderNotification.reconstitute(
                            id,
                            DaysBefore.THREE_DAYS,
                            null,
                            NotificationStatus.PENDING,
                            null,
                            null,
                            0,
                            null
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullStatus() {
            // Given
            var id = NotificationId.generate();

            // When/Then
            assertThrows(
                    NullPointerException.class,
                    () -> ReminderNotification.reconstitute(
                            id,
                            DaysBefore.THREE_DAYS,
                            TestReminderNotificationMother.DEFAULT_SCHEDULED_FOR,
                            null,
                            null,
                            null,
                            0,
                            null
                    )
            );
        }

    }

    @Nested
    class Reschedule {

        @Test
        void shouldRecalculateScheduledForWhenPending() {
            // Given
            var notification = aReminderNotification().withDaysBefore(DaysBefore.THREE_DAYS).build();
            var newDueDate = TestReminderMother.DUE_DATE_SIX_MONTHS;

            // When
            notification.reschedule(newDueDate);

            // Then
            assertEquals(newDueDate.value().minusDays(3), notification.getScheduledFor());
            assertEquals(NotificationStatus.PENDING, notification.getStatus());
        }

        @Test
        void shouldReturnToPendingWhenSkipped() {
            // Given
            var notification = aReminderNotification().skipped().build();

            // When
            notification.reschedule(TestReminderMother.DUE_DATE_SIX_MONTHS);

            // Then
            assertEquals(NotificationStatus.PENDING, notification.getStatus());
        }

        @Test
        void shouldResetDeliveryStateWhenPendingWithFailedAttempts() {
            // Given
            var notification = aReminderNotification()
                    .withAttempts(2)
                    .withLastAttemptAt(TestReminderNotificationMother.DEFAULT_ATTEMPT_AT)
                    .withLastError(TestReminderNotificationMother.DEFAULT_ERROR)
                    .build();

            // When
            notification.reschedule(TestReminderMother.DUE_DATE_SIX_MONTHS);

            // Then
            assertEquals(0, notification.getAttempts());
            assertNull(notification.getLastAttemptAt());
            assertNull(notification.getLastError());
        }

        @Test
        void shouldNotRescheduleWhenSent() {
            // Given
            var notification = aReminderNotification().sent().build();
            var scheduledFor = notification.getScheduledFor();

            // When
            notification.reschedule(TestReminderMother.DUE_DATE_SIX_MONTHS);

            // Then
            assertEquals(NotificationStatus.SENT, notification.getStatus());
            assertEquals(scheduledFor, notification.getScheduledFor());
        }

        @Test
        void shouldNotRescheduleWhenFailed() {
            // Given
            var notification = aReminderNotification().failed().build();
            var scheduledFor = notification.getScheduledFor();

            // When
            notification.reschedule(TestReminderMother.DUE_DATE_SIX_MONTHS);

            // Then
            assertEquals(NotificationStatus.FAILED, notification.getStatus());
            assertEquals(scheduledFor, notification.getScheduledFor());
        }

        @Test
        void shouldThrowWhenReschedulingWithNullDueDate() {
            // Given
            var notification = aReminderNotification().build();

            // When/Then
            assertThrows(
                    NullPointerException.class,
                    () -> notification.reschedule(null)
            );
        }

    }

    @Nested
    class MarkSent {

        @Test
        void shouldMarkPendingNotificationAsSent() {
            // Given
            var notification = aReminderNotification().pending().build();

            // When
            notification.markSent();

            // Then
            assertEquals(NotificationStatus.SENT, notification.getStatus());
            assertNotNull(notification.getSentAt());
            assertNotNull(notification.getLastAttemptAt());
            assertEquals(1, notification.getAttempts());
            assertNull(notification.getLastError());
        }

        @Test
        void shouldClearLastErrorWhenSentAfterFailedAttempt() {
            // Given
            var notification = aReminderNotification()
                    .withAttempts(1)
                    .withLastError(TestReminderNotificationMother.DEFAULT_ERROR)
                    .build();

            // When
            notification.markSent();

            // Then
            assertEquals(2, notification.getAttempts());
            assertNull(notification.getLastError());
        }

        @Test
        void shouldThrowWhenAlreadySent() {
            // Given
            var notification = aReminderNotification().sent().build();

            // When/Then
            assertThrows(
                    InvalidNotificationStateException.class,
                    notification::markSent
            );
        }

        @Test
        void shouldThrowWhenFailed() {
            // Given
            var notification = aReminderNotification().failed().build();

            // When/Then
            assertThrows(
                    InvalidNotificationStateException.class,
                    notification::markSent
            );
        }

        @Test
        void shouldThrowWhenSkipped() {
            // Given
            var notification = aReminderNotification().skipped().build();

            // When/Then
            assertThrows(
                    InvalidNotificationStateException.class,
                    notification::markSent
            );
        }

    }

    @Nested
    class MarkFailed {

        @Test
        void shouldRegisterAttemptAndKeepPendingBelowMaxAttempts() {
            // Given
            var notification = aReminderNotification().build();

            // When
            notification.markFailed(
                    TestReminderNotificationMother.DEFAULT_ERROR,
                    TestReminderNotificationMother.DEFAULT_MAX_ATTEMPTS
            );

            // Then
            assertEquals(NotificationStatus.PENDING, notification.getStatus());
            assertEquals(1, notification.getAttempts());
            assertNotNull(notification.getLastAttemptAt());
            assertEquals(TestReminderNotificationMother.DEFAULT_ERROR, notification.getLastError());
            assertNull(notification.getSentAt());
        }

        @Test
        void shouldMarkAsFailedWhenReachingMaxAttempts() {
            // Given
            var notification = aReminderNotification()
                    .withAttempts(TestReminderNotificationMother.DEFAULT_MAX_ATTEMPTS - 1)
                    .build();

            // When
            notification.markFailed(
                    TestReminderNotificationMother.DEFAULT_ERROR,
                    TestReminderNotificationMother.DEFAULT_MAX_ATTEMPTS
            );

            // Then
            assertEquals(NotificationStatus.FAILED, notification.getStatus());
            assertEquals(TestReminderNotificationMother.DEFAULT_MAX_ATTEMPTS, notification.getAttempts());
        }

        @Test
        void shouldThrowWhenSent() {
            // Given
            var notification = aReminderNotification().sent().build();

            // When/Then
            assertThrows(
                    InvalidNotificationStateException.class,
                    () -> notification.markFailed(
                            TestReminderNotificationMother.DEFAULT_ERROR,
                            TestReminderNotificationMother.DEFAULT_MAX_ATTEMPTS
                    )
            );
        }

        @Test
        void shouldThrowWhenAlreadyFailed() {
            // Given
            var notification = aReminderNotification().failed().build();

            // When/Then
            assertThrows(
                    InvalidNotificationStateException.class,
                    () -> notification.markFailed(
                            TestReminderNotificationMother.DEFAULT_ERROR,
                            TestReminderNotificationMother.DEFAULT_MAX_ATTEMPTS
                    )
            );
        }

        @Test
        void shouldThrowWhenSkipped() {
            // Given
            var notification = aReminderNotification().skipped().build();

            // When/Then
            assertThrows(
                    InvalidNotificationStateException.class,
                    () -> notification.markFailed(
                            TestReminderNotificationMother.DEFAULT_ERROR,
                            TestReminderNotificationMother.DEFAULT_MAX_ATTEMPTS
                    )
            );
        }

    }

    @Nested
    class MarkSkipped {

        @Test
        void shouldMarkPendingNotificationAsSkipped() {
            // Given
            var notification = aReminderNotification().build();

            // When
            notification.markSkipped();

            // Then
            assertEquals(NotificationStatus.SKIPPED, notification.getStatus());
            assertEquals(0, notification.getAttempts());
            assertNull(notification.getSentAt());
        }

        @Test
        void shouldThrowWhenSent() {
            // Given
            var notification = aReminderNotification().sent().build();

            // When/Then
            assertThrows(
                    InvalidNotificationStateException.class,
                    notification::markSkipped
            );
        }

        @Test
        void shouldThrowWhenFailed() {
            // Given
            var notification = aReminderNotification().failed().build();

            // When/Then
            assertThrows(
                    InvalidNotificationStateException.class,
                    notification::markSkipped
            );
        }

        @Test
        void shouldThrowWhenAlreadySkipped() {
            // Given
            var notification = aReminderNotification().skipped().build();

            // When/Then
            assertThrows(
                    InvalidNotificationStateException.class,
                    notification::markSkipped
            );
        }

    }

    @Nested
    class IsOverdue {

        @Test
        void shouldBeOverdueWhenScheduledForIsBeforeToday() {
            // Given
            var notification = aReminderNotification()
                    .withScheduledFor(TestReminderNotificationMother.PAST_SCHEDULED_FOR)
                    .build();

            // When/Then
            assertTrue(notification.isOverdue(LocalDate.now()));
        }

        @Test
        void shouldNotBeOverdueWhenScheduledForIsToday() {
            // Given
            var today = LocalDate.now();
            var notification = aReminderNotification()
                    .withScheduledFor(today)
                    .build();

            // When/Then
            assertFalse(notification.isOverdue(today));
        }

        @Test
        void shouldNotBeOverdueWhenScheduledForIsAfterToday() {
            // Given
            var today = LocalDate.now();
            var notification = aReminderNotification()
                    .withScheduledFor(today.plusDays(1))
                    .build();

            // When/Then
            assertFalse(notification.isOverdue(today));
        }

    }

}
