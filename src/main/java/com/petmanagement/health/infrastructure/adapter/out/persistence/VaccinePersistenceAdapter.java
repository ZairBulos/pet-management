package com.petmanagement.health.infrastructure.adapter.out.persistence;

import com.petmanagement.health.application.port.out.VaccineRepositoryPort;
import com.petmanagement.health.domain.model.aggregate.Vaccine;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.health.domain.model.valueobject.VaccineId;
import com.petmanagement.health.infrastructure.adapter.out.persistence.mapper.VaccineMapper;
import com.petmanagement.health.infrastructure.adapter.out.persistence.repository.VaccineJpaRepository;
import com.petmanagement.shared.domain.model.PageRequest;
import com.petmanagement.shared.domain.model.PageResponse;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class VaccinePersistenceAdapter implements VaccineRepositoryPort {

    private final VaccineJpaRepository repository;
    private final VaccineMapper mapper;

    public VaccinePersistenceAdapter(VaccineJpaRepository repository, VaccineMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Vaccine> findById(VaccineId vaccineId) {
        return repository.findById(vaccineId.value())
                .map(mapper::toDomain);
    }

    @Override
    public PageResponse<Vaccine> findByPetId(PetId petId, PageRequest pageRequest) {
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
    public void save(Vaccine vaccine) {
        repository.save(mapper.toJpaEntity(vaccine));
    }

}
