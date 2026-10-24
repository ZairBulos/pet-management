package com.petmanagement.reminders.domain.model.valueobject;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NotificationIdTest {

    @Nested
    class Generation {

        @Test
        void shouldCreateNotificationId() {
            var id = NotificationId.generate();

            assertNotNull(id);
            assertNotNull(id.value());
        }

        @Test
        void shouldCreatesDifferentIdentifiers() {
            var first = NotificationId.generate();
            var second = NotificationId.generate();

            assertNotEquals(first, second);
            assertNotEquals(first.value(), second.value());
        }

        @Test
        void shouldGenerateValidUuidFormat() {
            var id = NotificationId.generate();

            assertDoesNotThrow(() -> UUID.fromString(id.value().toString()));
        }

    }

    @Nested
    class CreationFromUuid {

        @Test
        void shouldCreateNotificationIdFromUuid() {
            var uuid = UUID.randomUUID();

            var notificationId = NotificationId.of(uuid);

            assertEquals(uuid, notificationId.value());
        }

        @Test
        void shouldCreateNotificationIdFromSameUuidMultipleTimes() {
            var uuid = UUID.randomUUID();

            var ownerId1 = NotificationId.of(uuid);
            var ownerId2 = NotificationId.of(uuid);

            assertEquals(ownerId1, ownerId2);
            assertEquals(ownerId1.value(), ownerId2.value());
        }

        @Test
        void shouldThrowWhenUuidIsNull() {
            assertThrows(
                    NullPointerException.class,
                    () -> NotificationId.of((UUID) null)
            );
        }

    }

    @Nested
    class CreationFromString {

        @Test
        void shouldCreateNotificationIdFromString() {
            var uuid = UUID.randomUUID();

            var notificationId = NotificationId.of(uuid.toString());

            assertEquals(uuid, notificationId.value());
        }

        @Test
        void shouldCreateNotificationIdFromUppercaseString() {
            var uuid = UUID.randomUUID();
            var uppercaseString = uuid.toString().toUpperCase();

            var notificationId = NotificationId.of(uppercaseString);

            assertEquals(uuid, notificationId.value());
        }

        @Test
        void shouldCreateNotificationIdFromLowercaseString() {
            var uuid = UUID.randomUUID();
            var lowercaseString = uuid.toString().toLowerCase();

            var notificationId = NotificationId.of(lowercaseString);

            assertEquals(uuid, notificationId.value());
        }

        @Test
        void shouldCreateNotificationIdFromMixedCaseString() {
            var uuidString = "550e8400-e29b-41d4-a716-446655440000";

            var notificationId = NotificationId.of(uuidString);

            assertEquals(UUID.fromString(uuidString), notificationId.value());
        }

        @Test
        void shouldThrowWhenStringIsNull() {
            assertThrows(
                    NullPointerException.class,
                    () -> NotificationId.of((String) null)
            );
        }

        @Test
        void shouldThrowWhenStringIsNotValidUuid() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> NotificationId.of("invalid-uuid")
            );
        }

        @Test
        void shouldThrowWhenStringIsEmpty() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> NotificationId.of("")
            );
        }

        @Test
        void shouldThrowWhenStringHasWrongFormat() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> NotificationId.of("550e8400e29b41d4a716446655440000") // Missing hyphens
            );
        }

        @Test
        void shouldThrowWhenStringHasPartialUuid() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> NotificationId.of("550e8400-e29b-41d4-a716")
            );
        }

        @Test
        void shouldThrowWhenStringHasExtraCharacters() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> NotificationId.of("550e8400-e29b-41d4-a716-446655440000-extra")
            );
        }

    }

}
