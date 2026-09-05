package com.petmanagement.health.application.port.in;

import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.health.domain.model.valueobject.Weight;
import com.petmanagement.health.domain.model.valueobject.WeightRecordId;

import java.time.LocalDate;

public interface CreateWeightRecordUseCase {
    WeightRecordId execute(CreateWeightRecord command);

    record CreateWeightRecord(
            PetId petId,
            LocalDate weightDate,
            Weight weight
    ) {}
}
