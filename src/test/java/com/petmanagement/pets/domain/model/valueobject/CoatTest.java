package com.petmanagement.pets.domain.model.valueobject;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CoatTest {

    @Nested
    class Creation {

        @Test
        void shouldCreateCoat() {
            var coat = new Coat("Brown");
            assertEquals("Brown", coat.value());
        }

        @Test
        void shouldCreateCoatWithMultipleWords() {
            var coat = new Coat("Brown and White");
            assertEquals("Brown and White", coat.value());
        }

        @Test
        void shouldTrimCoat() {
            var coat = new Coat("   Brown   ");
            assertEquals("Brown", coat.value());
        }

        @Test
        void shouldCreateCoatWithExactly100Characters() {
            var value = "a".repeat(100);
            var coat = new Coat(value);
            assertEquals(value, coat.value());
        }

        @Test
        void shouldCreateCoatWithSingleCharacter() {
            var coat = new Coat("B");
            assertEquals("B", coat.value());
        }

    }

    @Nested
    class Validation {

        @Test
        void shouldThrowWhenValueIsNull() {
            assertThrows(NullPointerException.class, () -> new Coat(null));
        }

        @Test
        void shouldThrowWhenValueIsEmpty() {
            assertThrows(IllegalArgumentException.class, () -> new Coat(""));
        }

        @Test
        void shouldThrowWhenValueIsBlank() {
            assertThrows(IllegalArgumentException.class, () -> new Coat("    "));
        }

        @Test
        void shouldThrowWhenCoatExceedsMaximumLength() {
            var value = "a".repeat(101);
            assertThrows(IllegalArgumentException.class, () -> new Coat(value));
        }

    }

}
