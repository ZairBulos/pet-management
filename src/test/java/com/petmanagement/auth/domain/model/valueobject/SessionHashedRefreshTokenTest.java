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
            var refreshToken = TestSessionMother.DEFAULT_PLAIN_REFRESH_TOKEN;

            // When
            var hashedRefreshToken = SessionHashedRefreshToken.from(refreshToken);

            // Then
            assertNotNull(hashedRefreshToken);
            assertNotEquals(refreshToken.value(), hashedRefreshToken.value());
        }

        @Test
        void shouldGenerateSameHashForSameToken() {
            // Given
            var refreshToken = TestSessionMother.DEFAULT_PLAIN_REFRESH_TOKEN;

            // When
            var hashed1 = SessionHashedRefreshToken.from(refreshToken);
            var hashed2 = SessionHashedRefreshToken.from(refreshToken);

            // Then
            assertEquals(hashed1, hashed2);
        }

        @Test
        void shouldGenerateDifferentHashForDifferentTokens() {
            // When
            var hashed1 = SessionHashedRefreshToken.from(TestSessionMother.DEFAULT_PLAIN_REFRESH_TOKEN);
            var hashed2 = SessionHashedRefreshToken.from(TestSessionMother.ANOTHER_PLAIN_REFRESH_TOKEN);

            // Then
            assertNotEquals(hashed1, hashed2);
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
            // When/Then
            assertThrows(
                    NullPointerException.class,
                    () -> SessionHashedRefreshToken.from(null)
            );
        }

    }

}
