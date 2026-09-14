package com.petmanagement.pets.application.service;

import com.petmanagement.pets.domain.exception.PetNotFoundException;
import com.petmanagement.pets.support.InMemoryPetRepository;
import com.petmanagement.pets.support.PetTestBuilder;
import com.petmanagement.pets.support.TestPetMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdatePetServiceTest {

    private InMemoryPetRepository repository;
    private UpdatePetService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryPetRepository();
        service = new UpdatePetService(repository);
    }

    @Nested
    class WhenUpdatingPet {

        @Test
        void shouldUpdatePet() {
            // Given
            var pet = PetTestBuilder.aPet().build();
            repository.save(pet);

            var command = PetTestBuilder.UpdatePetCommandBuilder
                    .aUpdatePetCommand()
                    .withId(pet.getId())
                    .build();

            // When
            var result = service.execute(command);

            // Then
            assertNotNull(result);
            assertEquals(command.name(), result.getName());
        }

    }

    @Nested
    class WhenPetDoesNotExist {

        @Test
        void shouldThrowWhenPetDoesNotExist() {
            // Given
            var command = PetTestBuilder.UpdatePetCommandBuilder
                    .aUpdatePetCommand()
                    .withId(TestPetMother.NON_EXISTENT_PET_ID)
                    .build();

            // When/Then
            assertThrows(
                    PetNotFoundException.class,
                    () -> service.execute(command)
            );
        }

    }

}
