package com.petmanagement.owners.infrastructure.adapter.in.http.mapper;

import com.petmanagement.owners.application.port.in.CreateOwnerUseCase;
import com.petmanagement.owners.domain.model.valueobject.Email;
import com.petmanagement.owners.domain.model.valueobject.OwnerName;
import com.petmanagement.owners.domain.model.valueobject.PhoneNumber;
import com.petmanagement.owners.infrastructure.adapter.in.http.dto.request.CreateOwnerRequest;
import org.springframework.stereotype.Component;

@Component
public class OwnerHttpMapper {

    public CreateOwnerUseCase.CreateOwnerCommand toCreateOwnerCommand(CreateOwnerRequest request) {
        return new CreateOwnerUseCase.CreateOwnerCommand(
                new OwnerName(request.name()),
                new Email(request.email()),
                new PhoneNumber(request.phone())
        );
    }

}
