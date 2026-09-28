package com.petmanagement.auth.domain.model.valueobject;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class SessionExpiresAtTest {

    @Nested
    class Creation {

        @Test
        void shouldGenerateWithDefaultTTL() {
            // When
            var expiresAt = SessionExpiresAt.generate();

            // Then
            assertNotNull(expiresAt);
        }

        @Test
        void shouldGenerateWithCustomTTL() {
            // Given
            var customTTL = Duration.ofDays(14);

            // When
            var expiresAt = SessionExpiresAt.generate(customTTL);

            // Then
            assertNotNull(expiresAt);
        }

    }

    @Nested
    class Validation {

        @Test
        void shouldThrowWhenExpiresAtIsNull() {
            // When/Then
            assertThrows(
                    NullPointerException.class,
                    () -> new SessionExpiresAt(null)
            );
        }

        @Test
        void shouldThrowWhenTTLIsNull() {
            // When/Then
            assertThrows(
                    NullPointerException.class,
                    () -> SessionExpiresAt.generate(null)
            );
        }

    }

    @Nested
    class IsExpired {

        @Test
        void shouldNotBeExpired() {
            // Given
            var fixed = Instant.now();
            var expiresAt = new SessionExpiresAt(fixed.plus(Duration.ofHours(1)));

            // When
            var result = expiresAt.isExpired(fixed);

            // Then
            assertFalse(result);
        }

        @Test
        void shouldBeExpired() {
            // Given
            var fixed = Instant.now();
            var expiresAt = new SessionExpiresAt(fixed.minus(Duration.ofHours(1)));

            // When
            var result = expiresAt.isExpired(fixed);

            // Then
            assertTrue(result);
        }

    }

}
