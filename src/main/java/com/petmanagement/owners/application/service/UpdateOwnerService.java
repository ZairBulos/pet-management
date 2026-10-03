package com.petmanagement.owners.application.service;

import com.petmanagement.owners.application.port.in.UpdateOwnerUseCase;
import com.petmanagement.owners.application.port.out.OwnerRepositoryPort;
import com.petmanagement.owners.domain.exception.OwnerAlreadyExistsException;
import com.petmanagement.owners.domain.exception.OwnerNotFoundException;
import com.petmanagement.owners.domain.model.aggregate.Owner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class UpdateOwnerService implements UpdateOwnerUseCase {

    private final OwnerRepositoryPort repository;

    UpdateOwnerService(OwnerRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Owner execute(UpdateOwnerCommand command) {
        var owner = repository.findById(command.ownerId())
                .orElseThrow(OwnerNotFoundException::new);

        if (!owner.getEmail().equals(command.email()))
            repository.findByEmail(command.email())
                    .ifPresent(_ -> {
                        throw new OwnerAlreadyExistsException();
                    });

        owner.updateEmail(command.email());
        owner.rename(command.name());
        owner.updatePhone(command.phone());

        repository.save(owner);

        return owner;
    }

}
