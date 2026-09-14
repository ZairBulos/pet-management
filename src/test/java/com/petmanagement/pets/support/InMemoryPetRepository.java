package com.petmanagement.pets.support;

import com.petmanagement.pets.application.port.out.PetRepositoryPort;
import com.petmanagement.pets.domain.model.aggregate.Pet;
import com.petmanagement.pets.domain.model.valueobject.PetId;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryPetRepository implements PetRepositoryPort {

    private final Map<PetId, Pet> pets = new HashMap<>();

    @Override
    public Optional<Pet> findById(PetId petId) {
        return Optional.ofNullable(pets.get(petId));
    }

    @Override
    public void save(Pet pet) {
        pets.put(pet.getId(), pet);
    }

    // === Helpers ===

    public void clear() {
        pets.clear();
    }

    public void saveAll(Pet... pets) {
        for (Pet pet : pets) {
            save(pet);
        }
    }

}
