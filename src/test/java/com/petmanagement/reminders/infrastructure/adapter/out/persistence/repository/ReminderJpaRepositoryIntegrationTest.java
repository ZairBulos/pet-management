package com.petmanagement.reminders.infrastructure.adapter.out.persistence.repository;

import com.petmanagement.reminders.support.TestReminderMother;
import com.petmanagement.support.RepositoryTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.petmanagement.reminders.support.ReminderJpaEntityTestBuilder.aReminderJpaEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RepositoryTest
class ReminderJpaRepositoryIntegrationTest {

    @Autowired
    private ReminderJpaRepository repository;

    @Nested
    class WhenFindingReminderBySourceId {

        @Test
        void shouldReturnReminderWhenSourceExists() {
            // Given
            var entity = aReminderJpaEntity()
                    .withId(TestReminderMother.EXISTING_VACCINE_ID.value())
                    .build();
            repository.save(entity);

            // When
            var result = repository.findBySourceId(entity.getId());

            // Then
            assertTrue(result.isPresent());
            assertEquals(entity.getId(), result.get().getId());
            assertEquals(TestReminderMother.EXISTING_VACCINE_ID.value(), result.get().getSourceId());
            assertEquals(entity.getNotifications().size(), result.get().getNotifications().size());
        }

        @Test
        void shouldReturnEmptyWhenSourceDoesNotExist() {
            // Given
            var nonExistentSourceId = TestReminderMother.NON_EXISTENT_VACCINE_ID.value();

            // When
            var result = repository.findBySourceId(nonExistentSourceId);

            // Then
            assertTrue(result.isEmpty());
        }

    }

}
