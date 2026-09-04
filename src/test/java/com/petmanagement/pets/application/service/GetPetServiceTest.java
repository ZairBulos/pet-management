package com.petmanagement.pets.application.service;

import com.petmanagement.pets.application.port.in.GetPetUseCase;
import com.petmanagement.pets.domain.exception.PetNotFoundException;
import com.petmanagement.pets.domain.model.aggregate.Pet;
import com.petmanagement.pets.domain.model.enums.Sex;
import com.petmanagement.pets.domain.model.valueobject.*;
import com.petmanagement.pets.support.InMemoryPetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class GetPetServiceTest {

    private InMemoryPetRepository repository;
    private GetPetService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryPetRepository();
        service = new GetPetService(repository);
    }

    @Test
    void shouldGetExistingPet() {
        // Given
        var pet = Pet.create(
                OwnerId.of("bf41537f-e8d3-445f-ad35-176e4e9f2c18"),
                new PetName("Buddy"),
                new Species("Dog"),
                new Breed("Golden Retriever"),
                new Coat("Golden"),
                Sex.MALE,
                LocalDate.of(2020, 5, 15)
        );
        repository.save(pet);

        var command = new GetPetUseCase.GetPetCommand(pet.getId());

        // When
        var result = service.execute(command);

        // Then
        assertNotNull(result);
        assertEquals(pet.getId(), result.getId());
        assertEquals(pet.getOwnerId(), result.getOwnerId());
    }

    @Test
    void shouldThrowWhenPetNotFound() {
        // Given
        var command = new GetPetUseCase.GetPetCommand(
                PetId.of("a8d898ae-de46-4289-ac50-17ae463fd1f0")
        );

        // When/Then
        assertThrows(
                PetNotFoundException.class,
                () -> service.execute(command)
        );
    }

}
