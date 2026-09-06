package com.petmanagement.health.support;

import com.petmanagement.health.application.port.out.WeightRecordRepositoryPort;
import com.petmanagement.health.domain.model.aggregate.WeightRecord;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.health.domain.model.valueobject.WeightRecordId;
import com.petmanagement.shared.domain.model.PageRequest;
import com.petmanagement.shared.domain.model.PageResponse;

import java.util.ArrayList;
import java.util.Comparator;
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
    public PageResponse<WeightRecord> findByPetId(PetId petId, PageRequest pageRequest) {
        var allRecords = weightRecords.stream()
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
        weightRecords.add(weightRecord);
    }

    public void clear() {
        weightRecords.clear();
    }

}
