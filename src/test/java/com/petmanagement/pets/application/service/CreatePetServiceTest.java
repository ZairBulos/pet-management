package com.petmanagement.pets.application.service;

import com.petmanagement.pets.application.port.in.CreatePetUseCase;
import com.petmanagement.pets.domain.model.enums.Sex;
import com.petmanagement.pets.domain.model.valueobject.*;
import com.petmanagement.pets.support.InMemoryPetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CreatePetServiceTest {

    private InMemoryPetRepository repository;
    private CreatePetService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryPetRepository();
        service = new CreatePetService(repository);
    }

    @Test
    void shouldCreatePet() {
        // Given
        var command = new CreatePetUseCase.CreatePetCommand(
                OwnerId.of("f6308a73-1962-481c-b3a5-74b5129c1165"),
                new PetName("Bellatrix"),
                new Species("Feline"),
                new Breed("Common"),
                new Coat("Orange-White"),
                Sex.FEMALE,
                LocalDate.of(2023, 01, 01)
        );

        // When
        var petId = service.execute(command);

        // Then
        assertNotNull(petId);
    }

}
