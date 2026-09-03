package com.petmanagement.owners.support;

import com.petmanagement.owners.application.port.out.OwnerRepositoryPort;
import com.petmanagement.owners.domain.model.aggregate.Owner;
import com.petmanagement.owners.domain.model.valueobject.Email;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryOwnerRepository implements OwnerRepositoryPort {

    private final List<Owner> owners = new ArrayList<>();

    @Override
    public Optional<Owner> findByEmail(Email email) {
        return owners.stream()
                .filter(owner -> owner.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public void save(Owner owner) {
        owners.add(owner);
    }

    public void clear() {
        owners.clear();
    }

}
