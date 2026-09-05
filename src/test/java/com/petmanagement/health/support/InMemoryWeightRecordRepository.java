package com.petmanagement.health.support;

import com.petmanagement.health.application.port.out.WeightRecordRepositoryPort;
import com.petmanagement.health.domain.model.aggregate.WeightRecord;

import java.util.ArrayList;
import java.util.List;

public class InMemoryWeightRecordRepository implements WeightRecordRepositoryPort {

    private final List<WeightRecord> weightRecords = new ArrayList<>();

    @Override
    public void save(WeightRecord weightRecord) {
        weightRecords.add(weightRecord);
    }

    public void clear() {
        weightRecords.clear();
    }

}
