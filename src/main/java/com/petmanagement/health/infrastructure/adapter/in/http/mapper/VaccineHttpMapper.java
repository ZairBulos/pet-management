package com.petmanagement.health.infrastructure.adapter.in.http.mapper;

import com.petmanagement.health.application.port.in.CreateVaccineUseCase;
import com.petmanagement.health.application.port.in.GetVaccineHistoryUseCase;
import com.petmanagement.health.domain.model.aggregate.Vaccine;
import com.petmanagement.health.domain.model.valueobject.NextDueDate;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.health.domain.model.valueobject.VaccineName;
import com.petmanagement.health.infrastructure.adapter.in.http.dto.request.CreateVaccineRequest;
import com.petmanagement.health.infrastructure.adapter.in.http.dto.response.VaccineResponse;
import com.petmanagement.shared.domain.model.PageRequest;
import com.petmanagement.shared.domain.model.PageResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class VaccineHttpMapper {

    public CreateVaccineUseCase.CreateVaccineCommand toCreateVaccineCommand(CreateVaccineRequest request) {
        return new CreateVaccineUseCase.CreateVaccineCommand(
                PetId.of(request.petId()),
                request.vaccinationDate(),
                new VaccineName(request.vaccineName()),
                new NextDueDate(request.nextDueDate())
        );
    }

    public GetVaccineHistoryUseCase.GetVaccineHistoryQuery toGetVaccineHistoryQuery(
            UUID petId, int page, int size
    ) {
        return new GetVaccineHistoryUseCase.GetVaccineHistoryQuery(
                PetId.of(petId),
                PageRequest.of(page, size)
        );
    }

    public PageResponse<VaccineResponse> toVaccineHistoryResponse(PageResponse<Vaccine> page) {
        return PageResponse.of(
                page.content().stream().map(this::toVaccineResponse).toList(),
                page.page(),
                page.size(),
                page.totalElements()
        );
    }

    private VaccineResponse toVaccineResponse(Vaccine vaccine) {
        return new VaccineResponse(
                vaccine.id().value(),
                vaccine.vaccinationDate(),
                vaccine.vaccineName().value(),
                vaccine.nextDueDate().value()
        );
    }

}
