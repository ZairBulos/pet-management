package com.petmanagement.health.domain.model.valueobject;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WeightRecordIdTest {

    @Nested
    class Generation {

        @Test
        void shouldCreateOwnerId() {
            var id = WeightRecordId.generate();

            assertNotNull(id);
            assertNotNull(id.value());
        }

        @Test
        void shouldCreatesDifferentIdentifiers() {
            var first = WeightRecordId.generate();
            var second = WeightRecordId.generate();

            assertNotEquals(first, second);
            assertNotEquals(first.value(), second.value());
        }

        @Test
        void shouldGenerateValidUuidFormat() {
            var id = WeightRecordId.generate();

            assertDoesNotThrow(() -> UUID.fromString(id.value().toString()));
        }

    }

    @Nested
    class CreationFromUuid {

        @Test
        void shouldCreateOwnerIdFromUuid() {
            var uuid = UUID.randomUUID();

            var weightRecordId = WeightRecordId.of(uuid);

            assertEquals(uuid, weightRecordId.value());
        }

        @Test
        void shouldCreateOwnerIdFromSameUuidMultipleTimes() {
            var uuid = UUID.randomUUID();

            var ownerId1 = WeightRecordId.of(uuid);
            var ownerId2 = WeightRecordId.of(uuid);

            assertEquals(ownerId1, ownerId2);
            assertEquals(ownerId1.value(), ownerId2.value());
        }

        @Test
        void shouldThrowWhenUuidIsNull() {
            assertThrows(
                    NullPointerException.class,
                    () -> WeightRecordId.of((UUID) null)
            );
        }

    }

    @Nested
    class CreationFromString {

        @Test
        void shouldCreateOwnerIdFromString() {
            var uuid = UUID.randomUUID();

            var weightRecordId = WeightRecordId.of(uuid.toString());

            assertEquals(uuid, weightRecordId.value());
        }

        @Test
        void shouldCreateOwnerIdFromUppercaseString() {
            var uuid = UUID.randomUUID();
            var uppercaseString = uuid.toString().toUpperCase();

            var weightRecordId = WeightRecordId.of(uppercaseString);

            assertEquals(uuid, weightRecordId.value());
        }

        @Test
        void shouldCreateOwnerIdFromLowercaseString() {
            var uuid = UUID.randomUUID();
            var lowercaseString = uuid.toString().toLowerCase();

            var weightRecordId = WeightRecordId.of(lowercaseString);

            assertEquals(uuid, weightRecordId.value());
        }

        @Test
        void shouldCreateOwnerIdFromMixedCaseString() {
            var uuidString = "550e8400-e29b-41d4-a716-446655440000";

            var weightRecordId = WeightRecordId.of(uuidString);

            assertEquals(UUID.fromString(uuidString), weightRecordId.value());
        }

        @Test
        void shouldThrowWhenStringIsNull() {
            assertThrows(
                    NullPointerException.class,
                    () -> WeightRecordId.of((String) null)
            );
        }

        @Test
        void shouldThrowWhenStringIsNotValidUuid() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> WeightRecordId.of("invalid-uuid")
            );
        }

        @Test
        void shouldThrowWhenStringIsEmpty() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> WeightRecordId.of("")
            );
        }

        @Test
        void shouldThrowWhenStringHasWrongFormat() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> WeightRecordId.of("550e8400e29b41d4a716446655440000") // Missing hyphens
            );
        }

        @Test
        void shouldThrowWhenStringHasPartialUuid() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> WeightRecordId.of("550e8400-e29b-41d4-a716")
            );
        }

        @Test
        void shouldThrowWhenStringHasExtraCharacters() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> WeightRecordId.of("550e8400-e29b-41d4-a716-446655440000-extra")
            );
        }

    }

}
