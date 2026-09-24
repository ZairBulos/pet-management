package com.petmanagement.auth.application.port.in;

import com.petmanagement.auth.domain.model.valueobject.Email;

public interface RequestAuthenticationUseCase {
    void execute(RequestAuthenticationCommand command);

    record RequestAuthenticationCommand(Email email) {}
}
