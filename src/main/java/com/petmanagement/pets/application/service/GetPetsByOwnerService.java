package com.petmanagement.pets.application.service;

import com.petmanagement.pets.application.port.in.GetPetsByOwnerUseCase;
import com.petmanagement.pets.application.port.out.PetRepositoryPort;
import com.petmanagement.pets.domain.model.aggregate.Pet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
class GetPetsByOwnerService implements GetPetsByOwnerUseCase {

    private final PetRepositoryPort repository;

    GetPetsByOwnerService(PetRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pet> execute(GetPetsByOwnerQuery query) {
        return repository.findByOwnerId(query.ownerId());
    }

}
