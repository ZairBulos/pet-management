package com.petmanagement.pets.support;

import com.petmanagement.pets.application.port.out.PetRepositoryPort;
import com.petmanagement.pets.domain.model.aggregate.Pet;

import java.util.ArrayList;
import java.util.List;

public class InMemoryPetRepository implements PetRepositoryPort {

    private final List<Pet> pets = new ArrayList<>();

    @Override
    public void save(Pet pet) {
        pets.add(pet);
    }

    public void clear() {
        pets.clear();
    }

}
