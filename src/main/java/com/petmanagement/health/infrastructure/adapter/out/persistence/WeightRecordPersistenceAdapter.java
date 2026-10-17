package com.petmanagement.health.infrastructure.adapter.out.persistence;

import com.petmanagement.health.application.port.out.WeightRecordRepositoryPort;
import com.petmanagement.health.domain.model.aggregate.WeightRecord;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.health.domain.model.valueobject.WeightRecordId;
import com.petmanagement.health.infrastructure.adapter.out.persistence.mapper.WeightRecordMapper;
import com.petmanagement.health.infrastructure.adapter.out.persistence.repository.WeightRecordJpaRepository;
import com.petmanagement.shared.domain.model.PageRequest;
import com.petmanagement.shared.domain.model.PageResponse;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class WeightRecordPersistenceAdapter implements WeightRecordRepositoryPort {

    private final WeightRecordJpaRepository repository;
    private final WeightRecordMapper mapper;

    public WeightRecordPersistenceAdapter(WeightRecordJpaRepository repository, WeightRecordMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<WeightRecord> findById(WeightRecordId weightRecordId) {
        return repository.findById(weightRecordId.value())
                .map(mapper::toDomain);
    }

    @Override
    public PageResponse<WeightRecord> findByPetId(PetId petId, PageRequest pageRequest) {
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
    public void save(WeightRecord weightRecord) {
        repository.save(mapper.toJpaEntity(weightRecord));
    }

}
