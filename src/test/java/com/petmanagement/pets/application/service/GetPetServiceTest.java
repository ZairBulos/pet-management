package com.petmanagement.pets.application.service;

import com.petmanagement.pets.application.port.in.GetPetUseCase;
import com.petmanagement.pets.domain.exception.PetNotFoundException;
import com.petmanagement.pets.support.InMemoryPetRepository;
import com.petmanagement.pets.support.PetTestBuilder;
import com.petmanagement.pets.support.TestPetMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GetPetServiceTest {

    private InMemoryPetRepository repository;
    private GetPetService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryPetRepository();
        service = new GetPetService(repository);
    }

    @Nested
    class WhenRetrievingExistingPet {

        @Test
        void shouldGetExistingPet() {
            // Given
            var pet = PetTestBuilder.aPet().build();
            repository.save(pet);

            var command = new GetPetUseCase.GetPetCommand(pet.getId());

            // When
            var result = service.execute(command);

            // Then
            assertNotNull(result);
            assertEquals(pet.getId(), result.getId());
            assertEquals(pet.getOwnerId(), result.getOwnerId());
            assertEquals(pet.getName(), result.getName());
        }

    }

    @Nested
    class WhenPetDoesNotExist {

        @Test
        void shouldThrowWhenPetDoesNotExist() {
            // Given
            var command = new GetPetUseCase.GetPetCommand(TestPetMother.NON_EXISTENT_PET_ID);

            // When/Then
            assertThrows(
                    PetNotFoundException.class,
                    () -> service.execute(command)
            );
        }

    }

}
