package com.petmanagement.health.domain.model.valueobject;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class NextDueDateTest {

    @Nested
    class Creation {

        @Test
        void shouldCreateNextDueDate() {
            var date = LocalDate.now().plusDays(30);

            var dueDate = new NextDueDate(date);

            assertEquals(date, dueDate.value());
        }

        @Test
        void shouldThrowWhenValueIsNull() {
            assertThrows(
                    NullPointerException.class,
                    () -> new NextDueDate(null)
            );
        }

    }

    @Nested
    class After {

        @Test
        void shouldCreateNextDueDateAfterBaseDate() {
            var baseDate = LocalDate.of(2024, 1, 15);
            var dueDate = LocalDate.of(2024, 4, 15);

            var nextDueDate = NextDueDate.after(baseDate, dueDate);

            assertEquals(dueDate, nextDueDate.value());
        }

        @Test
        void shouldThrowWhenDueDateIsBeforeBaseDate() {
            var baseDate = LocalDate.of(2024, 4, 15);
            var dueDate = LocalDate.of(2024, 1, 15);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> NextDueDate.after(baseDate, dueDate)
            );
        }

        @Test
        void shouldThrowWhenDueDateIsEqualToBaseDate() {
            var baseDate = LocalDate.of(2024, 1, 15);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> NextDueDate.after(baseDate, baseDate)
            );
        }

        @Test
        void shouldThrowWhenBaseDateIsNull() {
            var dueDate = LocalDate.now().plusDays(30);

            assertThrows(
                    NullPointerException.class,
                    () -> NextDueDate.after(null, dueDate)
            );
        }

        @Test
        void shouldThrowWhenDueDateIsNull() {
            var baseDate = LocalDate.now();

            assertThrows(
                    NullPointerException.class,
                    () -> NextDueDate.after(baseDate, null)
            );
        }

    }

    @Nested
    class DaysRemaining {

        @Test
        void shouldReturnPositiveDaysWhenFuture() {
            var today = LocalDate.now();
            var futureDate = today.plusDays(30);
            var dueDate = new NextDueDate(futureDate);

            var days = dueDate.daysRemaining(today);

            assertEquals(30, days);
        }

        @Test
        void shouldReturnZeroDaysWhenToday() {
            var today = LocalDate.now();
            var dueDate = new NextDueDate(today);

            var days = dueDate.daysRemaining(today);

            assertEquals(0, days);
        }

        @Test
        void shouldReturnNegativeDaysWhenPast() {
            var today = LocalDate.now();
            var pastDate = today.minusDays(10);
            var dueDate = new NextDueDate(pastDate);

            var days = dueDate.daysRemaining(today);

            assertEquals(-10, days);
        }

        @Test
        void shouldThrowWhenReferenceDateIsNull() {
            var dueDate = new NextDueDate(LocalDate.now().plusDays(10));

            assertThrows(
                    NullPointerException.class,
                    () -> dueDate.daysRemaining(null)
            );
        }

    }

    @Nested
    class IsOverdue {

        @Test
        void shouldReturnFalseWhenFuture() {
            var today = LocalDate.now();
            var futureDate = today.plusDays(30);
            var dueDate = new NextDueDate(futureDate);

            assertFalse(dueDate.isOverdue(today));
        }

        @Test
        void shouldReturnFalseWhenToday() {
            var today = LocalDate.now();
            var dueDate = new NextDueDate(today);

            assertFalse(dueDate.isOverdue(today));
        }

        @Test
        void shouldReturnTrueWhenPast() {
            var today = LocalDate.now();
            var pastDate = today.minusDays(10);
            var dueDate = new NextDueDate(pastDate);

            assertTrue(dueDate.isOverdue(today));
        }

        @Test
        void shouldThrowWhenReferenceDateIsNull() {
            var dueDate = new NextDueDate(LocalDate.now().plusDays(10));

            assertThrows(
                    NullPointerException.class,
                    () -> dueDate.isOverdue(null)
            );
        }

    }

}
