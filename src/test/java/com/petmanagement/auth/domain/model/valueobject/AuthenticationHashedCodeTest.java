package com.petmanagement.auth.domain.model.valueobject;

import com.petmanagement.auth.support.TestAuthenticationMother;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationHashedCodeTest {

    @Nested
    class Creation {

        @Test
        void shouldCreateHashedCode() {
            // Given
            var plainCode = TestAuthenticationMother.DEFAULT_PLAIN_CODE.value();
            var hasher = TestAuthenticationMother.DEFAULT_HASHER;

            // When
            var hashedCode = AuthenticationHashedCode.from(plainCode, hasher);

            // Then
            assertNotNull(hashedCode);
            assertEquals("hashed-" + plainCode, hashedCode.value());
        }

    }

    @Nested
    class Validation {

        @Test
        void shouldThrowWhenHashedCodeIsNull() {
            // When/Then
            assertThrows(
                    NullPointerException.class,
                    () -> new AuthenticationHashedCode(null)
            );
        }

        @Test
        void shouldThrowWhenPlainCodeIsNull() {
            // Given
            var hasher = TestAuthenticationMother.DEFAULT_HASHER;

            // When/Then
            assertThrows(
                    NullPointerException.class,
                    () -> AuthenticationHashedCode.from(null, hasher)
            );
        }

        @Test
        void shouldThrowWhenHasherIsNull() {
            // Given
            var plainCode = TestAuthenticationMother.DEFAULT_PLAIN_CODE.value();

            // When/Then
            assertThrows(
                    NullPointerException.class,
                    () -> AuthenticationHashedCode.from(plainCode, null)
            );
        }

        @Test
        void shouldThrowWhenPlainCodeIsNullOnMatch() {
            // Given
            var hashedCode = TestAuthenticationMother.DEFAULT_HASHED_CODE;
            var verifier = TestAuthenticationMother.DEFAULT_VERIFIER;

            // When/Then
            assertThrows(
                    NullPointerException.class,
                    () -> hashedCode.matches(null, verifier)
            );
        }

        @Test
        void shouldThrowWhenVerifierIsNull() {
            // Given
            var hashedCode = TestAuthenticationMother.DEFAULT_HASHED_CODE;
            var plainCode = TestAuthenticationMother.DEFAULT_PLAIN_CODE.value();

            // When/Then
            assertThrows(
                    NullPointerException.class,
                    () -> hashedCode.matches(plainCode, null)
            );
        }

    }

    @Nested
    class Matches {

        @Test
        void shouldMatchWhenPlainCodeIsValid() {
            // Given
            var hashedCode = TestAuthenticationMother.DEFAULT_HASHED_CODE;
            var plainCode = TestAuthenticationMother.DEFAULT_PLAIN_CODE.value();
            var verifier = TestAuthenticationMother.DEFAULT_VERIFIER;

            // When
            var result = hashedCode.matches(plainCode, verifier);

            // Then
            assertTrue(result);
        }

        @Test
        void shouldNotMatchWhenPlainCodeIsInvalid() {
            // Given
            var hashedCode = TestAuthenticationMother.DEFAULT_HASHED_CODE;
            var plainCode = TestAuthenticationMother.INVALID_PLAIN_CODE.value();
            var verifier = TestAuthenticationMother.DEFAULT_VERIFIER;

            // When
            var result = hashedCode.matches(plainCode, verifier);

            // Then
            assertFalse(result);
        }

    }

}
