package com.petmanagement.pets.domain.model.valueobject;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OwnerIdTest {

    @Nested
    class CreationFromUuid {

        @Test
        void shouldCreateOwnerIdFromUuid() {
            var uuid = UUID.randomUUID();
            var ownerId = OwnerId.of(uuid);

            assertEquals(uuid, ownerId.value());
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
            var ownerId = OwnerId.of(uuid.toString().toUpperCase());

            assertEquals(uuid, ownerId.value());
        }

        @Test
        void shouldCreateOwnerIdFromLowercaseString() {
            var uuid = UUID.randomUUID();
            var ownerId = OwnerId.of(uuid.toString().toLowerCase());

            assertEquals(uuid, ownerId.value());
        }

        @Test
        void shouldThrowWhenStringIsNull() {
            assertThrows(NullPointerException.class, () -> OwnerId.of((String) null));
        }

        @Test
        void shouldThrowWhenStringIsNotValidUuid() {
            assertThrows(IllegalArgumentException.class, () -> OwnerId.of("invalid-uuid"));
        }

    }

}
