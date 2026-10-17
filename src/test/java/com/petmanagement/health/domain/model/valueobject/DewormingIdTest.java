package com.petmanagement.health.domain.model.valueobject;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DewormingIdTest {

    @Nested
    class Generation {

        @Test
        void shouldCreateDewormingId() {
            var id = DewormingId.generate();

            assertNotNull(id);
            assertNotNull(id.value());
        }

        @Test
        void shouldCreatesDifferentIdentifiers() {
            var first = DewormingId.generate();
            var second = DewormingId.generate();

            assertNotEquals(first, second);
            assertNotEquals(first.value(), second.value());
        }

        @Test
        void shouldGenerateValidUuidFormat() {
            var id = DewormingId.generate();

            assertDoesNotThrow(() -> UUID.fromString(id.value().toString()));
        }

    }

    @Nested
    class CreationFromUuid {

        @Test
        void shouldCreateDewormingIdFromUuid() {
            var uuid = UUID.randomUUID();

            var dewormingId = DewormingId.of(uuid);

            assertEquals(uuid, dewormingId.value());
        }

        @Test
        void shouldCreateDewormingIdFromSameUuidMultipleTimes() {
            var uuid = UUID.randomUUID();

            var ownerId1 = DewormingId.of(uuid);
            var ownerId2 = DewormingId.of(uuid);

            assertEquals(ownerId1, ownerId2);
            assertEquals(ownerId1.value(), ownerId2.value());
        }

        @Test
        void shouldThrowWhenUuidIsNull() {
            assertThrows(
                    NullPointerException.class,
                    () -> DewormingId.of((UUID) null)
            );
        }

    }

    @Nested
    class CreationFromString {

        @Test
        void shouldCreateDewormingIdFromString() {
            var uuid = UUID.randomUUID();

            var dewormingId = DewormingId.of(uuid.toString());

            assertEquals(uuid, dewormingId.value());
        }

        @Test
        void shouldCreateDewormingIdFromUppercaseString() {
            var uuid = UUID.randomUUID();
            var uppercaseString = uuid.toString().toUpperCase();

            var dewormingId = DewormingId.of(uppercaseString);

            assertEquals(uuid, dewormingId.value());
        }

        @Test
        void shouldCreateDewormingIdFromLowercaseString() {
            var uuid = UUID.randomUUID();
            var lowercaseString = uuid.toString().toLowerCase();

            var dewormingId = DewormingId.of(lowercaseString);

            assertEquals(uuid, dewormingId.value());
        }

        @Test
        void shouldCreateDewormingIdFromMixedCaseString() {
            var uuidString = "550e8400-e29b-41d4-a716-446655440000";

            var dewormingId = DewormingId.of(uuidString);

            assertEquals(UUID.fromString(uuidString), dewormingId.value());
        }

        @Test
        void shouldThrowWhenStringIsNull() {
            assertThrows(
                    NullPointerException.class,
                    () -> DewormingId.of((String) null)
            );
        }

        @Test
        void shouldThrowWhenStringIsNotValidUuid() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> DewormingId.of("invalid-uuid")
            );
        }

        @Test
        void shouldThrowWhenStringIsEmpty() {
            assertThrows(IllegalArgumentException.class, () -> DewormingId.of(""));
        }

        @Test
        void shouldThrowWhenStringHasWrongFormat() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> DewormingId.of("550e8400e29b41d4a716446655440000") // Missing hyphens
            );
        }

        @Test
        void shouldThrowWhenStringHasPartialUuid() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> DewormingId.of("550e8400-e29b-41d4-a716")
            );
        }

        @Test
        void shouldThrowWhenStringHasExtraCharacters() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> DewormingId.of("550e8400-e29b-41d4-a716-446655440000-extra")
            );
        }

    }

}
