package com.petmanagement.shared.domain.model;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PageRequestTest {

    @Nested
    class Creation {

        @Test
        void shouldCreatePageRequest() {
            var pageRequest = new PageRequest(0, 10);

            assertEquals(0, pageRequest.page());
            assertEquals(10, pageRequest.size());
        }

        @Test
        void shouldCreatePageRequestWithOf() {
            var pageRequest = PageRequest.of(1, 15);

            assertEquals(1, pageRequest.page());
            assertEquals(15, pageRequest.size());
        }

    }

    @Nested
    class Validation {

        @Test
        void shouldThrowWhenPageIsNegative() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> PageRequest.of(-1, 10)
            );
        }

        @ParameterizedTest
        @ValueSource(ints = {0, -1})
        void shouldThrowWhenSizeIsNotPositive(int size) {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> PageRequest.of(0, size)
            );
        }

    }

}
