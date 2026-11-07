package com.petmanagement.reminders.infrastructure.adapter.out.persistence.mapper;

import com.petmanagement.reminders.domain.model.valueobject.DaysBefore;
import com.petmanagement.reminders.support.TestReminderMother;
import com.petmanagement.reminders.support.TestReminderNotificationMother;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.petmanagement.reminders.support.ReminderJpaEntityTestBuilder.aReminderJpaEntity;
import static com.petmanagement.reminders.support.ReminderNotificationJpaEntityTestBuilder.aReminderNotificationJpaEntity;
import static com.petmanagement.reminders.support.ReminderTestBuilder.aReminder;
import static org.junit.jupiter.api.Assertions.*;

class ReminderMapperTest {

    private final ReminderMapper mapper = new ReminderMapper();

    @Nested
    class WhenMappingDomainToJpaEntity {

        @Test
        void shouldMapDomainToJpaEntity() {
            // Given
            var reminder = aReminder().build();

            // When
            var result = mapper.toJpaEntity(reminder);

            // Then
            assertNotNull(result);
            assertEquals(reminder.getId().value(), result.getId());
            assertEquals(reminder.getSourceType(), result.getSourceType());
            assertEquals(reminder.getSourceId().value(), result.getSourceId());
            assertEquals(reminder.getPetId().value(), result.getPetId());
            assertEquals(reminder.getTitle().value(), result.getTitle());
            assertEquals(reminder.getDueDate().value(), result.getDueDate());
            assertEquals(reminder.getCreatedAt(), result.getCreatedAt());
            assertEquals(reminder.getUpdatedAt(), result.getUpdatedAt());
        }

        @Test
        void shouldMapNotifications() {
            // Given
            var reminder = aReminder().build();

            // When
            var result = mapper.toJpaEntity(reminder);

            // Then
            assertEquals(reminder.getNotifications().size(), result.getNotifications().size());

            reminder.getNotifications().forEach(notification -> {
                var entity = result.findNotificationById(notification.getId().value()).orElseThrow();

                assertEquals(notification.getDaysBefore().value(), entity.getDaysBefore());
                assertEquals(notification.getScheduledFor(), entity.getScheduledFor());
                assertEquals(notification.getStatus(), entity.getStatus());
                assertEquals(notification.getAttempts(), entity.getAttempts());
            });
        }

        @Test
        void shouldLinkNotificationsToReminder() {
            // Given
            var reminder = aReminder().build();

            // When
            var result = mapper.toJpaEntity(reminder);

            // Then
            result.getNotifications()
                    .forEach(notification -> assertSame(result, notification.getReminder()));
        }

    }

    @Nested
    class WhenMappingJpaEntityToDomain {

        @Test
        void shouldMapJpaEntityToDomain() {
            // Given
            var entity = aReminderJpaEntity().build();

            // When
            var result = mapper.toDomain(entity);

            // Then
            assertNotNull(result);
            assertEquals(TestReminderMother.DEFAULT_REMINDER_ID, result.getId());
            assertEquals(TestReminderMother.EXISTING_VACCINE_ID, result.getSourceId());
            assertEquals(TestReminderMother.VACCINE_SOURCE_TYPE, result.getSourceType());
            assertEquals(TestReminderMother.EXISTING_PET_ID, result.getPetId());
            assertEquals(TestReminderMother.VACCINE_TITLE, result.getTitle());
            assertEquals(TestReminderMother.DUE_DATE_THREE_MONTHS, result.getDueDate());
            assertEquals(entity.getCreatedAt(), result.getCreatedAt());
            assertEquals(entity.getUpdatedAt(), result.getUpdatedAt());
        }

        @Test
        void shouldMapNotifications() {
            // Given
            var entity = aReminderJpaEntity()
                    .withNotifications(
                            aReminderNotificationJpaEntity().build(),
                            aReminderNotificationJpaEntity()
                                    .withId(TestReminderNotificationMother.ANOTHER_NOTIFICATION_ID.value())
                                    .withDaysBefore(0)
                                    .build()
                    )
                    .build();

            // When
            var result = mapper.toDomain(entity);

            // Then
            assertEquals(2, result.getNotifications().size());

            var first = result.getNotifications().getFirst();
            assertEquals(TestReminderNotificationMother.DEFAULT_NOTIFICATION_ID, first.getId());
            assertEquals(DaysBefore.THREE_DAYS, first.getDaysBefore());
            assertEquals(TestReminderNotificationMother.DEFAULT_SCHEDULED_FOR, first.getScheduledFor());
            assertTrue(first.isPending());

            var last = result.getNotifications().getLast();
            assertEquals(TestReminderNotificationMother.ANOTHER_NOTIFICATION_ID, last.getId());
            assertEquals(DaysBefore.SAME_DAY, last.getDaysBefore());
        }

    }

}
