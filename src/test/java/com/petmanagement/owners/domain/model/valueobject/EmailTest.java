package com.petmanagement.owners.domain.model.valueobject;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Nested
    class Creation {

        @Test
        void shouldCreateEmail() {
            var email = new Email("jane.doe@example.com");
            assertEquals("jane.doe@example.com", email.value());
        }

        @Test
        void shouldCreateEmailWithPlusSign() {
            var email = new Email("user+tag@example.com");
            assertEquals("user+tag@example.com", email.value());
        }

        @Test
        void shouldCreateEmailWithDotsInLocalPart() {
            var email = new Email("john.q.doe@example.com");
            assertEquals("john.q.doe@example.com", email.value());
        }

        @Test
        void shouldCreateEmailWithSubdomain() {
            var email = new Email("user@mail.example.com");
            assertEquals("user@mail.example.com", email.value());
        }

        @Test
        void shouldCreateEmailWithCountryCodeTLD() {
            var email = new Email("user@example.co.uk");
            assertEquals("user@example.co.uk", email.value());
        }

        @Test
        void shouldCreateEmailWithDashInDomain() {
            var email = new Email("user@my-domain.com");
            assertEquals("user@my-domain.com", email.value());
        }

        @Test
        void shouldNormalizeEmailToLowercase() {
            var email = new Email("Jane.Doe@Example.Com");
            assertEquals("jane.doe@example.com", email.value());
        }

        @Test
        void shouldTrimEmail() {
            var email = new Email("   jane.doe@example.com   ");
            assertEquals("jane.doe@example.com", email.value());
        }

    }

    @Nested
    class Validation {

        @Test
        void shouldThrowWhenValueIsNull() {
            assertThrows(NullPointerException.class, () -> new Email(null));
        }

        @Test
        void shouldThrowWhenValueIsEmpty() {
            assertThrows(IllegalArgumentException.class, () -> new Email(""));
        }

        @Test
        void shouldThrowWhenValueIsBlank() {
            assertThrows(IllegalArgumentException.class, () -> new Email("    "));
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "not-an-email",              // Missing @
                "missing-domain@",           // Missing domain
                "@missing-local.com",        // Missing local part
                "spaces in@email.com",       // Spaces in local part
                "user@localhost",            // No dot in domain
                "user@domain",               // No TLD
                "user@.com",                 // Empty domain before TLD
                "user@domain.",              // Dot at the end of domain
                "user@@domain.com",          // Duplicate @
                "user name@domain.com",      // Space in local part
                "user@domain name.com"       // Space in domain
        })
        void shouldThrowWhenMalformed(String email) {
            assertThrows(IllegalArgumentException.class, () -> new Email(email));
        }

        @Test
        void shouldThrowWhenEmailExceedsMaxLength() {
            var longEmail = "a".repeat(200) + "@" + "b".repeat(60) + ".com";
            assertThrows(IllegalArgumentException.class, () -> new Email(longEmail));
        }

    }

}
