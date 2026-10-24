package com.petmanagement.reminders.domain.model.valueobject;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReminderDueDateTest {

    @Nested
    class Creation {

        @Test
        void shouldCreateReminderDueDate() {
            var date = LocalDate.now().plusDays(10);

            var dueDate = new ReminderDueDate(date);

            assertEquals(date, dueDate.value());
        }

        @Test
        void shouldCreateReminderDueDateInThePast() {
            var date = LocalDate.now().minusDays(10);

            var dueDate = new ReminderDueDate(date);

            assertEquals(date, dueDate.value());
        }

    }

    @Nested
    class Validation {

        @Test
        void shouldThrowWhenValueIsNull() {
            assertThrows(
                    NullPointerException.class,
                    () -> new ReminderDueDate(null)
            );
        }

    }

}
