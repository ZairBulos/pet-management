package com.petmanagement.health.support;

import com.petmanagement.health.application.port.out.WeightRecordRepositoryPort;
import com.petmanagement.health.domain.model.aggregate.WeightRecord;
import com.petmanagement.health.domain.model.valueobject.WeightRecordId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryWeightRecordRepository implements WeightRecordRepositoryPort {

    private final List<WeightRecord> weightRecords = new ArrayList<>();

    @Override
    public Optional<WeightRecord> findById(WeightRecordId weightRecordId) {
        return weightRecords.stream()
                .filter(weightRecord -> weightRecord.getId().equals(weightRecordId))
                .findFirst();
    }

    @Override
    public void save(WeightRecord weightRecord) {
        weightRecords.add(weightRecord);
    }

    public void clear() {
        weightRecords.clear();
    }

}
