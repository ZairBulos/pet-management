package com.petmanagement.owners.infrastructure.adapter.out.persistence;

import com.petmanagement.RepositoryTest;
import com.petmanagement.owners.domain.model.aggregate.Owner;
import com.petmanagement.owners.domain.model.valueobject.Email;
import com.petmanagement.owners.infrastructure.adapter.out.persistence.mapper.OwnerMapper;
import com.petmanagement.owners.support.OwnerTestBuilder;
import com.petmanagement.owners.support.TestOwnerMother;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RepositoryTest
@Import({
        OwnerPersistenceAdapter.class,
        OwnerMapper.class
})
class OwnerPersistenceAdapterIntegrationTest {

    @Autowired
    private OwnerPersistenceAdapter adapter;

    @Nested
    class WhenFindingOwnerById {

        @Test
        void shouldReturnOwnerWhenExists() {
            // Given
            var owner = OwnerTestBuilder.aOwner().build();
            adapter.save(owner);

            // When
            var result = adapter.findById(owner.getId());

            // Then
            assertTrue(result.isPresent());
        }

        @Test
        void shouldReturnEmptyWhenOwnerDoesNotExist() {
            // Given
            var ownerId = TestOwnerMother.NON_EXISTING_OWNER_ID;

            // When
            var result = adapter.findById(ownerId);

            // Then
            assertTrue(result.isEmpty());
        }

    }

    @Nested
    class WhenFindingOwnerByEmail {

        @Test
        void shouldReturnOwnerWhenEmailExists() {
            // Given
            var owner = OwnerTestBuilder.aOwner().build();
            adapter.save(owner);

            // When
            var result = adapter.findByEmail(owner.getEmail());

            // Then
            assertTrue(result.isPresent());
        }

        @Test
        void shouldReturnEmptyWhenEmailDoesNotExist() {
            // Given
            var email = new Email("notfound@example.com");

            // When
            var result = adapter.findByEmail(email);

            // Then
            assertTrue(result.isEmpty());
        }

    }

    @Nested
    class WhenSavingOwner {

        @Test
        void shouldPersistOwnerToDatabase() {
            // Given
            var owner = OwnerTestBuilder.aOwner().build();

            // When
            adapter.save(owner);

            // Then
            var result = adapter.findById(owner.getId());

            assertTrue(result.isPresent());
        }

        @Test
        void shouldUpdateOwnerWhenSavingExisting() {
            // Given
            var owner = OwnerTestBuilder.aOwner().build();
            adapter.save(owner);

            var updatedOwner = Owner.reconstitute(
                    owner.getId(),
                    TestOwnerMother.OWNER_NAME_ROBERT,
                    TestOwnerMother.EMAIL_ROBERT,
                    owner.getPhone(),
                    owner.getCreatedAt(),
                    owner.getUpdatedAt()
            );

            // When
            adapter.save(updatedOwner);

            // Then
            var result = adapter.findById(owner.getId());

            assertTrue(result.isPresent());
            assertEquals(updatedOwner.getName(), result.get().getName());
            assertEquals(updatedOwner.getEmail(), result.get().getEmail());
        }

    }

}
