package com.petmanagement.health.domain.model.valueobject;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VaccineNameTest {

    @Nested
    class Creation {

        @Test
        void shouldCreateVaccineName() {
            var name = new VaccineName("Purevax RCP");

            assertEquals("Purevax RCP", name.value());
        }

        @Test
        void shouldCreateVaccineNameWithNumbers() {
            var name = new VaccineName("Felocell 4");

            assertEquals("Felocell 4", name.value());
        }

        @Test
        void shouldTrimVaccineName() {
            var name = new VaccineName("   Nobivac Tricat Trio   ");

            assertEquals("Nobivac Tricat Trio", name.value());
        }

        @Test
        void shouldCreateVaccineNameWithExactly100Characters() {
            var value = "a".repeat(100);
            var name = new VaccineName(value);

            assertEquals(value, name.value());
        }

        @Test
        void shouldCreateVaccineNameWithSpecialCharacters() {
            var name = new VaccineName("Feligen CRP + FeLV");

            assertEquals("Feligen CRP + FeLV", name.value());
        }

    }

    @Nested
    class Validation {

        @Test
        void shouldThrowWhenValueIsNull() {
            assertThrows(
                    NullPointerException.class,
                    () -> new VaccineName(null)
            );
        }

        @Test
        void shouldThrowWhenValueIsEmpty() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new VaccineName("")
            );
        }

        @Test
        void shouldThrowWhenValueIsBlank() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new VaccineName("    ")
            );
        }

        @Test
        void shouldThrowWhenDoseExceedsMaximumLength() {
            var value = "a".repeat(101);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> new VaccineName(value)
            );
        }

    }

}
