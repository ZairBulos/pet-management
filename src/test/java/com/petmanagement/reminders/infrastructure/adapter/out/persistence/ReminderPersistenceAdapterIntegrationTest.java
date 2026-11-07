package com.petmanagement.reminders.infrastructure.adapter.out.persistence;

import com.petmanagement.reminders.infrastructure.adapter.out.persistence.mapper.ReminderMapper;
import com.petmanagement.reminders.support.TestReminderMother;
import com.petmanagement.support.RepositoryTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import static com.petmanagement.reminders.support.ReminderTestBuilder.aReminder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RepositoryTest
@Import({
        ReminderPersistenceAdapter.class,
        ReminderMapper.class
})
class ReminderPersistenceAdapterIntegrationTest {

    @Autowired
    private ReminderPersistenceAdapter adapter;

    @Nested
    class WhenFindingReminderBySourceId {

        @Test
        void shouldReturnReminderWhenExists() {
            // Given
            var reminder = aReminder().build();
            adapter.save(reminder);

            // When
            var result = adapter.findBySourceId(reminder.getSourceId());

            // Then
            assertTrue(result.isPresent());
            assertEquals(reminder.getId(), result.get().getId());
            assertEquals(reminder.getNotifications().size(), result.get().getNotifications().size());
        }

        @Test
        void shouldReturnEmptyWhenReminderDoesNotExist() {
            // Given
            var nonExistentSourceId = TestReminderMother.NON_EXISTENT_SOURCE_ID;

            // When
            var result = adapter.findBySourceId(nonExistentSourceId);

            // Then
            assertTrue(result.isEmpty());
        }

    }

    @Nested
    class WhenSavingReminder {

        @Test
        void shouldPersistReminderToDatabase() {
            // Given
            var reminder = aReminder().build();

            // When
            adapter.save(reminder);

            // Then
            var result = adapter.findBySourceId(reminder.getSourceId());

            assertTrue(result.isPresent());
        }

        @Test
        void shouldUpdateReminderWhenSavingExisting() {
            // Given
            var reminder = aReminder().build();
            adapter.save(reminder);

            reminder.reschedule(TestReminderMother.DUE_DATE_SIX_MONTHS);

            // When
            adapter.save(reminder);

            // Then
            var result = adapter.findBySourceId(reminder.getSourceId());

            assertTrue(result.isPresent());
            assertEquals(TestReminderMother.DUE_DATE_SIX_MONTHS, result.get().getDueDate());
        }

    }

}
