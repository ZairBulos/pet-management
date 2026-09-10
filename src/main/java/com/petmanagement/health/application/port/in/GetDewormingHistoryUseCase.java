package com.petmanagement.health.application.port.in;

import com.petmanagement.health.domain.model.aggregate.Deworming;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.shared.domain.model.PageRequest;
import com.petmanagement.shared.domain.model.PageResponse;

public interface GetDewormingHistoryUseCase {
    PageResponse<Deworming> execute(GetDewormingHistoryQuery query);

    record GetDewormingHistoryQuery(PetId petId, PageRequest pageRequest) {}
}
