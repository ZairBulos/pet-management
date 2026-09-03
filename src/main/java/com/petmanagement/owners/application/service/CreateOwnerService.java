package com.petmanagement.owners.application.service;

import com.petmanagement.owners.application.port.in.CreateOwnerUseCase;
import com.petmanagement.owners.application.port.out.OwnerRepositoryPort;
import com.petmanagement.owners.domain.exception.OwnerAlreadyExistsException;
import com.petmanagement.owners.domain.model.aggregate.Owner;
import com.petmanagement.owners.domain.model.valueobject.OwnerId;
import org.springframework.stereotype.Service;

@Service
class CreateOwnerService implements CreateOwnerUseCase {

    private final OwnerRepositoryPort repository;

    public CreateOwnerService(OwnerRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public OwnerId execute(CreateOwnerCommand command) {
        repository.findByEmail(command.email())
                .ifPresent(_ -> {
                    throw new OwnerAlreadyExistsException();
                });

        var owner = Owner.create(command.name(), command.email(), command.phone());
        repository.save(owner);

        return owner.getId();
    }

}
