package com.petmanagement.health.application.port.in;

import com.petmanagement.health.domain.model.aggregate.WeightRecord;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.shared.domain.model.PageRequest;
import com.petmanagement.shared.domain.model.PageResponse;

public interface GetWeightHistoryUseCase {
    PageResponse<WeightRecord> execute(GetWeightHistoryQuery query);

    record GetWeightHistoryQuery(PetId petId, PageRequest pageRequest) {}
}
