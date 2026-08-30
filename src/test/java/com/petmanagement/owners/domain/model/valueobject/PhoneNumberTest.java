package com.petmanagement.owners.domain.model.valueobject;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PhoneNumberTest {

    @Nested
    class Creation {

        @Test
        void shouldCreatePhoneNumber() {
            var phone = new PhoneNumber("+5492611234567");
            assertEquals("+5492611234567", phone.value());
        }

        @Test
        void shouldCreatePhoneNumberWithoutPlus() {
            var phone = new PhoneNumber("5492611234567");
            assertEquals("5492611234567", phone.value());
        }

        @Test
        void shouldCreatePhoneNumberWithExactly7Digits() {
            var phone = new PhoneNumber("1234567");
            assertEquals("1234567", phone.value());
        }

        @Test
        void shouldCreatePhoneNumberWithExactly15Digits() {
            var phone = new PhoneNumber("123456789012345");
            assertEquals("123456789012345", phone.value());
        }

        @Test
        void shouldCreatePhoneNumberWith8Digits() {
            var phone = new PhoneNumber("12345678");
            assertEquals("12345678", phone.value());
        }

        @Test
        void shouldCreatePhoneNumberWith14Digits() {
            var phone = new PhoneNumber("12345678901234");
            assertEquals("12345678901234", phone.value());
        }

        @Test
        void shouldNormalizePhoneByRemovingSpaces() {
            var phone = new PhoneNumber("+54 9 261 123 4567");
            assertEquals("+5492611234567", phone.value());
        }

        @Test
        void shouldNormalizePhoneByRemovingHyphens() {
            var phone = new PhoneNumber("+54-9-261-123-4567");
            assertEquals("+5492611234567", phone.value());
        }

        @Test
        void shouldNormalizePhoneByRemovingParentheses() {
            var phone = new PhoneNumber("+1 (555) 123-4567");
            assertEquals("+15551234567", phone.value());
        }

        @Test
        void shouldNormalizePhoneWithAllFormattingCharacters() {
            var phone = new PhoneNumber("   +1 (555) 123-4567   ");
            assertEquals("+15551234567", phone.value());
        }

        @Test
        void shouldNormalizePhoneByRemovingDots() {
            var phone = new PhoneNumber("123.456.7890");
            assertEquals("1234567890", phone.value());
        }

        @Test
        void shouldAcceptPhoneWithPlusAtStart() {
            var phone = new PhoneNumber("+1234567");
            assertEquals("+1234567", phone.value());
        }

        @Test
        void shouldNormalizePhoneWithMultipleSpaces() {
            var phone = new PhoneNumber("+1   555   123   4567");
            assertEquals("+15551234567", phone.value());
        }

        @Test
        void shouldNormalizePhoneWithMixedFormatting() {
            var phone = new PhoneNumber("+34 (9) 611-234-567");
            assertEquals("+349611234567", phone.value());
        }

    }

    @Nested
    class Validation {

        @Test
        void shouldThrowWhenValueIsNull() {
            assertThrows(NullPointerException.class, () -> new PhoneNumber(null));
        }

        @Test
        void shouldThrowWhenValueIsEmpty() {
            assertThrows(IllegalArgumentException.class, () -> new PhoneNumber(""));
        }

        @Test
        void shouldThrowWhenValueIsBlank() {
            assertThrows(IllegalArgumentException.class, () -> new PhoneNumber("    "));
        }

        @Test
        void shouldThrowWhenValueHasOnlySpecialCharacters() {
            assertThrows(IllegalArgumentException.class, () -> new PhoneNumber("+-() ."));
        }

        @Test
        void shouldThrowWhenPhoneHasTooFewDigits() {
            assertThrows(IllegalArgumentException.class, () -> new PhoneNumber("123456"));
        }

        @Test
        void shouldThrowWhenPhoneHasTooManyDigits() {
            assertThrows(IllegalArgumentException.class, () -> new PhoneNumber("1234567890123456"));
        }

        @Test
        void shouldThrowWhenPhoneContainsLetters() {
            assertThrows(IllegalArgumentException.class, () -> new PhoneNumber("+549261ABC4567"));
        }

        @Test
        void shouldThrowWhenPhoneContainsSpecialCharactersNotAllowed() {
            assertThrows(IllegalArgumentException.class, () -> new PhoneNumber("+549261123456!"));
        }

        @Test
        void shouldThrowWhenPhoneIsOnlyLetters() {
            assertThrows(IllegalArgumentException.class, () -> new PhoneNumber("abc1234567"));
        }

        @Test
        void shouldThrowWhenPhoneHasExactly6Digits() {
            assertThrows(IllegalArgumentException.class, () -> new PhoneNumber("123456"));
        }

        @Test
        void shouldThrowWhenPhoneHasExactly16Digits() {
            assertThrows(IllegalArgumentException.class, () -> new PhoneNumber("1234567890123456"));
        }

        @Test
        void shouldThrowWhenPhoneHasPlusAndTooFewDigits() {
            assertThrows(IllegalArgumentException.class, () -> new PhoneNumber("+123456"));
        }

        @Test
        void shouldThrowWhenPhoneHasPlusAndTooManyDigits() {
            assertThrows(IllegalArgumentException.class, () -> new PhoneNumber("+1234567890123456"));
        }

        @Test
        void shouldThrowWhenPhoneWithFormattingHasTooFewDigits() {
            assertThrows(IllegalArgumentException.class, () -> new PhoneNumber("12-3-4-5-6"));
        }

        @Test
        void shouldThrowWhenPhoneWithFormattingHasTooManyDigits() {
            assertThrows(IllegalArgumentException.class, () -> new PhoneNumber("1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6"));
        }

    }

}
