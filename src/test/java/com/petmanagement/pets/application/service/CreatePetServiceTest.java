package com.petmanagement.pets.application.service;

import com.petmanagement.pets.support.InMemoryPetRepository;
import com.petmanagement.pets.support.PetTestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CreatePetServiceTest {

    private InMemoryPetRepository repository;
    private CreatePetService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryPetRepository();
        service = new CreatePetService(repository);
    }

    @Nested
    class WhenCreatingPet {

        @Test
        void shouldCreatePet() {
            // Given
            var command = PetTestBuilder.CreatePetCommandBuilder
                    .aCreatePetCommand()
                    .build();

            // When
            var petId = service.execute(command);

            // Then
            assertNotNull(petId);
        }

    }

}
