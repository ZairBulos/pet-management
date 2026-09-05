package com.petmanagement.health.application.service;

import com.petmanagement.health.application.port.in.CreateWeightRecordUseCase;
import com.petmanagement.health.application.port.out.WeightRecordRepositoryPort;
import com.petmanagement.health.domain.exception.PetNotFoundException;
import com.petmanagement.health.domain.model.aggregate.WeightRecord;
import com.petmanagement.health.domain.model.valueobject.WeightRecordId;
import com.petmanagement.pets.api.PetApi;
import org.springframework.stereotype.Service;

@Service
class CreateWeightRecordService implements CreateWeightRecordUseCase {

    private final WeightRecordRepositoryPort repository;
    private final PetApi petApi;

    public CreateWeightRecordService(WeightRecordRepositoryPort repository, PetApi petApi) {
        this.repository = repository;
        this.petApi = petApi;
    }

    @Override
    public WeightRecordId execute(CreateWeightRecord command) {
        var exists = petApi.existsById(command.petId().value());

        if (!exists)
            throw new PetNotFoundException();

        var weightRecord = WeightRecord.create(
                command.petId(),
                command.weightDate(),
                command.weight()
        );

        repository.save(weightRecord);

        return weightRecord.getId();
    }

}
