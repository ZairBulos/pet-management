package com.petmanagement.health.infrastructure.adapter.in.http.mapper;

import com.petmanagement.health.application.port.in.CreateVaccineUseCase;
import com.petmanagement.health.domain.model.valueobject.NextDueDate;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.health.domain.model.valueobject.VaccineName;
import com.petmanagement.health.infrastructure.adapter.in.http.dto.request.CreateVaccineRequest;
import org.springframework.stereotype.Component;

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

}
