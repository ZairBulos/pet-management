package com.petmanagement.reminders.domain.model.valueobject;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DaysBeforeTest {

    @Nested
    class Creation {

        @Test
        void shouldCreateSameDay() {
            var daysBefore = new DaysBefore(0);

            assertEquals(0, daysBefore.value());
        }

        @Test
        void shouldCreateThreeDays() {
            var daysBefore = new DaysBefore(3);

            assertEquals(3, daysBefore.value());
        }

        @Test
        void shouldExposeConstants() {
            assertEquals(0, DaysBefore.SAME_DAY.value());
            assertEquals(3, DaysBefore.THREE_DAYS.value());
        }

    }

    @Nested
    class Validation {

        @Test
        void shouldThrowWhenValueIsNegative() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new DaysBefore(-1)
            );
        }

    }

    @Nested
    class Defaults {

        @Test
        void shouldReturnThreeDaysAndSameDay() {
            var defaults = DaysBefore.defaults();

            assertEquals(2, defaults.size());
            assertTrue(defaults.contains(DaysBefore.THREE_DAYS));
            assertTrue(defaults.contains(DaysBefore.SAME_DAY));
        }

        @Test
        void shouldReturnUnmodifiableList() {
            var defaults = DaysBefore.defaults();

            assertThrows(
                    UnsupportedOperationException.class,
                    () -> defaults.add(DaysBefore.SAME_DAY)
            );
        }

    }

}
