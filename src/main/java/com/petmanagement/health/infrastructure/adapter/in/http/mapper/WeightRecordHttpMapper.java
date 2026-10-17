package com.petmanagement.health.infrastructure.adapter.in.http.mapper;

import com.petmanagement.health.application.port.in.CreateWeightRecordUseCase;
import com.petmanagement.health.application.port.in.GetWeightHistoryUseCase;
import com.petmanagement.health.domain.model.aggregate.WeightRecord;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.health.domain.model.valueobject.Weight;
import com.petmanagement.health.infrastructure.adapter.in.http.dto.request.CreateWeightRecordRequest;
import com.petmanagement.health.infrastructure.adapter.in.http.dto.response.WeightRecordResponse;
import com.petmanagement.shared.domain.model.PageRequest;
import com.petmanagement.shared.domain.model.PageResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

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

    public GetWeightHistoryUseCase.GetWeightHistoryQuery toGetWeightHistoryQuery(
            UUID petId, int page, int size
    ) {
        return new GetWeightHistoryUseCase.GetWeightHistoryQuery(
                PetId.of(petId),
                PageRequest.of(page, size)
        );
    }

    public PageResponse<WeightRecordResponse> toWeightHistoryResponse(PageResponse<WeightRecord> page) {
        return PageResponse.of(
                page.content().stream().map(this::toWeightRecordResponse).toList(),
                page.page(),
                page.size(),
                page.totalElements()
        );
    }

    private WeightRecordResponse toWeightRecordResponse(WeightRecord weightRecord) {
        return new WeightRecordResponse(
                weightRecord.id().value(),
                weightRecord.weightDate(),
                weightRecord.weight().value()
        );
    }

}
