package com.petmanagement.reminders.application.service;

import com.petmanagement.reminders.support.InMemoryReminderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.petmanagement.reminders.support.ReminderTestBuilder.CreateReminderCommandBuilder.aCreateReminderCommand;
import static com.petmanagement.reminders.support.ReminderTestBuilder.aReminder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CreateReminderServiceTest {

    private InMemoryReminderRepository repository;
    private CreateReminderService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryReminderRepository();
        service = new CreateReminderService(repository);
    }

    @Nested
    class WhenCreatingReminder {

        @Test
        void shouldCreateVaccineReminder() {
            // Given
            var command = aCreateReminderCommand().vaccine().build();

            // When
            var reminderId = service.execute(command);

            // Then
            assertNotNull(reminderId);
        }

        @Test
        void shouldCreateDewormReminder() {
            // Given
            var command = aCreateReminderCommand().deworming().build();

            // When
            var reminderId = service.execute(command);

            // Then
            assertNotNull(reminderId);
        }

    }

    @Nested
    class WhenExistingReminder {

        @Test
        void shouldReturnExistingReminderId() {
            // Given
            var existing = aReminder().vaccine().build();
            repository.save(existing);

            var command = aCreateReminderCommand().vaccine().build();

            // When
            var reminderId = service.execute(command);

            // Then
            assertEquals(existing.getId(), reminderId);
        }


        @Test
        void shouldNotCreateDuplicate() {
            // Given
            repository.save(aReminder().deworming().build());

            var command = aCreateReminderCommand().deworming().build();

            // When
            service.execute(command);

            // Then
            assertEquals(1, repository.count());
        }

    }

}
