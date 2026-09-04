package com.petmanagement.owners.domain.model.aggregate;

import com.petmanagement.owners.domain.model.valueobject.Email;
import com.petmanagement.owners.domain.model.valueobject.OwnerId;
import com.petmanagement.owners.domain.model.valueobject.OwnerName;
import com.petmanagement.owners.domain.model.valueobject.PhoneNumber;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class OwnerTest {

    private static final OwnerName NAME = new OwnerName("Jane Doe");
    private static final Email EMAIL = new Email("jane.doe@example.com");
    private static final PhoneNumber PHONE = new PhoneNumber("+5492611234567");

    @Nested
    class Creation {

        @Test
        void shouldCreateOwner() {
            var owner = Owner.create(NAME, EMAIL, PHONE);

            assertNotNull(owner.getId());
            assertEquals(NAME, owner.getName());
            assertEquals(EMAIL, owner.getEmail());
            assertEquals(PHONE, owner.getPhone());
            assertNotNull(owner.getCreatedAt());
            assertNotNull(owner.getUpdatedAt());
        }

        @Test
        void shouldGenerateUniqueIdForEachOwner() {
            var owner1 = Owner.create(NAME, EMAIL, PHONE);
            var owner2 = Owner.create(NAME, EMAIL, PHONE);

            assertNotEquals(owner1.getId(), owner2.getId());
        }

        @Test
        void shouldHaveSameCreatedAndUpdatedAtOnCreation() {
            var owner = Owner.create(NAME, EMAIL, PHONE);

            assertEquals(owner.getCreatedAt(), owner.getUpdatedAt());
        }

        @Test
        void shouldThrowWhenCreatingWithNullName() {
            assertThrows(
                    NullPointerException.class,
                    () -> Owner.create(null, EMAIL, PHONE)
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullEmail() {
            assertThrows(
                    NullPointerException.class,
                    () -> Owner.create(NAME, null, PHONE)
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullPhone() {
            assertThrows(
                    NullPointerException.class,
                    () -> Owner.create(NAME, EMAIL, null)
            );
        }

    }

    @Nested
    class Reconstitution {

        @Test
        void shouldReconstituteOwner() {
            var id = OwnerId.generate();
            var createdAt = Instant.parse("2024-01-15T10:00:00Z");
            var updatedAt = Instant.parse("2024-01-20T15:30:00Z");

            var owner = Owner.reconstitute(id, NAME, EMAIL, PHONE, createdAt, updatedAt);

            assertEquals(id, owner.getId());
            assertEquals(NAME, owner.getName());
            assertEquals(EMAIL, owner.getEmail());
            assertEquals(PHONE, owner.getPhone());
            assertEquals(createdAt, owner.getCreatedAt());
            assertEquals(updatedAt, owner.getUpdatedAt());
        }

        @Test
        void shouldThrowWhenReconstituteWithNullId() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Owner.reconstitute(null, NAME, EMAIL, PHONE, now, now)
            );
        }

    }

    @Nested
    class Rename {

        @Test
        void shouldRenameOwner() {
            var owner = Owner.create(NAME, EMAIL, PHONE);
            var newName = new OwnerName("John Doe");

            owner.rename(newName);

            assertEquals(newName, owner.getName());
        }

        @Test
        void shouldNotUpdateWithSameName() {
            var owner = Owner.create(NAME, EMAIL, PHONE);
            var originalUpdatedAt = owner.getUpdatedAt();

            owner.rename(NAME);

            assertEquals(originalUpdatedAt, owner.getUpdatedAt());
        }

        @Test
        void shouldThrowWhenRenamingWithNullName() {
            var owner = Owner.create(NAME, EMAIL, PHONE);

            assertThrows(
                    NullPointerException.class, () -> owner.rename(null)
            );
        }

    }

    @Nested
    class UpdateEmail {

        @Test
        void shouldUpdateEmail() {
            var owner = Owner.create(NAME, EMAIL, PHONE);
            var newEmail = new Email("john.doe@example.com");

            owner.updateEmail(newEmail);

            assertEquals(newEmail, owner.getEmail());
        }

        @Test
        void shouldNotUpdateWithSameEmail() {
            var owner = Owner.create(NAME, EMAIL, PHONE);
            var originalUpdatedAt = owner.getUpdatedAt();

            owner.updateEmail(EMAIL);

            assertEquals(originalUpdatedAt, owner.getUpdatedAt());
        }

        @Test
        void shouldThrowWhenUpdatingWithNullEmail() {
            var owner = Owner.create(NAME, EMAIL, PHONE);

            assertThrows(
                    NullPointerException.class,
                    () -> owner.updateEmail(null)
            );
        }

    }

    @Nested
    class UpdatePhone {

        @Test
        void shouldUpdatePhone() {
            var owner = Owner.create(NAME, EMAIL, PHONE);
            var newPhone = new PhoneNumber("+5492619876543");

            owner.updatePhone(newPhone);

            assertEquals(newPhone, owner.getPhone());
        }

        @Test
        void shouldNotUpdateWithSamePhone() {
            var owner = Owner.create(NAME, EMAIL, PHONE);
            var originalUpdatedAt = owner.getUpdatedAt();

            owner.updatePhone(PHONE);

            assertEquals(originalUpdatedAt, owner.getUpdatedAt());
        }

        @Test
        void shouldThrowWhenUpdatingWithNullPhone() {
            var owner = Owner.create(NAME, EMAIL, PHONE);

            assertThrows(
                    NullPointerException.class,
                    () -> owner.updatePhone(null)
            );
        }

    }

}
