package com.petmanagement.health.domain.model.valueobject;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DrugDoseTest {

    @Nested
    class Creation {

        @Test
        void shouldCreateDrugDose() {
            var dose = new DrugDose("1 tablet");

            assertEquals("1 tablet", dose.value());
        }

        @Test
        void shouldCreateDrugDoseWithNumbers() {
            var dose = new DrugDose("500mg twice daily");

            assertEquals("500mg twice daily", dose.value());
        }

        @Test
        void shouldTrimDrugDose() {
            var dose = new DrugDose("   1 tablet   ");

            assertEquals("1 tablet", dose.value());
        }

        @Test
        void shouldCreateDrugDoseWithExactly100Characters() {
            var value = "a".repeat(100);
            var dose = new DrugDose(value);

            assertEquals(value, dose.value());
        }

        @Test
        void shouldCreateDrugDoseWithSpecialCharacters() {
            var dose = new DrugDose("2.5ml/kg (1/2 tablet)");

            assertEquals("2.5ml/kg (1/2 tablet)", dose.value());
        }

    }

    @Nested
    class Validation {

        @Test
        void shouldThrowWhenValueIsNull() {
            assertThrows(
                    NullPointerException.class,
                    () -> new DrugDose(null)
            );
        }

        @Test
        void shouldThrowWhenValueIsEmpty() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new DrugDose("")
            );
        }

        @Test
        void shouldThrowWhenValueIsBlank() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new DrugDose("    ")
            );
        }

        @Test
        void shouldThrowWhenDoseExceedsMaximumLength() {
            var value = "a".repeat(101);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> new DrugDose(value)
            );
        }

    }

}
