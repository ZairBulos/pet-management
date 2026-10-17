package com.petmanagement.owners.domain.model.valueobject;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OwnerNameTest {

    @Nested
    class Creation {

        @Test
        void shouldCreateOwnerName() {
            var name = new OwnerName("Jane Doe");
            assertEquals("Jane Doe", name.value());
        }

        @Test
        void shouldCreateNameWithExactly2Characters() {
            var name = new OwnerName("Jo");
            assertEquals("Jo", name.value());
        }

        @Test
        void shouldCreateNameWithNumbers() {
            var name = new OwnerName("Jane Doe 123");
            assertEquals("Jane Doe 123", name.value());
        }

        @Test
        void shouldCreateNameWithSpecialCharacters() {
            var name = new OwnerName("Juan O'Brien");
            assertEquals("Juan O'Brien", name.value());
        }

        @Test
        void shouldCreateNameWithHyphens() {
            var name = new OwnerName("María-José García-López");
            assertEquals("María-José García-López", name.value());
        }

        @Test
        void shouldCreateNameWithAccents() {
            var name = new OwnerName("José María Pérez Álvarez");
            assertEquals("José María Pérez Álvarez", name.value());
        }

        @Test
        void shouldCreateNameWithUnicodeCharacters() {
            var name = new OwnerName("李明 (Li Ming)");
            assertEquals("李明 (Li Ming)", name.value());
        }

        @Test
        void shouldTrimName() {
            var name = new OwnerName("   Jane Doe   ");
            assertEquals("Jane Doe", name.value());
        }

        @Test
        void shouldTrimMultipleWhitespaces() {
            var name = new OwnerName("\t\n   Jane Doe   \n\t");
            assertEquals("Jane Doe", name.value());
        }

        @Test
        void shouldPreserveInternalWhitespaces() {
            var name = new OwnerName("Jane   Marie   Doe");
            assertEquals("Jane   Marie   Doe", name.value());
        }

        @Test
        void shouldAcceptNameWithExactly150Characters() {
            var value = "a".repeat(150);
            var name = new OwnerName(value);
            assertEquals(value, name.value());
        }

    }

    @Nested
    class Validation {

        @Test
        void shouldThrowWhenValueIsNull() {
            assertThrows(NullPointerException.class, () -> new OwnerName(null));
        }

        @Test
        void shouldThrowWhenValueIsEmpty() {
            assertThrows(IllegalArgumentException.class, () -> new OwnerName(""));
        }

        @Test
        void shouldThrowWhenValueIsBlank() {
            assertThrows(IllegalArgumentException.class, () -> new OwnerName("    "));
        }

        @Test
        void shouldThrowWhenValueIsOnlyWhitespaceCharacters() {
            assertThrows(IllegalArgumentException.class, () -> new OwnerName("\t\n  \r"));
        }

        @Test
        void shouldThrowWhenNameIsSingleCharacter() {
            assertThrows(IllegalArgumentException.class, () -> new OwnerName("J"));
        }

        @Test
        void shouldThrowWhenNameExceedsMaximumLength() {
            var value = "a".repeat(151);
            assertThrows(IllegalArgumentException.class, () -> new OwnerName(value));
        }

    }

}
