package com.petmanagement.health.domain.model.valueobject;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DrugNameTest {

    @Nested
    class Creation {

        @Test
        void shouldCreateDrugName() {
            var name = new DrugName("Amoxicillin");

            assertEquals("Amoxicillin", name.value());
        }

        @Test
        void shouldCreateDrugNameWithNumbers() {
            var name = new DrugName("Baytril 50mg");

            assertEquals("Baytril 50mg", name.value());
        }

        @Test
        void shouldTrimDrugName() {
            var name = new DrugName("   Amoxicillin   ");

            assertEquals("Amoxicillin", name.value());
        }

        @Test
        void shouldCreateDrugNameWithExactly100Characters() {
            var value = "a".repeat(100);
            var name = new DrugName(value);

            assertEquals(value, name.value());
        }

        @Test
        void shouldCreateDrugNameWithSpecialCharacters() {
            var name = new DrugName("Amoxicillin-Clavulanate 500mg/125mg");

            assertEquals("Amoxicillin-Clavulanate 500mg/125mg", name.value());
        }

    }

    @Nested
    class Validation {

        @Test
        void shouldThrowWhenValueIsNull() {
            assertThrows(
                    NullPointerException.class,
                    () -> new DrugName(null)
            );
        }

        @Test
        void shouldThrowWhenValueIsEmpty() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new DrugName("")
            );
        }

        @Test
        void shouldThrowWhenValueIsBlank() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new DrugName("    ")
            );
        }

        @Test
        void shouldThrowWhenDoseExceedsMaximumLength() {
            var value = "a".repeat(101);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> new DrugName(value)
            );
        }

    }

}
