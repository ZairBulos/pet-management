package com.petmanagement.health.infrastructure.adapter.in.http.mapper;

import com.petmanagement.health.application.port.in.CreateDewormingUseCase;
import com.petmanagement.health.domain.model.valueobject.DrugDose;
import com.petmanagement.health.domain.model.valueobject.DrugName;
import com.petmanagement.health.domain.model.valueobject.NextDueDate;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.health.infrastructure.adapter.in.http.dto.request.CreateDewormingRequest;
import org.springframework.stereotype.Component;

@Component
public class DewormingHttpMapper {

    public CreateDewormingUseCase.CreateDewormingCommand toCreateDewormingCommand(CreateDewormingRequest request) {
        return new CreateDewormingUseCase.CreateDewormingCommand(
                PetId.of(request.petId()),
                request.dewormingDate(),
                new DrugName(request.drugName()),
                new DrugDose(request.drugDose()),
                new NextDueDate(request.nextDueDate())
        );
    }

}
