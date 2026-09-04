package com.petmanagement.pets.application.service;

import com.petmanagement.pets.application.port.in.UpdatePetUseCase;
import com.petmanagement.pets.domain.exception.PetNotFoundException;
import com.petmanagement.pets.domain.model.aggregate.Pet;
import com.petmanagement.pets.domain.model.enums.Sex;
import com.petmanagement.pets.domain.model.valueobject.*;
import com.petmanagement.pets.support.InMemoryPetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UpdatePetServiceTest {

    private InMemoryPetRepository repository;
    private UpdatePetService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryPetRepository();
        service = new UpdatePetService(repository);
    }

    @Test
    void shouldUpdatePet() {
        // Given
        var pet = Pet.create(
                OwnerId.of("352a9d8f-d0b3-4d6c-aee0-0608c63baac2"),
                new PetName("Buddy"),
                new Species("Dog"),
                new Breed("Golden Retriever"),
                new Coat("Golden"),
                Sex.MALE,
                LocalDate.of(2020, 5, 15)
        );
        repository.save(pet);

        var command = new UpdatePetUseCase.UpdatePetCommand(
                pet.getId(),
                new PetName("Max")
        );

        // When
        var result = service.execute(command);

        // Then
        assertNotNull(result);
        assertEquals(pet.getName(), result.getName());
    }

    @Test
    void shouldThrowWhenPetNotFound() {
        // Given
        var command = new UpdatePetUseCase.UpdatePetCommand(
                PetId.of("799ad3be-d31e-43ae-b5f5-a4f32939c36a"),
                new PetName("Not Found")
        );

        // When/Then
        assertThrows(
                PetNotFoundException.class,
                () -> service.execute(command)
        );
    }

}
