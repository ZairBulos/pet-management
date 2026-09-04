package com.petmanagement.pets.application.port.out;

import com.petmanagement.pets.domain.model.aggregate.Pet;
import com.petmanagement.pets.domain.model.valueobject.PetId;

import java.util.Optional;

public interface PetRepositoryPort {
    Optional<Pet> findById(PetId petId);
    void save(Pet pet);
}
