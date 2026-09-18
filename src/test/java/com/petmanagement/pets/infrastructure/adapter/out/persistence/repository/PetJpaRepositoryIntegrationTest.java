package com.petmanagement.pets.infrastructure.adapter.out.persistence.repository;

import com.petmanagement.RepositoryTest;
import com.petmanagement.pets.support.PetJpaEntityTestBuilder;
import com.petmanagement.pets.support.TestOwnerIdMother;
import com.petmanagement.pets.support.TestPetMother;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RepositoryTest
class PetJpaRepositoryIntegrationTest {

    @Autowired
    private PetJpaRepository repository;

    @Nested
    class WhenFindingPetsByOwner {

        @Test
        void shouldReturnPetsForExistingOwner() {
            // Given
            var ownerId = TestOwnerIdMother.EXISTING_OWNER_ID.value();

            var pet1 = PetJpaEntityTestBuilder.aPetJpaEntity()
                    .withId(TestPetMother.DEFAULT_PET_ID.value())
                    .withName(TestPetMother.PET_NAME_BUDDY.value())
                    .withOwnerId(ownerId)
                    .build();
            var pet2 = PetJpaEntityTestBuilder.aPetJpaEntity()
                    .withId(TestPetMother.ANOTHER_PET_ID.value())
                    .withName(TestPetMother.PET_NAME_CHARLIE.value())
                    .withOwnerId(ownerId)
                    .build();

            repository.saveAll(List.of(pet1, pet2));

            // When
            var result = repository.findByOwnerId(ownerId);

            // Then
            assertEquals(2, result.size());
        }

        @Test
        void shouldReturnEmptyWhenOwnerHasNoPets() {
            // Given
            var ownerId = TestOwnerIdMother.ANOTHER_OWNER_ID.value();

            // When
            var result = repository.findByOwnerId(ownerId);

            // Then
            assertTrue(result.isEmpty());
        }

    }

}
