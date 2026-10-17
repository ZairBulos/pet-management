package com.petmanagement.owners.application.port.out;

import com.petmanagement.owners.domain.model.aggregate.Owner;
import com.petmanagement.owners.domain.model.valueobject.Email;
import com.petmanagement.owners.domain.model.valueobject.OwnerId;

import java.util.Optional;

public interface OwnerRepositoryPort {
    Optional<Owner> findById(OwnerId ownerId);
    Optional<Owner> findByEmail(Email email);
    void save(Owner owner);
}
