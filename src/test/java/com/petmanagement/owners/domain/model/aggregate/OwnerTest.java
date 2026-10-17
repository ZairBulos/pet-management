package com.petmanagement.owners.domain.model.aggregate;

import com.petmanagement.owners.domain.model.valueobject.OwnerId;
import com.petmanagement.owners.support.OwnerTestBuilder;
import com.petmanagement.owners.support.TestOwnerMother;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class OwnerTest {

    @Nested
    class Creation {

        @Test
        void shouldCreateOwner() {
            var owner = OwnerTestBuilder.aOwner().build();

            assertNotNull(owner.getId());
            assertEquals(TestOwnerMother.OWNER_NAME_JOHN, owner.getName());
            assertEquals(TestOwnerMother.EMAIL_JOHN, owner.getEmail());
            assertEquals(TestOwnerMother.PHONE_JOHN, owner.getPhone());
            assertNotNull(owner.getCreatedAt());
            assertNotNull(owner.getUpdatedAt());
        }

        @Test
        void shouldGenerateUniqueIdForEachOwner() {
            var owner1 = OwnerTestBuilder.aOwner().build();
            var owner2 = OwnerTestBuilder.aOwner().build();

            assertNotEquals(owner1.getId(), owner2.getId());
        }

        @Test
        void shouldHaveSameCreatedAndUpdatedAtOnCreation() {
            var owner = OwnerTestBuilder.aOwner().build();

            assertEquals(owner.getCreatedAt(), owner.getUpdatedAt());
        }

        @Test
        void shouldThrowWhenCreatingWithNullName() {
            assertThrows(
                    NullPointerException.class,
                    () -> Owner.create(null, TestOwnerMother.EMAIL_JOHN, TestOwnerMother.PHONE_JOHN)
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullEmail() {
            assertThrows(
                    NullPointerException.class,
                    () -> Owner.create(TestOwnerMother.OWNER_NAME_JOHN, null, TestOwnerMother.PHONE_JOHN)
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullPhone() {
            assertThrows(
                    NullPointerException.class,
                    () -> Owner.create(TestOwnerMother.OWNER_NAME_JOHN, TestOwnerMother.EMAIL_JOHN, null)
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

            var owner = Owner.reconstitute(
                    id,
                    TestOwnerMother.OWNER_NAME_JOHN,
                    TestOwnerMother.EMAIL_JOHN,
                    TestOwnerMother.PHONE_JOHN,
                    createdAt,
                    updatedAt
            );

            assertEquals(id, owner.getId());
            assertEquals(TestOwnerMother.OWNER_NAME_JOHN, owner.getName());
            assertEquals(TestOwnerMother.EMAIL_JOHN, owner.getEmail());
            assertEquals(TestOwnerMother.PHONE_JOHN, owner.getPhone());
            assertEquals(createdAt, owner.getCreatedAt());
            assertEquals(updatedAt, owner.getUpdatedAt());
        }

        @Test
        void shouldThrowWhenReconstituteWithNullId() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Owner.reconstitute(null, TestOwnerMother.OWNER_NAME_JOHN, TestOwnerMother.EMAIL_JOHN, TestOwnerMother.PHONE_JOHN, now, now)
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullName() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Owner.reconstitute(OwnerId.generate(), null, TestOwnerMother.EMAIL_JOHN, TestOwnerMother.PHONE_JOHN, now, now)
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullEmail() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Owner.reconstitute(OwnerId.generate(), TestOwnerMother.OWNER_NAME_JOHN, null, TestOwnerMother.PHONE_JOHN, now, now)
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullPhone() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Owner.reconstitute(OwnerId.generate(), TestOwnerMother.OWNER_NAME_JOHN, TestOwnerMother.EMAIL_JOHN, null, now, now)
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullCreatedAt() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Owner.reconstitute(OwnerId.generate(), TestOwnerMother.OWNER_NAME_JOHN, TestOwnerMother.EMAIL_JOHN, TestOwnerMother.PHONE_JOHN, null, now)
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullUpdatedAt() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Owner.reconstitute(OwnerId.generate(), TestOwnerMother.OWNER_NAME_JOHN, TestOwnerMother.EMAIL_JOHN, TestOwnerMother.PHONE_JOHN, now, null)
            );
        }

    }

    @Nested
    class Rename {

        @Test
        void shouldRenameOwner() {
            var owner = OwnerTestBuilder.aOwner().build();

            owner.rename(TestOwnerMother.OWNER_NAME_ROBERT);

            assertEquals(TestOwnerMother.OWNER_NAME_ROBERT, owner.getName());
        }

        @Test
        void shouldNotUpdateWithSameName() {
            var owner = OwnerTestBuilder.aOwner().build();
            var originalUpdatedAt = owner.getUpdatedAt();

            owner.rename(TestOwnerMother.OWNER_NAME_JOHN);

            assertEquals(originalUpdatedAt, owner.getUpdatedAt());
        }

        @Test
        void shouldThrowWhenRenamingWithNullName() {
            var owner = OwnerTestBuilder.aOwner().build();

            assertThrows(
                    NullPointerException.class,
                    () -> owner.rename(null)
            );
        }

    }

    @Nested
    class UpdateEmail {

        @Test
        void shouldUpdateEmail() {
            var owner = OwnerTestBuilder.aOwner().build();

            owner.updateEmail(TestOwnerMother.EMAIL_ROBERT);

            assertEquals(TestOwnerMother.EMAIL_ROBERT, owner.getEmail());
        }

        @Test
        void shouldNotUpdateWithSameEmail() {
            var owner = OwnerTestBuilder.aOwner().build();
            var originalUpdatedAt = owner.getUpdatedAt();

            owner.updateEmail(TestOwnerMother.EMAIL_JOHN);

            assertEquals(originalUpdatedAt, owner.getUpdatedAt());
        }

        @Test
        void shouldThrowWhenUpdatingWithNullEmail() {
            var owner = OwnerTestBuilder.aOwner().build();

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
            var owner = OwnerTestBuilder.aOwner().build();

            owner.updatePhone(TestOwnerMother.PHONE_ROBERT);

            assertEquals(TestOwnerMother.PHONE_ROBERT, owner.getPhone());
        }

        @Test
        void shouldNotUpdateWithSamePhone() {
            var owner = OwnerTestBuilder.aOwner().build();
            var originalUpdatedAt = owner.getUpdatedAt();

            owner.updatePhone(TestOwnerMother.PHONE_JOHN);

            assertEquals(originalUpdatedAt, owner.getUpdatedAt());
        }

        @Test
        void shouldThrowWhenUpdatingWithNullPhone() {
            var owner = OwnerTestBuilder.aOwner().build();

            assertThrows(
                    NullPointerException.class,
                    () -> owner.updatePhone(null)
            );
        }

    }

}
