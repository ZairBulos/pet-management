package com.petmanagement.shared.domain.model;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PageResponseTest {

    @Nested
    class Creation {

        @Test
        void shouldCreatePageResponse() {
            var response = new PageResponse<>(
                    List.of("item1", "item2"), 0, 10, 2, 1
            );

            assertEquals(List.of("item1", "item2"), response.content());
            assertEquals(0, response.page());
            assertEquals(10, response.size());
            assertEquals(2, response.totalElements());
            assertEquals(1, response.totalPages());
        }

        @Test
        void shouldCreateWithOf() {
            var response = PageResponse.of(List.of("a", "b"), 0, 10, 25);

            assertEquals(List.of("a", "b"), response.content());
            assertEquals(0, response.page());
            assertEquals(10, response.size());
            assertEquals(25, response.totalElements());
            assertEquals(3, response.totalPages());
        }

        @Test
        void shouldCreateEmpty() {
            var response = PageResponse.empty(0, 10);

            assertEquals(List.of(), response.content());
            assertEquals(0, response.page());
            assertEquals(10, response.size());
            assertEquals(0, response.totalElements());
            assertEquals(0, response.totalPages());
        }

        @Test
        void shouldMakeCopyOfContent() {
            var originalList = new java.util.ArrayList<>(List.of("a", "b"));
            var response = new PageResponse<>(originalList, 0, 10, 2, 1);

            originalList.clear();

            assertEquals(List.of("a", "b"), response.content());
        }

    }

    @Nested
    class Validation {

        @Test
        void shouldThrowWhenContentIsNull() {
            assertThrows(
                    NullPointerException.class,
                    () -> new PageResponse<>(null, 0, 10, 0, 0)
            );
        }

        @Test
        void shouldThrowWhenPageIsNegative() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new PageResponse<>(List.of(), -1, 10, 0, 0)
            );
        }

        @ParameterizedTest
        @ValueSource(ints = {0, -1})
        void shouldThrowWhenSizeIsNotPositive(int size) {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new PageResponse<>(List.of(), 0, size, 0, 0)
            );
        }

        @Test
        void shouldThrowWhenTotalElementsIsNegative() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new PageResponse<>(List.of(), 0, 10, -1, 0)
            );
        }

    }

    @Nested
    class Factory {

        @Test
        void shouldCalculateTotalPages() {
            var response = PageResponse.of(List.of(), 0, 10, 25);

            assertEquals(3, response.totalPages());
        }

        @Test
        void shouldCalculateTotalPagesForExactDivision() {
            var response = PageResponse.of(List.of(), 0, 10, 100);

            assertEquals(10, response.totalPages());
        }

        @Test
        void shouldCalculateZeroPagesForEmptyResult() {
            var response = PageResponse.of(List.of(), 0, 10, 0);

            assertEquals(0, response.totalPages());
        }

        @Test
        void shouldCalculateTotalPagesWithRemainder() {
            var response = PageResponse.of(List.of(), 0, 10, 101);

            assertEquals(11, response.totalPages());
        }

    }

    @Nested
    class Content {

        @Test
        void shouldReturnNumberOfElements() {
            var response = PageResponse.of(List.of("a", "b", "c"), 0, 10, 25);

            assertEquals(3, response.numberOfElements());
        }

        @Test
        void shouldReturnZeroNumberOfElementsWhenEmpty() {
            var response = PageResponse.of(List.of(), 0, 10, 0);

            assertEquals(0, response.numberOfElements());
        }

        @Test
        void shouldBeEmptyWhenContentIsEmpty() {
            var response = PageResponse.empty(0, 10);

            assertTrue(response.isEmpty());
        }

        @Test
        void shouldNotBeEmptyWhenContentHasElements() {
            var response = PageResponse.of(List.of("a"), 0, 10, 1);

            assertFalse(response.isEmpty());
        }

    }

    @Nested
    class Navigation {

        @Test
        void shouldHaveNextWhenNotOnLast() {
            var response = PageResponse.of(List.of(), 0, 10, 100);

            assertTrue(response.hasNext());
        }

        @Test
        void shouldNotHaveNextWhenOnLast() {
            var response = PageResponse.of(List.of(), 9, 10, 100);

            assertFalse(response.hasNext());
        }

        @Test
        void shouldNotHaveNextWhenOnlyOnePage() {
            var response = PageResponse.of(List.of("a"), 0, 10, 1);

            assertFalse(response.hasNext());
        }

        @Test
        void shouldHavePreviousWhenNotOnFirst() {
            var response = PageResponse.of(List.of(), 1, 10, 100);

            assertTrue(response.hasPrevious());
        }

        @Test
        void shouldNotHavePreviousWhenOnFirst() {
            var response = PageResponse.of(List.of(), 0, 10, 100);

            assertFalse(response.hasPrevious());
        }

        @Test
        void shouldBeFirstWhenPageIsZero() {
            var response = PageResponse.of(List.of(), 0, 10, 100);

            assertTrue(response.isFirst());
        }

        @Test
        void shouldNotBeFirstWhenPageIsGreaterThanZero() {
            var response = PageResponse.of(List.of(), 1, 10, 100);

            assertFalse(response.isFirst());
        }

        @Test
        void shouldBeLastWhenPageEqualsLastIndex() {
            var response = PageResponse.of(List.of(), 9, 10, 100);

            assertTrue(response.isLast());
        }

        @Test
        void shouldNotBeLastWhenPageIsNotLastIndex() {
            var response = PageResponse.of(List.of(), 8, 10, 100);

            assertFalse(response.isLast());
        }

        @Test
        void shouldBeLastWhenEmptyResult() {
            var response = PageResponse.of(List.of(), 0, 10, 0);

            assertTrue(response.isLast());
        }

    }

}
