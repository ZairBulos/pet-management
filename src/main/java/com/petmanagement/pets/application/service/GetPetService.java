package com.petmanagement.pets.application.service;

import com.petmanagement.pets.application.port.in.GetPetUseCase;
import com.petmanagement.pets.application.port.out.PetRepositoryPort;
import com.petmanagement.pets.domain.exception.PetNotFoundException;
import com.petmanagement.pets.domain.model.aggregate.Pet;
import org.springframework.stereotype.Service;

@Service
class GetPetService implements GetPetUseCase {

    private final PetRepositoryPort repository;

    public GetPetService(PetRepositoryPort repository) {
        this.repository = repository;
    }


    @Override
    public Pet execute(GetPetCommand command) {
        return repository.findById(command.petId())
                .orElseThrow(PetNotFoundException::new);
    }

}
