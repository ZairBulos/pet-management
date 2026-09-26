package com.petmanagement.owners.application.service;

import com.petmanagement.owners.api.OwnerApi;
import com.petmanagement.owners.application.port.out.OwnerRepositoryPort;
import com.petmanagement.owners.domain.model.valueobject.Email;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
class OwnerApiService implements OwnerApi {

    private final OwnerRepositoryPort repository;

    public OwnerApiService(OwnerRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return repository
                .findByEmail(new Email(email))
                .isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UUID> getOwnerIdByEmail(String email) {
        return repository
                .findByEmail(new Email(email))
                .map(owner -> owner.getId().value());
    }

}
