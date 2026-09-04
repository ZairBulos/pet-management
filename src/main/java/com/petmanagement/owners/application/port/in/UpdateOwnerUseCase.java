package com.petmanagement.owners.application.port.in;

import com.petmanagement.owners.domain.model.aggregate.Owner;
import com.petmanagement.owners.domain.model.valueobject.Email;
import com.petmanagement.owners.domain.model.valueobject.OwnerId;
import com.petmanagement.owners.domain.model.valueobject.OwnerName;
import com.petmanagement.owners.domain.model.valueobject.PhoneNumber;

public interface UpdateOwnerUseCase {
    Owner execute(UpdateOwnerCommand command);

    record UpdateOwnerCommand(OwnerId ownerId, OwnerName name, Email email, PhoneNumber phone) {}
}
