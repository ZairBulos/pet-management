package com.petmanagement.owners.infrastructure.adapter.in.http.mapper;

import com.petmanagement.owners.application.port.in.CreateOwnerUseCase;
import com.petmanagement.owners.application.port.in.GetOwnerUseCase;
import com.petmanagement.owners.application.port.in.UpdateOwnerUseCase;
import com.petmanagement.owners.domain.model.aggregate.Owner;
import com.petmanagement.owners.domain.model.valueobject.Email;
import com.petmanagement.owners.domain.model.valueobject.OwnerId;
import com.petmanagement.owners.domain.model.valueobject.OwnerName;
import com.petmanagement.owners.domain.model.valueobject.PhoneNumber;
import com.petmanagement.owners.infrastructure.adapter.in.http.dto.request.CreateOwnerRequest;
import com.petmanagement.owners.infrastructure.adapter.in.http.dto.request.UpdateOwnerRequest;
import com.petmanagement.owners.infrastructure.adapter.in.http.dto.response.OwnerResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OwnerHttpMapper {

    public CreateOwnerUseCase.CreateOwnerCommand toCreateOwnerCommand(CreateOwnerRequest request) {
        return new CreateOwnerUseCase.CreateOwnerCommand(
                new OwnerName(request.name()),
                new Email(request.email()),
                new PhoneNumber(request.phone())
        );
    }

    public GetOwnerUseCase.GetOwnerCommand toGetOwnerUseCase(UUID ownerId) {
        return new GetOwnerUseCase.GetOwnerCommand(OwnerId.of(ownerId));
    }

    public UpdateOwnerUseCase.UpdateOwnerCommand toUpdateOwnerCommand(UUID ownerId, UpdateOwnerRequest request) {
        return new UpdateOwnerUseCase.UpdateOwnerCommand(
                OwnerId.of(ownerId),
                new OwnerName(request.name()),
                new Email(request.email()),
                new PhoneNumber(request.phone())
        );
    }

    public OwnerResponse toOwnerResponse(Owner owner) {
        return new OwnerResponse(
                owner.getName().value(),
                owner.getEmail().value(),
                owner.getPhone().value()
        );
    }

}
