package com.petmanagement.owners.application.port.in;

import com.petmanagement.owners.domain.model.aggregate.Owner;
import com.petmanagement.owners.domain.model.valueobject.OwnerId;

public interface GetOwnerUseCase {
    Owner execute(GetOwnerCommand command);

    record GetOwnerCommand(OwnerId ownerId) {}
}
