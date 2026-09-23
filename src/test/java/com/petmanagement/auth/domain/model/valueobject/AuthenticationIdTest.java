package com.petmanagement.auth.domain.model.valueobject;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthenticationIdTest {

    @Nested
    class Generation {

        @Test
        void shouldCreateAuthenticationId() {
            // When
            var id = AuthenticationId.generate();

            // Then
            assertNotNull(id);
            assertNotNull(id.value());
        }

        @Test
        void shouldCreatesDifferentIdentifiers() {
            // When
            var first = AuthenticationId.generate();
            var second = AuthenticationId.generate();

            // Then
            assertNotEquals(first, second);
            assertNotEquals(first.value(), second.value());
        }

        @Test
        void shouldGenerateValidUuidFormat() {
            // When
            var id = AuthenticationId.generate();

            // Then
            assertDoesNotThrow(() -> UUID.fromString(id.value().toString()));
        }

    }

    @Nested
    class CreationFromUuid {

        @Test
        void shouldCreateAuthenticationIdFromUuid() {
            // Given
            var uuid = UUID.randomUUID();

            // When
            var id = AuthenticationId.of(uuid);

            // Then
            assertEquals(uuid, id.value());
        }

        @Test
        void shouldCreateAuthenticationIdFromSameUuidMultipleTimes() {
            // Given
            var uuid = UUID.randomUUID();

            // When
            var id1 = AuthenticationId.of(uuid);
            var id2 = AuthenticationId.of(uuid);

            // Then
            assertEquals(id1, id2);
            assertEquals(id1.value(), id2.value());
        }

        @Test
        void shouldThrowWhenUuidIsNull() {
            // When/Then
            assertThrows(
                    NullPointerException.class,
                    () -> AuthenticationId.of((UUID) null)
            );
        }

    }

    @Nested
    class CreationFromString {

        @Test
        void shouldCreateAuthenticationIdFromString() {
            // Given
            var uuid = UUID.randomUUID();

            // When
            var id = AuthenticationId.of(uuid.toString());

            // Then
            assertEquals(uuid, id.value());
        }

        @Test
        void shouldCreateAuthenticationIdFromUppercaseString() {
            // Given
            var uuid = UUID.randomUUID();
            var uppercaseString = uuid.toString().toUpperCase();

            // When
            var id = AuthenticationId.of(uppercaseString);

            // Then
            assertEquals(uuid, id.value());
        }

        @Test
        void shouldCreateAuthenticationIdFromLowercaseString() {
            // Given
            var uuid = UUID.randomUUID();
            var lowercaseString = uuid.toString().toLowerCase();

            // When
            var id = AuthenticationId.of(lowercaseString);

            // Then
            assertEquals(uuid, id.value());
        }

        @Test
        void shouldCreateAuthenticationIdFromMixedCaseString() {
            // Given
            var uuidString = "550e8400-e29b-41d4-a716-446655440000";

            // When
            var id = AuthenticationId.of(uuidString);

            // Then
            assertEquals(UUID.fromString(uuidString), id.value());
        }

        @Test
        void shouldThrowWhenStringIsNull() {
            // When/Then
            assertThrows(
                    NullPointerException.class,
                    () -> AuthenticationId.of((String) null)
            );
        }

        @Test
        void shouldThrowWhenStringIsNotValidUuid() {
            // When/Then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> AuthenticationId.of("invalid-uuid")
            );
        }

        @Test
        void shouldThrowWhenStringIsEmpty() {
            // When/Then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> AuthenticationId.of("")
            );
        }

        @Test
        void shouldThrowWhenStringHasWrongFormat() {
            // When/Then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> AuthenticationId.of("550e8400e29b41d4a716446655440000") // Missing hyphens
            );
        }

        @Test
        void shouldThrowWhenStringHasPartialUuid() {
            // When/Then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> AuthenticationId.of("550e8400-e29b-41d4-a716")
            );
        }

        @Test
        void shouldThrowWhenStringHasExtraCharacters() {
            // When/Then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> AuthenticationId.of("550e8400-e29b-41d4-a716-446655440000-extra")
            );
        }

    }

}
