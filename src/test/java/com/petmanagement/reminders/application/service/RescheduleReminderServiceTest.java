package com.petmanagement.reminders.application.service;

import com.petmanagement.reminders.domain.exception.ReminderNotFoundException;
import com.petmanagement.reminders.support.InMemoryReminderRepository;
import com.petmanagement.reminders.support.TestReminderMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.petmanagement.reminders.support.ReminderTestBuilder.RescheduleReminderCommandBuilder.aRescheduleReminderCommand;
import static com.petmanagement.reminders.support.ReminderTestBuilder.aReminder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RescheduleReminderServiceTest {

    private InMemoryReminderRepository repository;
    private RescheduleReminderService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryReminderRepository();
        service = new RescheduleReminderService(repository);
    }

    @Nested
    class WhenReschedulingReminder {

        @Test
        void shouldRescheduleVaccineReminder() {
            // Given
            var existing = aReminder().vaccine().build();
            repository.save(existing);

            var command = aRescheduleReminderCommand().vaccine().build();

            // When
            service.execute(command);

            // Then
            assertEquals(command.dueDate(), existing.getDueDate().value());
        }

        @Test
        void shouldRescheduleDewormingReminder() {
            // Given
            var existing = aReminder().deworming().build();
            repository.save(existing);

            var command = aRescheduleReminderCommand().deworming().build();

            // When
            service.execute(command);

            // Then
            assertEquals(command.dueDate(), existing.getDueDate().value());
        }

    }

    @Nested
    class WhenReminderDoesNotExist {

        @Test
        void shouldThrowWhenReminderDoesNotExist() {
            // Given
            var command = aRescheduleReminderCommand()
                    .withSourceId(TestReminderMother.NON_EXISTENT_VACCINE_ID)
                    .build();

            // When/Then
            assertThrows(
                    ReminderNotFoundException.class,
                    () -> service.execute(command)
            );
        }

    }

}
