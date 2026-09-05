package com.petmanagement.health.application.port.out;

import com.petmanagement.health.domain.model.aggregate.WeightRecord;

public interface WeightRecordRepositoryPort {
    void save(WeightRecord weightRecord);
}
