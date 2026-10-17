package com.petmanagement.pets.application.service;

import com.petmanagement.pets.support.InMemoryPetRepository;
import com.petmanagement.pets.support.PetTestBuilder;
import com.petmanagement.pets.support.TestPetMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PetApiServiceTest {

    private InMemoryPetRepository repository;
    private PetApiService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryPetRepository();
        service = new PetApiService(repository);
    }

    @Nested
    class WhenCheckingPetExistence {

        @Test
        void shouldReturnTrueWhenPetExists() {
            // Given
            var pet = PetTestBuilder.aPet().build();
            repository.save(pet);

            var petId = pet.getId().value();

            // When
            var result = service.existsById(petId);

            // Then
            assertTrue(result);
        }

        @Test
        void shouldReturnFalseWhenPetDoesNotExist() {
            // Given
            var petId = TestPetMother.NON_EXISTENT_PET_ID.value();

            // When
            boolean result = service.existsById(petId);

            // Then
            assertFalse(result);
        }

    }

}
