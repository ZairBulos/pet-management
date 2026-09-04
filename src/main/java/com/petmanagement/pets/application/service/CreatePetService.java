package com.petmanagement.pets.application.service;

import com.petmanagement.pets.application.port.in.CreatePetUseCase;
import com.petmanagement.pets.application.port.out.PetRepositoryPort;
import com.petmanagement.pets.domain.model.aggregate.Pet;
import com.petmanagement.pets.domain.model.valueobject.PetId;
import org.springframework.stereotype.Service;

@Service
class CreatePetService implements CreatePetUseCase {

    private final PetRepositoryPort repository;

    public CreatePetService(PetRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public PetId execute(CreatePetCommand command) {
        var pet = Pet.create(
                command.ownerId(),
                command.name(),
                command.species(),
                command.breed(),
                command.coat(),
                command.sex(),
                command.birthDate()
        );

        repository.save(pet);

        return pet.getId();
    }

}
