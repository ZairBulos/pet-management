package com.petmanagement.auth.application.port.in;

import com.petmanagement.auth.domain.model.valueobject.AuthenticationCode;
import com.petmanagement.auth.domain.model.valueobject.Email;

public interface VerifyAuthenticationUseCase {
    VerifyAuthenticationResult execute(VerifyAuthenticationCommand command);

    record VerifyAuthenticationCommand(Email email, AuthenticationCode code) { }

    record VerifyAuthenticationResult(String accessToken, String refreshToken) { }
}
