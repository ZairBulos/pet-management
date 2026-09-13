package com.petmanagement.health.support;

import com.petmanagement.health.application.port.out.WeightRecordRepositoryPort;
import com.petmanagement.health.domain.model.aggregate.WeightRecord;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.health.domain.model.valueobject.WeightRecordId;
import com.petmanagement.shared.domain.model.PageRequest;
import com.petmanagement.shared.domain.model.PageResponse;

import java.util.*;

public final class InMemoryWeightRecordRepository implements WeightRecordRepositoryPort {

    private final Map<WeightRecordId, WeightRecord> weightRecords = new HashMap<>();

    @Override
    public Optional<WeightRecord> findById(WeightRecordId weightRecordId) {
        return Optional.ofNullable(weightRecords.get(weightRecordId));
    }

    @Override
    public PageResponse<WeightRecord> findByPetId(PetId petId, PageRequest pageRequest) {
        var allRecords = weightRecords.values().stream()
                .filter(weightRecord -> weightRecord.getPetId().equals(petId))
                .sorted(Comparator.comparing(WeightRecord::getWeightDate).reversed())
                .toList();

        var start = pageRequest.page() * pageRequest.size();
        var end = Math.min(start + pageRequest.size(), allRecords.size());

        var pageContent = start >= allRecords.size()
                ? List.<WeightRecord>of()
                : allRecords.subList(start, end);

        return PageResponse.of(
                pageContent,
                pageRequest.page(),
                pageRequest.size(),
                allRecords.size()
        );
    }

    @Override
    public void save(WeightRecord weightRecord) {
        weightRecords.put(weightRecord.getId(), weightRecord);
    }

    // === Helpers ===

    public void clear() {
        weightRecords.clear();
    }

    public void saveAll(WeightRecord... weightRecords) {
        for (WeightRecord weightRecord : weightRecords) {
            save(weightRecord);
        }
    }

}
