package com.petmanagement.pets.infrastructure.adapter.out.persistence.mapper;

import com.petmanagement.pets.support.PetJpaEntityTestBuilder;
import com.petmanagement.pets.support.PetTestBuilder;
import com.petmanagement.pets.support.TestOwnerIdMother;
import com.petmanagement.pets.support.TestPetMother;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PetMapperTest {

    private final PetMapper mapper = new PetMapper();

    @Nested
    class WhenMappingDomainToJpaEntity {

        @Test
        void shouldMapDomainToJpaEntity() {
            // Given
            var pet = PetTestBuilder.aPet().build();

            // When
            var result = mapper.toJpaEntity(pet);

            // Then
            assertNotNull(result);
            assertEquals(pet.getId().value(), result.getId());
            assertEquals(pet.getOwnerId().value(), result.getOwnerId());
            assertEquals(pet.getName().value(), result.getName());
            assertEquals(pet.getSpecies().value(), result.getSpecies());
            assertEquals(pet.getBreed().value(), result.getBreed());
            assertEquals(pet.getCoat().value(), result.getCoat());
            assertEquals(pet.getSex().name(), result.getSex());
            assertEquals(pet.getCreatedAt(), result.getCreatedAt());
            assertEquals(pet.getUpdatedAt(), result.getUpdatedAt());
        }

    }

    @Nested
    class WhenMappingJpaEntityToDomain {

        @Test
        void shouldMapJpaEntityToDomain() {
            // Given
            var entity = PetJpaEntityTestBuilder.aPetJpaEntity().build();

            // When
            var result = mapper.toDomain(entity);

            // Then
            assertNotNull(result);
            assertEquals(TestPetMother.DEFAULT_PET_ID, result.getId());
            assertEquals(TestOwnerIdMother.EXISTING_OWNER_ID, result.getOwnerId());
            assertEquals(TestPetMother.PET_NAME_BUDDY, result.getName());
            assertEquals(TestPetMother.SPECIES_DOG, result.getSpecies());
            assertEquals(TestPetMother.BREED_GOLDEN_RETRIEVER, result.getBreed());
            assertEquals(TestPetMother.COAT_GOLDEN, result.getCoat());
            assertEquals(TestPetMother.SEX_MALE, result.getSex());
            assertEquals(TestPetMother.BIRTH_DATE_STANDARD, result.getBirthDate());
        }

    }

}
