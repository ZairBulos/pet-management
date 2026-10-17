package com.petmanagement.auth.application.port.in;

import com.petmanagement.auth.domain.model.valueobject.SessionRefreshToken;

public interface RefreshSessionUseCase {
    RefreshSessionResult execute(RefreshSessionCommand command);

    record RefreshSessionCommand(SessionRefreshToken refreshToken) {}

    record RefreshSessionResult(String accessToken, String refreshToken) {}
}
