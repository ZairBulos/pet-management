package com.petmanagement.reminders.domain.model.valueobject;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReminderTitleTest {

    @Nested
    class Creation {

        @Test
        void shouldCreateReminderTitle() {
            var title = new ReminderTitle("Rabies");

            assertEquals("Rabies", title.value());
        }

        @Test
        void shouldTrimReminderTitle() {
            var title = new ReminderTitle("   Rabies   ");

            assertEquals("Rabies", title.value());
        }

        @Test
        void shouldCreateReminderTitleWithExactly100Characters() {
            var value = "a".repeat(100);
            var title = new ReminderTitle(value);

            assertEquals(value, title.value());
        }

        @Test
        void shouldNotCountSurroundingWhitespaceInMaximumLength() {
            var value = "a".repeat(100);
            var title = new ReminderTitle("  " + value + "  ");

            assertEquals(value, title.value());
        }

        @Test
        void shouldCreateReminderTitleWithSpecialCharacters() {
            var title = new ReminderTitle("“Drontal Plus (1/2 tablet) – booster dose");

            assertEquals("“Drontal Plus (1/2 tablet) – booster dose", title.value());
        }

    }

    @Nested
    class Validation {

        @Test
        void shouldThrowWhenValueIsNull() {
            assertThrows(
                    NullPointerException.class,
                    () -> new ReminderTitle(null)
            );
        }

        @Test
        void shouldThrowWhenValueIsEmpty() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new ReminderTitle("")
            );
        }

        @Test
        void shouldThrowWhenValueIsBlank() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new ReminderTitle("    ")
            );
        }

        @Test
        void shouldThrowWhenTitleExceedsMaximumLength() {
            var value = "a".repeat(101);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> new ReminderTitle(value)
            );
        }

    }

}
