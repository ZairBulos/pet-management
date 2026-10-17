package com.petmanagement.health.infrastructure.adapter.in.http.mapper;

import com.petmanagement.health.application.port.in.CreateDewormingUseCase;
import com.petmanagement.health.application.port.in.GetDewormingHistoryUseCase;
import com.petmanagement.health.application.port.in.RescheduleDewormingUseCase;
import com.petmanagement.health.domain.model.aggregate.Deworming;
import com.petmanagement.health.domain.model.valueobject.*;
import com.petmanagement.health.infrastructure.adapter.in.http.dto.request.CreateDewormingRequest;
import com.petmanagement.health.infrastructure.adapter.in.http.dto.request.RescheduleDewormingRequest;
import com.petmanagement.health.infrastructure.adapter.in.http.dto.response.DewormingResponse;
import com.petmanagement.shared.domain.model.PageRequest;
import com.petmanagement.shared.domain.model.PageResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

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

    public GetDewormingHistoryUseCase.GetDewormingHistoryQuery toGetDewormingHistoryQuery(
            UUID petId, int page, int size
    ) {
        return new GetDewormingHistoryUseCase.GetDewormingHistoryQuery(
                PetId.of(petId),
                PageRequest.of(page, size)
        );
    }

    public RescheduleDewormingUseCase.RescheduleDewormingCommand toRescheduleDewormingCommand(
            RescheduleDewormingRequest request
    ) {
        return new RescheduleDewormingUseCase.RescheduleDewormingCommand(
                DewormingId.of(request.dewormingId()),
                request.nextDueDate()
        );
    }

    public PageResponse<DewormingResponse> toDewormingHistoryResponse(PageResponse<Deworming> page) {
        return PageResponse.of(
                page.content().stream().map(this::toDewormingResponse).toList(),
                page.page(),
                page.size(),
                page.totalElements()
        );
    }

    public DewormingResponse toDewormingResponse(Deworming deworming) {
        return new DewormingResponse(
                deworming.id().value(),
                deworming.dewormingDate(),
                deworming.drugName().value(),
                deworming.drugDose().value(),
                deworming.nextDueDate().value()
        );
    }

}
