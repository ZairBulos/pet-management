package com.petmanagement.shared.domain.model;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    private static final String ERROR = "UNAUTHORIZED";
    private static final String MESSAGE = "Authentication is required to access this resource";
    private static final String PATH = "/api/pets";
    private static final List<String> DETAILS = List.of("name must not be blank", "birthDate must be in the past");

    @Nested
    class Creation {

        @Test
        void shouldCreateErrorResponseWithoutDetails() {
            // When
            var response = ErrorResponse.of(ERROR, MESSAGE, PATH);

            // Then
            assertEquals(ERROR, response.error());
            assertEquals(MESSAGE, response.message());
            assertEquals(PATH, response.path());
            assertNull(response.details());
            assertNotNull(response.timestamp());
        }

        @Test
        void shouldCreateErrorResponseWithDetails() {
            // When
            var response = ErrorResponse.of(ERROR, MESSAGE, DETAILS, PATH);

            // Then
            assertEquals(ERROR, response.error());
            assertEquals(MESSAGE, response.message());
            assertEquals(DETAILS, response.details());
            assertEquals(PATH, response.path());
            assertNotNull(response.timestamp());
        }

    }

    @Nested
    class Validation {

        @Test
        void shouldThrowWhenErrorIsNull() {
            // When/Then
            assertThrows(
                    NullPointerException.class,
                    () -> ErrorResponse.of(null, MESSAGE, PATH)
            );
        }

        @Test
        void shouldThrowWhenMessageIsNull() {
            // When/Then
            assertThrows(
                    NullPointerException.class,
                    () -> ErrorResponse.of(ERROR, null, PATH)
            );
        }

        @Test
        void shouldThrowWhenPathIsNull() {
            // When/Then
            assertThrows(
                    NullPointerException.class,
                    () -> ErrorResponse.of(ERROR, MESSAGE, null)
            );
        }

        @Test
        void shouldThrowWhenTimestampIsNull() {
            // When/Then
            assertThrows(
                    NullPointerException.class,
                    () -> new ErrorResponse(ERROR, MESSAGE, DETAILS, null, PATH)
            );
        }

    }

}
