package com.petmanagement.health.application.service;

import com.petmanagement.health.application.port.in.UpdateWeightRecordUseCase;
import com.petmanagement.health.application.port.out.WeightRecordRepositoryPort;
import com.petmanagement.health.domain.exception.WeightRecordNotFoundException;
import com.petmanagement.health.domain.model.aggregate.WeightRecord;
import org.springframework.stereotype.Service;

@Service
class UpdateWeightRecordService implements UpdateWeightRecordUseCase {

    private final WeightRecordRepositoryPort repository;

    public UpdateWeightRecordService(WeightRecordRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public WeightRecord execute(UpdateWeightRecordCommand command) {
        var weightRecord = repository.findById(command.weightRecordId())
                .orElseThrow(WeightRecordNotFoundException::new);

        weightRecord.update(command.weightDate(), command.weight());

        repository.save(weightRecord);

        return weightRecord;
    }

}
