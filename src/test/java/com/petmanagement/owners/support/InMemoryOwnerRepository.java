package com.petmanagement.owners.support;

import com.petmanagement.owners.application.port.out.OwnerRepositoryPort;
import com.petmanagement.owners.domain.model.aggregate.Owner;
import com.petmanagement.owners.domain.model.valueobject.Email;
import com.petmanagement.owners.domain.model.valueobject.OwnerId;

import java.util.*;

public final class InMemoryOwnerRepository implements OwnerRepositoryPort {

    private final Map<OwnerId, Owner> owners = new HashMap<>();

    @Override
    public Optional<Owner> findById(OwnerId ownerId) {
        return Optional.ofNullable(owners.get(ownerId));
    }

    @Override
    public Optional<Owner> findByEmail(Email email) {
        return owners.values().stream()
                .filter(owner -> owner.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public void save(Owner owner) {
        owners.put(owner.getId(), owner);
    }

    // === Helpers ===

    public void clear() {
        owners.clear();
    }

    public void saveAll(Owner... owners) {
        for (Owner owner : owners) {
            save(owner);
        }
    }

}
