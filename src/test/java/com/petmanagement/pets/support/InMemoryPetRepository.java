package com.petmanagement.pets.support;

import com.petmanagement.pets.application.port.out.PetRepositoryPort;
import com.petmanagement.pets.domain.model.aggregate.Pet;
import com.petmanagement.pets.domain.model.valueobject.PetId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryPetRepository implements PetRepositoryPort {

    private final List<Pet> pets = new ArrayList<>();

    @Override
    public Optional<Pet> findById(PetId petId) {
        return pets.stream()
                .filter(pet -> pet.getId().equals(petId))
                .findFirst();
    }

    @Override
    public void save(Pet pet) {
        pets.add(pet);
    }

    public void clear() {
        pets.clear();
    }

}
