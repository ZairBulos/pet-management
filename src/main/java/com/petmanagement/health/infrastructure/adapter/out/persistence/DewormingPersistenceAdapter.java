package com.petmanagement.health.infrastructure.adapter.out.persistence;

import com.petmanagement.health.application.port.out.DewormingRepositoryPort;
import com.petmanagement.health.domain.model.aggregate.Deworming;
import com.petmanagement.health.domain.model.valueobject.DewormingId;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.health.infrastructure.adapter.out.persistence.mapper.DewormingMapper;
import com.petmanagement.health.infrastructure.adapter.out.persistence.repository.DewormingJpaRepository;
import com.petmanagement.shared.domain.model.PageRequest;
import com.petmanagement.shared.domain.model.PageResponse;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class DewormingPersistenceAdapter implements DewormingRepositoryPort {

    private final DewormingJpaRepository repository;
    private final DewormingMapper mapper;

    public DewormingPersistenceAdapter(DewormingJpaRepository repository, DewormingMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Deworming> findById(DewormingId dewormingId) {
        return repository.findById(dewormingId.value())
                .map(mapper::toDomain);
    }

    @Override
    public PageResponse<Deworming> findByPetId(PetId petId, PageRequest pageRequest) {
        var page = repository.findByPetId(
                petId.value(),
                org.springframework.data.domain.PageRequest.of(pageRequest.page(), pageRequest.size())
        );

        var content = page.getContent()
                .stream()
                .map(mapper::toDomain)
                .toList();

        return PageResponse.of(
                content,
                pageRequest.page(),
                pageRequest.size(),
                page.getTotalElements()
        );
    }

    @Override
    public void save(Deworming deworming) {
        repository.save(mapper.toJpaEntity(deworming));
    }

}
