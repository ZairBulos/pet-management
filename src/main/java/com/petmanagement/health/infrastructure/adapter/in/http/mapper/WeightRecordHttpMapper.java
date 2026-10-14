package com.petmanagement.health.infrastructure.adapter.in.http.mapper;

import com.petmanagement.health.application.port.in.CreateWeightRecordUseCase;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.health.domain.model.valueobject.Weight;
import com.petmanagement.health.infrastructure.adapter.in.http.dto.request.CreateWeightRecordRequest;
import org.springframework.stereotype.Component;

@Component
public class WeightRecordHttpMapper {

    public CreateWeightRecordUseCase.CreateWeightRecordCommand toCreateWeightRecordCommand(
            CreateWeightRecordRequest request
    ) {
        return new CreateWeightRecordUseCase.CreateWeightRecordCommand(
                PetId.of(request.petId()),
                request.weightDate(),
                Weight.of(request.weight())
        );
    }

}
