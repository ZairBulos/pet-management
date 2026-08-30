package com.petmanagement.pets.domain.model.valueobject;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PetNameTest {

    @Nested
    class Creation {

        @Test
        void shouldCreatePetName() {
            var name = new PetName("Max");
            assertEquals("Max", name.value());
        }

        @Test
        void shouldCreatePetNameWithExactly2Characters() {
            var name = new PetName("Bo");
            assertEquals("Bo", name.value());
        }

        @Test
        void shouldCreatePetNameWithExactly100Characters() {
            var value = "a".repeat(100);
            var name = new PetName(value);
            assertEquals(value, name.value());
        }

        @Test
        void shouldTrimPetName() {
            var name = new PetName("   Max   ");
            assertEquals("Max", name.value());
        }

        @Test
        void shouldCreatePetNameWithNumbers() {
            var name = new PetName("Dog2023");
            assertEquals("Dog2023", name.value());
        }

        @Test
        void shouldCreatePetNameWithSpecialCharacters() {
            var name = new PetName("Max-O'Brien");
            assertEquals("Max-O'Brien", name.value());
        }

    }

    @Nested
    class Validation {

        @Test
        void shouldThrowWhenValueIsNull() {
            assertThrows(NullPointerException.class, () -> new PetName(null));
        }

        @Test
        void shouldThrowWhenValueIsEmpty() {
            assertThrows(IllegalArgumentException.class, () -> new PetName(""));
        }

        @Test
        void shouldThrowWhenValueIsBlank() {
            assertThrows(IllegalArgumentException.class, () -> new PetName("    "));
        }

        @Test
        void shouldThrowWhenNameIsSingleCharacter() {
            assertThrows(IllegalArgumentException.class, () -> new PetName("A"));
        }

        @Test
        void shouldThrowWhenNameExceedsMaximumLength() {
            var value = "a".repeat(101);
            assertThrows(IllegalArgumentException.class, () -> new PetName(value));
        }

    }

}
