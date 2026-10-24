package com.petmanagement.reminders.domain.model.valueobject;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReminderIdTest {

    @Nested
    class Generation {

        @Test
        void shouldCreateReminderId() {
            var id = ReminderId.generate();

            assertNotNull(id);
            assertNotNull(id.value());
        }

        @Test
        void shouldCreatesDifferentIdentifiers() {
            var first = ReminderId.generate();
            var second = ReminderId.generate();

            assertNotEquals(first, second);
            assertNotEquals(first.value(), second.value());
        }

        @Test
        void shouldGenerateValidUuidFormat() {
            var id = ReminderId.generate();

            assertDoesNotThrow(() -> UUID.fromString(id.value().toString()));
        }

    }

    @Nested
    class CreationFromUuid {

        @Test
        void shouldCreateReminderIdFromUuid() {
            var uuid = UUID.randomUUID();

            var reminderId = ReminderId.of(uuid);

            assertEquals(uuid, reminderId.value());
        }

        @Test
        void shouldCreateReminderIdFromSameUuidMultipleTimes() {
            var uuid = UUID.randomUUID();

            var ownerId1 = ReminderId.of(uuid);
            var ownerId2 = ReminderId.of(uuid);

            assertEquals(ownerId1, ownerId2);
            assertEquals(ownerId1.value(), ownerId2.value());
        }

        @Test
        void shouldThrowWhenUuidIsNull() {
            assertThrows(
                    NullPointerException.class,
                    () -> ReminderId.of((UUID) null)
            );
        }

    }

    @Nested
    class CreationFromString {

        @Test
        void shouldCreateReminderIdFromString() {
            var uuid = UUID.randomUUID();

            var reminderId = ReminderId.of(uuid.toString());

            assertEquals(uuid, reminderId.value());
        }

        @Test
        void shouldCreateReminderIdFromUppercaseString() {
            var uuid = UUID.randomUUID();
            var uppercaseString = uuid.toString().toUpperCase();

            var reminderId = ReminderId.of(uppercaseString);

            assertEquals(uuid, reminderId.value());
        }

        @Test
        void shouldCreateReminderIdFromLowercaseString() {
            var uuid = UUID.randomUUID();
            var lowercaseString = uuid.toString().toLowerCase();

            var reminderId = ReminderId.of(lowercaseString);

            assertEquals(uuid, reminderId.value());
        }

        @Test
        void shouldCreateReminderIdFromMixedCaseString() {
            var uuidString = "550e8400-e29b-41d4-a716-446655440000";

            var reminderId = ReminderId.of(uuidString);

            assertEquals(UUID.fromString(uuidString), reminderId.value());
        }

        @Test
        void shouldThrowWhenStringIsNull() {
            assertThrows(
                    NullPointerException.class,
                    () -> ReminderId.of((String) null)
            );
        }

        @Test
        void shouldThrowWhenStringIsNotValidUuid() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> ReminderId.of("invalid-uuid")
            );
        }

        @Test
        void shouldThrowWhenStringIsEmpty() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> ReminderId.of("")
            );
        }

        @Test
        void shouldThrowWhenStringHasWrongFormat() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> ReminderId.of("550e8400e29b41d4a716446655440000") // Missing hyphens
            );
        }

        @Test
        void shouldThrowWhenStringHasPartialUuid() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> ReminderId.of("550e8400-e29b-41d4-a716")
            );
        }

        @Test
        void shouldThrowWhenStringHasExtraCharacters() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> ReminderId.of("550e8400-e29b-41d4-a716-446655440000-extra")
            );
        }

    }
    
}
