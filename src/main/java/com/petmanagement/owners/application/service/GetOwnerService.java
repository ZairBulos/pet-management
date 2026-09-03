package com.petmanagement.owners.application.service;

import com.petmanagement.owners.application.port.in.GetOwnerUseCase;
import com.petmanagement.owners.application.port.out.OwnerRepositoryPort;
import com.petmanagement.owners.domain.exception.OwnerNotFoundException;
import com.petmanagement.owners.domain.model.aggregate.Owner;
import org.springframework.stereotype.Service;

@Service
class GetOwnerService implements GetOwnerUseCase {

    private final OwnerRepositoryPort repository;

    public GetOwnerService(OwnerRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Owner execute(GetOwnerCommand command) {
        return repository.findById(command.ownerId())
                .orElseThrow(OwnerNotFoundException::new);
    }

}
