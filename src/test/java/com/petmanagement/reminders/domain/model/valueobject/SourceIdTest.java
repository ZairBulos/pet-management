package com.petmanagement.reminders.domain.model.valueobject;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SourceIdTest {

    @Nested
    class CreationFromUuid {

        @Test
        void shouldCreateSourceIdFromUuid() {
            var uuid = UUID.randomUUID();
            var sourceId = SourceId.of(uuid);

            assertEquals(uuid, sourceId.value());
        }

        @Test
        void shouldThrowWhenUuidIsNull() {
            assertThrows(
                    NullPointerException.class,
                    () -> SourceId.of((UUID) null)
            );
        }

    }

    @Nested
    class CreationFromString {

        @Test
        void shouldCreateSourceIdFromString() {
            var uuid = UUID.randomUUID();
            var sourceId = SourceId.of(uuid.toString());

            assertEquals(uuid, sourceId.value());
        }

        @Test
        void shouldCreateSourceIdFromUppercaseString() {
            var uuid = UUID.randomUUID();
            var sourceId = SourceId.of(uuid.toString().toUpperCase());

            assertEquals(uuid, sourceId.value());
        }

        @Test
        void shouldCreateSourceIdFromLowercaseString() {
            var uuid = UUID.randomUUID();
            var sourceId = SourceId.of(uuid.toString().toLowerCase());

            assertEquals(uuid, sourceId.value());
        }

        @Test
        void shouldThrowWhenStringIsNull() {
            assertThrows(
                    NullPointerException.class,
                    () -> SourceId.of((String) null)
            );
        }

        @Test
        void shouldThrowWhenStringIsNotValidUuid() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> SourceId.of("invalid-uuid")
            );
        }

    }

}
