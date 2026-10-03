package com.petmanagement.owners.application.service;

import com.petmanagement.owners.application.port.in.GetOwnerUseCase;
import com.petmanagement.owners.application.port.out.OwnerRepositoryPort;
import com.petmanagement.owners.domain.exception.OwnerNotFoundException;
import com.petmanagement.owners.domain.model.aggregate.Owner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class GetOwnerService implements GetOwnerUseCase {

    private final OwnerRepositoryPort repository;

    GetOwnerService(OwnerRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public Owner execute(GetOwnerCommand command) {
        return repository.findById(command.ownerId())
                .orElseThrow(OwnerNotFoundException::new);
    }

}
