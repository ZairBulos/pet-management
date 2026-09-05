package com.petmanagement.pets.application.service;

import com.petmanagement.pets.domain.model.aggregate.Pet;
import com.petmanagement.pets.domain.model.enums.Sex;
import com.petmanagement.pets.domain.model.valueobject.*;
import com.petmanagement.pets.support.InMemoryPetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

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

    @Test
    void shouldReturnTrueWhenPetExists() {
        // Given
        var pet = Pet.create(
                OwnerId.of("b7da461d-da63-4bb3-bf06-397d92d85a5e"),
                new PetName("Luna"),
                new Species("Feline"),
                new Breed("Siamese"),
                new Coat("Cream"),
                Sex.FEMALE,
                LocalDate.of(2021, 01, 8)
        );
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
        var petId = UUID.fromString("5447cfce-bc2b-4957-a672-9f7c767ce010");

        // When
        boolean result = service.existsById(petId);

        // Then
        assertFalse(result);
    }

}
