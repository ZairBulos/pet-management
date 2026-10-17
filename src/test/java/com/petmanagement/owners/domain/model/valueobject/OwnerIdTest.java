package com.petmanagement.owners.domain.model.valueobject;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OwnerIdTest {

    @Nested
    class Generation {

        @Test
        void shouldCreateOwnerId() {
            var id = OwnerId.generate();

            assertNotNull(id);
            assertNotNull(id.value());
        }

        @Test
        void shouldCreatesDifferentIdentifiers() {
            var first = OwnerId.generate();
            var second = OwnerId.generate();

            assertNotEquals(first, second);
            assertNotEquals(first.value(), second.value());
        }

        @Test
        void shouldGenerateValidUuidFormat() {
            var id = OwnerId.generate();

            assertDoesNotThrow(() -> UUID.fromString(id.value().toString()));
        }

    }

    @Nested
    class CreationFromUuid {

        @Test
        void shouldCreateOwnerIdFromUuid() {
            var uuid = UUID.randomUUID();

            var ownerId = OwnerId.of(uuid);

            assertEquals(uuid, ownerId.value());
        }

        @Test
        void shouldCreateOwnerIdFromSameUuidMultipleTimes() {
            var uuid = UUID.randomUUID();

            var ownerId1 = OwnerId.of(uuid);
            var ownerId2 = OwnerId.of(uuid);

            assertEquals(ownerId1, ownerId2);
            assertEquals(ownerId1.value(), ownerId2.value());
        }

        @Test
        void shouldThrowWhenUuidIsNull() {
            assertThrows(NullPointerException.class, () -> OwnerId.of((UUID) null));
        }

    }

    @Nested
    class CreationFromString {

        @Test
        void shouldCreateOwnerIdFromString() {
            var uuid = UUID.randomUUID();

            var ownerId = OwnerId.of(uuid.toString());

            assertEquals(uuid, ownerId.value());
        }

        @Test
        void shouldCreateOwnerIdFromUppercaseString() {
            var uuid = UUID.randomUUID();
            var uppercaseString = uuid.toString().toUpperCase();

            var ownerId = OwnerId.of(uppercaseString);

            assertEquals(uuid, ownerId.value());
        }

        @Test
        void shouldCreateOwnerIdFromLowercaseString() {
            var uuid = UUID.randomUUID();
            var lowercaseString = uuid.toString().toLowerCase();

            var ownerId = OwnerId.of(lowercaseString);

            assertEquals(uuid, ownerId.value());
        }

        @Test
        void shouldCreateOwnerIdFromMixedCaseString() {
            var uuidString = "550e8400-e29b-41d4-a716-446655440000";

            var ownerId = OwnerId.of(uuidString);

            assertEquals(UUID.fromString(uuidString), ownerId.value());
        }

        @Test
        void shouldThrowWhenStringIsNull() {
            assertThrows(NullPointerException.class, () -> OwnerId.of((String) null));
        }

        @Test
        void shouldThrowWhenStringIsNotValidUuid() {
            assertThrows(IllegalArgumentException.class, () -> OwnerId.of("invalid-uuid"));
        }

        @Test
        void shouldThrowWhenStringIsEmpty() {
            assertThrows(IllegalArgumentException.class, () -> OwnerId.of(""));
        }

        @Test
        void shouldThrowWhenStringHasWrongFormat() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> OwnerId.of("550e8400e29b41d4a716446655440000") // Missing hyphens
            );
        }

        @Test
        void shouldThrowWhenStringHasPartialUuid() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> OwnerId.of("550e8400-e29b-41d4-a716")
            );
        }

        @Test
        void shouldThrowWhenStringHasExtraCharacters() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> OwnerId.of("550e8400-e29b-41d4-a716-446655440000-extra")
            );
        }

    }

}
