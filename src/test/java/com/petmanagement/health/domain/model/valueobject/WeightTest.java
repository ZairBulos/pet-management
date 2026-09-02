package com.petmanagement.health.domain.model.valueobject;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WeightTest {

    @Nested
    class Creation {

        @Test
        void shouldCreateWeightWithBigDecimal() {
            var weight = new Weight(BigDecimal.valueOf(25.5));

            assertEquals(new BigDecimal("25.50"), weight.value());
        }

        @Test
        void shouldCreateWeightWithMinimalValue() {
            var weight = new Weight(BigDecimal.valueOf(0.01));

            assertEquals(new BigDecimal("0.01"), weight.value());
        }

        @Test
        void shouldCreateWeightWithLargeValue() {
            var weight = new Weight(BigDecimal.valueOf(9999.99));

            assertEquals(new BigDecimal("9999.99"), weight.value());
        }

        @Test
        void shouldNormalizeIntegerValueTo2Decimals() {
            var weight = new Weight(BigDecimal.valueOf(25));

            assertEquals(new BigDecimal("25.00"), weight.value());
        }

        @Test
        void shouldNormalizeScaleTo2Decimals() {
            var weight = new Weight(BigDecimal.valueOf(25.567));

            assertEquals(new BigDecimal("25.57"), weight.value());
        }

        @Test
        void shouldRoundHalfUp() {
            var weight = new Weight(BigDecimal.valueOf(25.555));

            assertEquals(new BigDecimal("25.56"), weight.value());
        }

        @Test
        void shouldRoundDownWhenThirdDecimalIsLessThan5() {
            var weight = new Weight(BigDecimal.valueOf(25.554));

            assertEquals(new BigDecimal("25.55"), weight.value());
        }

    }

    @Nested
    class OfBigDecimal {

        @Test
        void shouldCreateWeightUsingBigDecimalFactoryMethod() {
            var weight = Weight.of(BigDecimal.valueOf(25.5));

            assertEquals(new BigDecimal("25.50"), weight.value());
        }

    }

    @Nested
    class OfDouble {

        @Test
        void shouldCreateWeightUsingDoubleFactoryMethod() {
            var weight = Weight.of(25.5);

            assertEquals(new BigDecimal("25.50"), weight.value());
        }

    }

    @Nested
    class Validation {

        @Test
        void shouldThrowWhenValueIsNull() {
            assertThrows(
                    NullPointerException.class, () -> new Weight(null)
            );
        }

        @Test
        void shouldThrowWhenValueIsZero() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new Weight(BigDecimal.ZERO)
            );
        }

        @Test
        void shouldThrowWhenValueIsNegative() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new Weight(BigDecimal.valueOf(-25.5))
            );
        }

        @Test
        void shouldThrowWhenValueIsNegativeWithSmallMagnitude() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new Weight(BigDecimal.valueOf(-0.01))
            );
        }

        @Test
        void shouldThrowWhenValueRoundsToZero() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new Weight(new BigDecimal("0.001"))
            );
        }

        @Test
        void shouldThrowWhenNegativeValueRoundsToZero() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new Weight(new BigDecimal("-0.001"))
            );
        }

    }

}
