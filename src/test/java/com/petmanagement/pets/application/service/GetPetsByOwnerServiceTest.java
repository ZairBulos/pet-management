package com.petmanagement.pets.application.service;

import com.petmanagement.pets.application.port.in.GetPetsByOwnerUseCase;
import com.petmanagement.pets.support.InMemoryPetRepository;
import com.petmanagement.pets.support.PetTestBuilder;
import com.petmanagement.pets.support.TestOwnerIdMother;
import com.petmanagement.pets.support.TestPetMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GetPetsByOwnerServiceTest {

    private InMemoryPetRepository repository;
    private GetPetsByOwnerService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryPetRepository();
        service = new GetPetsByOwnerService(repository);
    }

    @Nested
    class WhenOwnerHasPets {

        @Test
        void shouldReturnAllPetsForOwner() {
            // Given
            var ownerId = TestOwnerIdMother.EXISTING_OWNER_ID;

            var pet1 = PetTestBuilder.aPet()
                    .withOwnerId(ownerId)
                    .withPetName(TestPetMother.PET_NAME_BUDDY)
                    .build();
            var pet2 = PetTestBuilder.aPet()
                    .withOwnerId(ownerId)
                    .withPetName(TestPetMother.PET_NAME_LUNA)
                    .build();

            repository.saveAll(pet1, pet2);

            var query = new GetPetsByOwnerUseCase.GetPetsByOwnerQuery(ownerId);

            // When
            var result = service.execute(query);

            // Then
            assertEquals(2, result.size());
        }

    }

    @Nested
    class WhenOwnerHasNoPets {

        @Test
        void shouldReturnEmptyList() {
            // Given
            var ownerId = TestOwnerIdMother.ANOTHER_OWNER_ID;

            var query = new GetPetsByOwnerUseCase.GetPetsByOwnerQuery(ownerId);

            // When
            var result = service.execute(query);

            // Then
            assertTrue(result.isEmpty());
        }

    }

}
