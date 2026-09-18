package com.petmanagement.pets.infrastructure.adapter.out.persistence;

import com.petmanagement.RepositoryTest;
import com.petmanagement.pets.domain.model.aggregate.Pet;
import com.petmanagement.pets.infrastructure.adapter.out.persistence.mapper.PetMapper;
import com.petmanagement.pets.support.PetTestBuilder;
import com.petmanagement.pets.support.TestOwnerIdMother;
import com.petmanagement.pets.support.TestPetMother;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RepositoryTest
@Import({
        PetPersistenceAdapter.class,
        PetMapper.class,
})
class PetPersistenceAdapterIntegrationTest {

    @Autowired
    private PetPersistenceAdapter adapter;

    @Nested
    class WhenFindingPetById {

        @Test
        void shouldReturnPetWhenExists() {
            // Given
            var pet = PetTestBuilder.aPet().build();
            adapter.save(pet);

            // When
            var result = adapter.findById(pet.getId());

            // Then
            assertTrue(result.isPresent());
        }

        @Test
        void shouldReturnEmptyWhenPetDoesNotExist() {
            // Given
            var nonExistentPetId = TestPetMother.NON_EXISTENT_PET_ID;

            // When
            var result = adapter.findById(nonExistentPetId);

            // Then
            assertTrue(result.isEmpty());
        }

    }

    @Nested
    class WhenFindingPetByOwnerId {

        @Test
        void shouldReturnPetsForExistingOwner() {
            // Given
            var ownerId = TestOwnerIdMother.EXISTING_OWNER_ID;

            var pet1 = PetTestBuilder.aPet()
                    .withPetName(TestPetMother.PET_NAME_LUNA)
                    .withOwnerId(ownerId)
                    .build();
            var pet2 = PetTestBuilder.aPet()
                    .withPetName(TestPetMother.PET_NAME_BELLA)
                    .withOwnerId(ownerId)
                    .build();

            adapter.save(pet1);
            adapter.save(pet2);

            // When
            var result = adapter.findByOwnerId(ownerId);

            // Then
            assertEquals(2, result.size());
        }

        @Test
        void shouldReturnEmptyWhenOwnerHasNoPets() {
            // Given
            var nonExistentOwnerId = TestOwnerIdMother.NON_EXISTING_OWNER_ID;

            // When
            var result = adapter.findByOwnerId(nonExistentOwnerId);

            // Then
            assertTrue(result.isEmpty());
        }

    }

    @Nested
    class WhenSavingPet {

        @Test
        void shouldPersistPetToDatabase() {
            // Given
            var pet = PetTestBuilder.aPet().build();

            // When
            adapter.save(pet);

            // Then
            var result = adapter.findById(pet.getId());
            assertTrue(result.isPresent());
        }

        @Test
        void shouldUpdatePetWhenSavingExisting() {
            // Given
            var pet = PetTestBuilder.aPet().build();
            adapter.save(pet);

            var updatedPet = Pet.reconstitute(
                    pet.getId(),
                    pet.getOwnerId(),
                    TestPetMother.PET_NAME_CHARLIE,
                    pet.getSpecies(),
                    pet.getBreed(),
                    pet.getCoat(),
                    pet.getSex(),
                    pet.getBirthDate(),
                    pet.getCreatedAt(),
                    pet.getUpdatedAt()
            );

            // When
            adapter.save(updatedPet);

            // Then
            var result = adapter.findById(pet.getId());

            assertTrue(result.isPresent());
            assertEquals(updatedPet.getName(), result.get().getName());
        }

    }

}
