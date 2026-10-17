package com.petmanagement.pets.application.service;

import com.petmanagement.pets.api.PetApi;
import com.petmanagement.pets.application.port.out.PetRepositoryPort;
import com.petmanagement.pets.domain.model.valueobject.PetId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
class PetApiService implements PetApi {

    private final PetRepositoryPort repository;

    PetApiService(PetRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID petId) {
        return repository
                .findById(PetId.of(petId))
                .isPresent();
    }

}
