package com.petmanagement.auth.domain.model.valueobject;

import com.petmanagement.auth.support.TestSessionMother;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SessionRefreshTokenTest {

    @Nested
    class Creation {

        @Test
        void shouldCreateRefreshToken() {
            // Given
            var value = TestSessionMother.DEFAULT_PLAIN_REFRESH_TOKEN.value();

            // When
            var refreshToken = new SessionRefreshToken(value);

            // Then
            assertNotNull(refreshToken);
            assertEquals(value, refreshToken.value());
        }

        @Test
        void shouldBeEqualWhenValuesAreEqual() {
            // Given
            var value = TestSessionMother.DEFAULT_PLAIN_REFRESH_TOKEN.value();

            // When
            var refreshToken1 = new SessionRefreshToken(value);
            var refreshToken2 = new SessionRefreshToken(value);

            // Then
            assertEquals(refreshToken1, refreshToken2);
            assertEquals(refreshToken1.hashCode(), refreshToken2.hashCode());
        }

        @Test
        void shouldNotBeEqualWhenValuesAreDifferent() {
            // When
            var refreshToken1 = TestSessionMother.DEFAULT_PLAIN_REFRESH_TOKEN;
            var refreshToken2 = TestSessionMother.ANOTHER_PLAIN_REFRESH_TOKEN;

            // Then
            assertNotEquals(refreshToken1, refreshToken2);
        }

    }

    @Nested
    class Validation {

        @Test
        void shouldThrowWhenRefreshTokenIsNull() {
            // When/Then
            assertThrows(
                    NullPointerException.class,
                    () -> new SessionRefreshToken(null)
            );
        }

    }

}
