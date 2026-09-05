package com.petmanagement.health.application.port.out;

import com.petmanagement.health.domain.model.aggregate.WeightRecord;
import com.petmanagement.health.domain.model.valueobject.WeightRecordId;

import java.util.Optional;

public interface WeightRecordRepositoryPort {
    Optional<WeightRecord> findById(WeightRecordId weightRecordId);
    void save(WeightRecord weightRecord);
}
