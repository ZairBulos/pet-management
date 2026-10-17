package com.petmanagement.pets.application.service;

import com.petmanagement.pets.application.port.in.GetPetUseCase;
import com.petmanagement.pets.application.port.out.PetRepositoryPort;
import com.petmanagement.pets.domain.exception.PetNotFoundException;
import com.petmanagement.pets.domain.model.aggregate.Pet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class GetPetService implements GetPetUseCase {

    private final PetRepositoryPort repository;

    GetPetService(PetRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public Pet execute(GetPetCommand command) {
        return repository.findById(command.petId())
                .orElseThrow(PetNotFoundException::new);
    }

}
