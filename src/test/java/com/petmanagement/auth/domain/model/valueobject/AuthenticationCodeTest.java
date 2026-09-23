package com.petmanagement.auth.domain.model.valueobject;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AuthenticationCodeTest {

    @Nested
    class Creation {

        @Test
        void shouldGenerateValidCode() {
            // When
            var code = AuthenticationCode.generate();

            // Then
            assertNotNull(code);
            assertEquals(6, code.value().length());
        }

        @Test
        void shouldGenerateUniqueCodes() {
            // When
            var code1 = AuthenticationCode.generate();
            var code2 = AuthenticationCode.generate();

            assertNotEquals(code1, code2);
            assertNotEquals(code1.value(), code2.value());
        }

    }

    @Nested
    class Validation {

        @Test
        void shouldThrowWhenCodeIsNull() {
            // When/Then
            assertThrows(
                    NullPointerException.class,
                    () -> new AuthenticationCode(null)
            );
        }

    }

}
