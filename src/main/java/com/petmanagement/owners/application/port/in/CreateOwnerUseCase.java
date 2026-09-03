package com.petmanagement.owners.application.port.in;

import com.petmanagement.owners.domain.model.valueobject.Email;
import com.petmanagement.owners.domain.model.valueobject.OwnerId;
import com.petmanagement.owners.domain.model.valueobject.OwnerName;
import com.petmanagement.owners.domain.model.valueobject.PhoneNumber;

public interface CreateOwnerUseCase {
    OwnerId execute(CreateOwnerCommand command);

    record CreateOwnerCommand(OwnerName name, Email email, PhoneNumber phone) {}
}
