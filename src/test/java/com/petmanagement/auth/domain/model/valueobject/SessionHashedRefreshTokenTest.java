package com.petmanagement.auth.domain.model.valueobject;

import com.petmanagement.auth.support.TestSessionMother;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SessionHashedRefreshTokenTest {

    @Nested
    class Creation {

        @Test
        void shouldCreateHashedRefreshToken() {
            // Given
            var plainToken = TestSessionMother.DEFAULT_PLAIN_REFRESH_TOKEN.value();
            var hasher = TestSessionMother.DEFAULT_HASHER;

            // When
            var hashedRefreshToken = SessionHashedRefreshToken.from(plainToken, hasher);

            // Then
            assertNotNull(hashedRefreshToken);
            assertEquals("hashed-" + plainToken, hashedRefreshToken.value());
        }

        @Test
        void shouldGenerateSameHashForSameToken() {
            // Given
            var plainToken = TestSessionMother.DEFAULT_PLAIN_REFRESH_TOKEN.value();
            var hasher = TestSessionMother.DEFAULT_HASHER;

            // When
            var hashed1 = SessionHashedRefreshToken.from(plainToken, hasher);
            var hashed2 = SessionHashedRefreshToken.from(plainToken, hasher);

            // Then
            assertEquals(hashed1, hashed2);
        }

        @Test
        void shouldGenerateDifferentHashForDifferentTokens() {
            // Given
            var hasher = TestSessionMother.DEFAULT_HASHER;

            // When
            var hashed1 = SessionHashedRefreshToken.from(
                    TestSessionMother.DEFAULT_PLAIN_REFRESH_TOKEN.value(), hasher);
            var hashed2 = SessionHashedRefreshToken.from(
                    TestSessionMother.ANOTHER_PLAIN_REFRESH_TOKEN.value(), hasher);

            // Then
            assertNotEquals(hashed1, hashed2);
        }

        @Test
        void shouldMatchMotherHashedRefreshToken() {
            // Given
            var plainToken = TestSessionMother.DEFAULT_PLAIN_REFRESH_TOKEN.value();
            var hasher = TestSessionMother.DEFAULT_HASHER;

            // When
            var hashedRefreshToken = SessionHashedRefreshToken.from(plainToken, hasher);

            // Then
            assertEquals(TestSessionMother.DEFAULT_HASHED_REFRESH_TOKEN, hashedRefreshToken);
        }

    }

    @Nested
    class Validation {

        @Test
        void shouldThrowWhenHashedRefreshTokenIsNull() {
            // When/Then
            assertThrows(
                    NullPointerException.class,
                    () -> new SessionHashedRefreshToken(null)
            );
        }

        @Test
        void shouldThrowWhenRefreshTokenIsNull() {
            // Given
            var hasher = TestSessionMother.DEFAULT_HASHER;

            // When/Then
            assertThrows(
                    NullPointerException.class,
                    () -> SessionHashedRefreshToken.from(null, hasher)
            );
        }

        @Test
        void shouldThrowWhenHasherIsNull() {
            // Given
            var plainToken = TestSessionMother.DEFAULT_PLAIN_REFRESH_TOKEN.value();

            // When/Then
            assertThrows(
                    NullPointerException.class,
                    () -> SessionHashedRefreshToken.from(plainToken, null)
            );
        }

    }

}
