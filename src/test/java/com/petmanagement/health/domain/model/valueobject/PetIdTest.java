package com.petmanagement.health.domain.model.valueobject;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PetIdTest {

    @Nested
    class CreationFromUuid {

        @Test
        void shouldCreateOwnerIdFromUuid() {
            var uuid = UUID.randomUUID();
            var petId = PetId.of(uuid);

            assertEquals(uuid, petId.value());
        }

        @Test
        void shouldThrowWhenUuidIsNull() {
            assertThrows(
                    NullPointerException.class,
                    () -> PetId.of((UUID) null)
            );
        }

    }

    @Nested
    class CreationFromString {

        @Test
        void shouldCreateOwnerIdFromString() {
            var uuid = UUID.randomUUID();
            var petId = PetId.of(uuid.toString());

            assertEquals(uuid, petId.value());
        }

        @Test
        void shouldCreateOwnerIdFromUppercaseString() {
            var uuid = UUID.randomUUID();
            var petId = PetId.of(uuid.toString().toUpperCase());

            assertEquals(uuid, petId.value());
        }

        @Test
        void shouldCreateOwnerIdFromLowercaseString() {
            var uuid = UUID.randomUUID();
            var petId = PetId.of(uuid.toString().toLowerCase());

            assertEquals(uuid, petId.value());
        }

        @Test
        void shouldThrowWhenStringIsNull() {
            assertThrows(
                    NullPointerException.class,
                    () -> PetId.of((String) null)
            );
        }

        @Test
        void shouldThrowWhenStringIsNotValidUuid() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> PetId.of("invalid-uuid")
            );
        }

    }

}
