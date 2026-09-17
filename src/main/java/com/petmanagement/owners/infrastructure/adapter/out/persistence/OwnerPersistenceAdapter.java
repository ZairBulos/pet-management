package com.petmanagement.owners.infrastructure.adapter.out.persistence;

import com.petmanagement.owners.application.port.out.OwnerRepositoryPort;
import com.petmanagement.owners.domain.model.aggregate.Owner;
import com.petmanagement.owners.domain.model.valueobject.Email;
import com.petmanagement.owners.domain.model.valueobject.OwnerId;
import com.petmanagement.owners.infrastructure.adapter.out.persistence.mapper.OwnerMapper;
import com.petmanagement.owners.infrastructure.adapter.out.persistence.repository.OwnerJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class OwnerPersistenceAdapter implements OwnerRepositoryPort {

    private final OwnerJpaRepository repository;
    private final OwnerMapper mapper;

    public OwnerPersistenceAdapter(OwnerJpaRepository repository, OwnerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Owner> findById(OwnerId ownerId) {
        return repository.findById(ownerId.value())
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Owner> findByEmail(Email email) {
        return repository.findByEmail(email.value())
                .map(mapper::toDomain);
    }

    @Override
    public void save(Owner owner) {
        repository.save(mapper.toJpaEntity(owner));
    }

}
