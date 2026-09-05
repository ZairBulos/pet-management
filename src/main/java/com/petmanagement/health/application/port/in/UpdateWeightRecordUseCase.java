package com.petmanagement.health.application.port.in;

import com.petmanagement.health.domain.model.aggregate.WeightRecord;
import com.petmanagement.health.domain.model.valueobject.Weight;
import com.petmanagement.health.domain.model.valueobject.WeightRecordId;

import java.time.LocalDate;

public interface UpdateWeightRecordUseCase {
    WeightRecord execute(UpdateWeightRecordCommand command);

    record UpdateWeightRecordCommand(
            WeightRecordId weightRecordId,
            LocalDate weightDate,
            Weight weight
    ) {}
}
