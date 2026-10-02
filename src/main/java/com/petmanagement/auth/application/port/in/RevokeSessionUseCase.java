package com.petmanagement.auth.application.port.in;

import com.petmanagement.auth.domain.model.valueobject.SessionRefreshToken;

public interface RevokeSessionUseCase {
    void execute(RevokeSessionCommand command);

    record RevokeSessionCommand(SessionRefreshToken refreshToken) {}
}
