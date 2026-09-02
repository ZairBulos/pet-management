package com.petmanagement.health.domain.model.valueobject;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VaccineIdTest {

    @Nested
    class Generation {

        @Test
        void shouldCreateOwnerId() {
            var id = VaccineId.generate();

            assertNotNull(id);
            assertNotNull(id.value());
        }

        @Test
        void shouldCreatesDifferentIdentifiers() {
            var first = VaccineId.generate();
            var second = VaccineId.generate();

            assertNotEquals(first, second);
            assertNotEquals(first.value(), second.value());
        }

        @Test
        void shouldGenerateValidUuidFormat() {
            var id = VaccineId.generate();

            assertDoesNotThrow(() -> UUID.fromString(id.value().toString()));
        }

    }

    @Nested
    class CreationFromUuid {

        @Test
        void shouldCreateOwnerIdFromUuid() {
            var uuid = UUID.randomUUID();

            var vaccineId = VaccineId.of(uuid);

            assertEquals(uuid, vaccineId.value());
        }

        @Test
        void shouldCreateOwnerIdFromSameUuidMultipleTimes() {
            var uuid = UUID.randomUUID();

            var ownerId1 = VaccineId.of(uuid);
            var ownerId2 = VaccineId.of(uuid);

            assertEquals(ownerId1, ownerId2);
            assertEquals(ownerId1.value(), ownerId2.value());
        }

        @Test
        void shouldThrowWhenUuidIsNull() {
            assertThrows(
                    NullPointerException.class,
                    () -> VaccineId.of((UUID) null)
            );
        }

    }

    @Nested
    class CreationFromString {

        @Test
        void shouldCreateOwnerIdFromString() {
            var uuid = UUID.randomUUID();

            var vaccineId = VaccineId.of(uuid.toString());

            assertEquals(uuid, vaccineId.value());
        }

        @Test
        void shouldCreateOwnerIdFromUppercaseString() {
            var uuid = UUID.randomUUID();
            var uppercaseString = uuid.toString().toUpperCase();

            var vaccineId = VaccineId.of(uppercaseString);

            assertEquals(uuid, vaccineId.value());
        }

        @Test
        void shouldCreateOwnerIdFromLowercaseString() {
            var uuid = UUID.randomUUID();
            var lowercaseString = uuid.toString().toLowerCase();

            var vaccineId = VaccineId.of(lowercaseString);

            assertEquals(uuid, vaccineId.value());
        }

        @Test
        void shouldCreateOwnerIdFromMixedCaseString() {
            var uuidString = "550e8400-e29b-41d4-a716-446655440000";

            var vaccineId = VaccineId.of(uuidString);

            assertEquals(UUID.fromString(uuidString), vaccineId.value());
        }

        @Test
        void shouldThrowWhenStringIsNull() {
            assertThrows(
                    NullPointerException.class,
                    () -> VaccineId.of((String) null)
            );
        }

        @Test
        void shouldThrowWhenStringIsNotValidUuid() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> VaccineId.of("invalid-uuid")
            );
        }

        @Test
        void shouldThrowWhenStringIsEmpty() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> VaccineId.of("")
            );
        }

        @Test
        void shouldThrowWhenStringHasWrongFormat() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> VaccineId.of("550e8400e29b41d4a716446655440000") // Missing hyphens
            );
        }

        @Test
        void shouldThrowWhenStringHasPartialUuid() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> VaccineId.of("550e8400-e29b-41d4-a716")
            );
        }

        @Test
        void shouldThrowWhenStringHasExtraCharacters() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> VaccineId.of("550e8400-e29b-41d4-a716-446655440000-extra")
            );
        }

    }

}
