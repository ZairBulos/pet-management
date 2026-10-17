package com.petmanagement.health.application.port.in;

import com.petmanagement.health.domain.model.aggregate.Vaccine;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.shared.domain.model.PageRequest;
import com.petmanagement.shared.domain.model.PageResponse;

public interface GetVaccineHistoryUseCase {
    PageResponse<Vaccine> execute(GetVaccineHistoryQuery query);

    record GetVaccineHistoryQuery(PetId petId, PageRequest pageRequest) {}
}
