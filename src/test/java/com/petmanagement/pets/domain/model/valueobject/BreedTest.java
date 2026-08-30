package com.petmanagement.pets.domain.model.valueobject;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BreedTest {

    @Nested
    class Creation {

        @Test
        void shouldCreateBreed() {
            var breed = new Breed("Labrador");
            assertEquals("Labrador", breed.value());
        }

        @Test
        void shouldCreateBreedWithMultipleWords() {
            var breed = new Breed("Golden Retriever");
            assertEquals("Golden Retriever", breed.value());
        }

        @Test
        void shouldTrimBreed() {
            var breed = new Breed("   Labrador   ");
            assertEquals("Labrador", breed.value());
        }

        @Test
        void shouldCreateBreedWithExactly100Characters() {
            var value = "a".repeat(100);
            var breed = new Breed(value);
            assertEquals(value, breed.value());
        }

        @Test
        void shouldCreateUnknownBreed() {
            var breed = Breed.unknown();
            assertTrue(breed.isUnknown());
            assertNull(breed.value());
        }

        @Test
        void shouldCreateBreedWithSingleCharacter() {
            var breed = new Breed("L");
            assertEquals("L", breed.value());
        }

    }

    @Nested
    class Validation {

        @Test
        void shouldThrowWhenValueIsEmpty() {
            assertThrows(IllegalArgumentException.class, () -> new Breed(""));
        }

        @Test
        void shouldThrowWhenValueIsBlank() {
            assertThrows(IllegalArgumentException.class, () -> new Breed("    "));
        }

        @Test
        void shouldThrowWhenBreedExceedsMaximumLength() {
            var value = "a".repeat(101);
            assertThrows(IllegalArgumentException.class, () -> new Breed(value));
        }

    }

    @Nested
    class IsUnknown {

        @Test
        void shouldReturnTrueForUnknownBreed() {
            var breed = Breed.unknown();
            assertTrue(breed.isUnknown());
        }

        @Test
        void shouldReturnFalseForKnownBreed() {
            var breed = new Breed("Labrador");
            assertFalse(breed.isUnknown());
        }

        @Test
        void shouldReturnFalseForAnyNonNullBreed() {
            var breed = new Breed("A");
            assertFalse(breed.isUnknown());
        }

    }

}
