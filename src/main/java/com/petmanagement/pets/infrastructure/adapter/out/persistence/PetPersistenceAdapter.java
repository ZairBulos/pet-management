package com.petmanagement.pets.infrastructure.adapter.out.persistence;

import com.petmanagement.pets.application.port.out.PetRepositoryPort;
import com.petmanagement.pets.domain.model.aggregate.Pet;
import com.petmanagement.pets.domain.model.valueobject.OwnerId;
import com.petmanagement.pets.domain.model.valueobject.PetId;
import com.petmanagement.pets.infrastructure.adapter.out.persistence.mapper.PetMapper;
import com.petmanagement.pets.infrastructure.adapter.out.persistence.repository.PetJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
class PetPersistenceAdapter implements PetRepositoryPort {

    private final PetJpaRepository repository;
    private final PetMapper mapper;

    public PetPersistenceAdapter(PetJpaRepository repository, PetMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Pet> findById(PetId petId) {
        return repository.findById(petId.value())
                .map(mapper::toDomain);
    }

    @Override
    public List<Pet> findByOwnerId(OwnerId ownerId) {
        return repository.findByOwnerId(ownerId.value())
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void save(Pet pet) {
        repository.save(mapper.toJpaEntity(pet));
    }

}
