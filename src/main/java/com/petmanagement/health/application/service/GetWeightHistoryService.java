package com.petmanagement.health.application.service;

import com.petmanagement.health.application.port.in.GetWeightHistoryUseCase;
import com.petmanagement.health.application.port.out.WeightRecordRepositoryPort;
import com.petmanagement.health.domain.exception.PetNotFoundException;
import com.petmanagement.health.domain.model.aggregate.WeightRecord;
import com.petmanagement.pets.api.PetApi;
import com.petmanagement.shared.domain.model.PageResponse;
import org.springframework.stereotype.Service;

@Service
class GetWeightHistoryService implements GetWeightHistoryUseCase {

    private final WeightRecordRepositoryPort repository;
    private final PetApi petApi;

    public GetWeightHistoryService(WeightRecordRepositoryPort repository, PetApi petApi) {
        this.repository = repository;
        this.petApi = petApi;
    }

    @Override
    public PageResponse<WeightRecord> execute(GetWeightHistoryQuery query) {
        var petExists = petApi.existsById(query.petId().value());

        if (!petExists)
            throw new PetNotFoundException();

        return repository.findByPetId(query.petId(), query.pageRequest());
    }

}
