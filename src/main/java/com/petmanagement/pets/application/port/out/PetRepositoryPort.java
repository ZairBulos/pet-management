package com.petmanagement.pets.application.port.out;

import com.petmanagement.pets.domain.model.aggregate.Pet;

public interface PetRepositoryPort {
    void save(Pet pet);
}
