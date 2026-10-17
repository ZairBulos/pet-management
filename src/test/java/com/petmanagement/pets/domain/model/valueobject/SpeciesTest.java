package com.petmanagement.pets.domain.model.valueobject;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SpeciesTest {

    @Nested
    class Creation {

        @Test
        void shouldCreateSpecies() {
            var species = new Species("Dog");
            assertEquals("Dog", species.value());
        }

        @Test
        void shouldCreateSpeciesWithMultipleWords() {
            var species = new Species("Guinea Pig");
            assertEquals("Guinea Pig", species.value());
        }

        @Test
        void shouldTrimSpecies() {
            var species = new Species("   Dog   ");
            assertEquals("Dog", species.value());
        }

        @Test
        void shouldCreateSpeciesWithExactly60Characters() {
            var value = "a".repeat(60);
            var species = new Species(value);
            assertEquals(value, species.value());
        }

        @Test
        void shouldCreateSpeciesWithSingleCharacter() {
            var species = new Species("D");
            assertEquals("D", species.value());
        }

    }

    @Nested
    class Validation {

        @Test
        void shouldThrowWhenValueIsNull() {
            assertThrows(NullPointerException.class, () -> new Species(null));
        }

        @Test
        void shouldThrowWhenValueIsEmpty() {
            assertThrows(IllegalArgumentException.class, () -> new Species(""));
        }

        @Test
        void shouldThrowWhenValueIsBlank() {
            assertThrows(IllegalArgumentException.class, () -> new Species("    "));
        }

        @Test
        void shouldThrowWhenSpeciesExceedsMaximumLength() {
            var value = "a".repeat(61);
            assertThrows(IllegalArgumentException.class, () -> new Species(value));
        }

    }

}
