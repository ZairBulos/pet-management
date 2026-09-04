package com.petmanagement.pets.application.service;

import com.petmanagement.pets.application.port.in.UpdatePetUseCase;
import com.petmanagement.pets.application.port.out.PetRepositoryPort;
import com.petmanagement.pets.domain.exception.PetNotFoundException;
import com.petmanagement.pets.domain.model.aggregate.Pet;
import org.springframework.stereotype.Service;

@Service
class UpdatePetService implements UpdatePetUseCase {

    private final PetRepositoryPort repository;

    public UpdatePetService(PetRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Pet execute(UpdatePetCommand command) {
        var pet = repository.findById(command.petId())
                .orElseThrow(PetNotFoundException::new);

        pet.rename(command.name());

        repository.save(pet);

        return pet;
    }

}
