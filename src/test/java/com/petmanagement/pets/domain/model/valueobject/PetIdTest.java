package com.petmanagement.pets.domain.model.valueobject;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PetIdTest {

    @Nested
    class Generation {

        @Test
        void shouldCreatePetId() {
            var id = PetId.generate();

            assertNotNull(id);
            assertNotNull(id.value());
        }

        @Test
        void shouldCreatesDifferentIdentifiers() {
            var first = PetId.generate();
            var second = PetId.generate();

            assertNotEquals(first, second);
            assertNotEquals(first.value(), second.value());
        }

        @Test
        void shouldGenerateValidUuidFormat() {
            var id = PetId.generate();

            assertDoesNotThrow(() -> UUID.fromString(id.value().toString()));
        }

    }

    @Nested
    class CreationFromUuid {

        @Test
        void shouldCreatePetIdFromUuid() {
            var uuid = UUID.randomUUID();

            var petId = PetId.of(uuid);

            assertEquals(uuid, petId.value());
        }

        @Test
        void shouldCreatePetIdFromSameUuidMultipleTimes() {
            var uuid = UUID.randomUUID();

            var petId1 = PetId.of(uuid);
            var petId2 = PetId.of(uuid);

            assertEquals(petId1, petId2);
            assertEquals(petId1.value(), petId2.value());
        }

        @Test
        void shouldThrowWhenUuidIsNull() {
            assertThrows(NullPointerException.class, () -> PetId.of((UUID) null));
        }

    }

    @Nested
    class CreationFromString {

        @Test
        void shouldCreatePetIdFromString() {
            var uuid = UUID.randomUUID();

            var petId = PetId.of(uuid.toString());

            assertEquals(uuid, petId.value());
        }

        @Test
        void shouldCreatePetIdFromUppercaseString() {
            var uuid = UUID.randomUUID();
            var uppercaseString = uuid.toString().toUpperCase();

            var petId = PetId.of(uppercaseString);

            assertEquals(uuid, petId.value());
        }

        @Test
        void shouldCreatePetIdFromLowercaseString() {
            var uuid = UUID.randomUUID();
            var lowercaseString = uuid.toString().toLowerCase();

            var petId = PetId.of(lowercaseString);

            assertEquals(uuid, petId.value());
        }

        @Test
        void shouldCreatePetIdFromMixedCaseString() {
            var uuidString = "550e8400-e29b-41d4-a716-446655440000";

            var petId = PetId.of(uuidString);

            assertEquals(UUID.fromString(uuidString), petId.value());
        }

        @Test
        void shouldThrowWhenStringIsNull() {
            assertThrows(NullPointerException.class, () -> PetId.of((String) null));
        }

        @Test
        void shouldThrowWhenStringIsNotValidUuid() {
            assertThrows(IllegalArgumentException.class, () -> PetId.of("invalid-uuid"));
        }

        @Test
        void shouldThrowWhenStringIsEmpty() {
            assertThrows(IllegalArgumentException.class, () -> PetId.of(""));
        }

        @Test
        void shouldThrowWhenStringHasWrongFormat() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> PetId.of("550e8400e29b41d4a716446655440000") // Missing hyphens
            );
        }

        @Test
        void shouldThrowWhenStringHasPartialUuid() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> PetId.of("550e8400-e29b-41d4-a716")
            );
        }

        @Test
        void shouldThrowWhenStringHasExtraCharacters() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> PetId.of("550e8400-e29b-41d4-a716-446655440000-extra")
            );
        }

    }

}
