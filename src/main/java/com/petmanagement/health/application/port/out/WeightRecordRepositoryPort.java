package com.petmanagement.health.application.port.out;

import com.petmanagement.health.domain.model.aggregate.WeightRecord;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.health.domain.model.valueobject.WeightRecordId;
import com.petmanagement.shared.domain.model.PageRequest;
import com.petmanagement.shared.domain.model.PageResponse;

import java.util.Optional;

public interface WeightRecordRepositoryPort {
    Optional<WeightRecord> findById(WeightRecordId weightRecordId);
    PageResponse<WeightRecord> findByPetId(PetId petId, PageRequest pageRequest);
    void save(WeightRecord weightRecord);
}
