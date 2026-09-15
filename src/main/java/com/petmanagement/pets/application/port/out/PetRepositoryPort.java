package com.petmanagement.pets.application.port.out;

import com.petmanagement.pets.domain.model.aggregate.Pet;
import com.petmanagement.pets.domain.model.valueobject.OwnerId;
import com.petmanagement.pets.domain.model.valueobject.PetId;

import java.util.List;
import java.util.Optional;

public interface PetRepositoryPort {
    Optional<Pet> findById(PetId petId);
    List<Pet> findByOwnerId(OwnerId ownerId);
    void save(Pet pet);
}
