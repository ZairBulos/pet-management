package com.petmanagement.auth.infrastructure.adapter.in.http.mapper;

import com.petmanagement.auth.application.port.in.RefreshSessionUseCase;
import com.petmanagement.auth.application.port.in.RequestAuthenticationUseCase;
import com.petmanagement.auth.application.port.in.RevokeSessionUseCase;
import com.petmanagement.auth.application.port.in.VerifyAuthenticationUseCase;
import com.petmanagement.auth.domain.model.valueobject.AuthenticationCode;
import com.petmanagement.auth.domain.model.valueobject.Email;
import com.petmanagement.auth.domain.model.valueobject.SessionRefreshToken;
import com.petmanagement.auth.infrastructure.adapter.in.http.dto.request.RefreshSessionRequest;
import com.petmanagement.auth.infrastructure.adapter.in.http.dto.request.RequestAuthenticationRequest;
import com.petmanagement.auth.infrastructure.adapter.in.http.dto.request.RevokeSessionRequest;
import com.petmanagement.auth.infrastructure.adapter.in.http.dto.request.VerifyAuthenticationRequest;
import com.petmanagement.auth.infrastructure.adapter.in.http.dto.response.TokenResponse;
import org.springframework.stereotype.Component;

@Component
public class AuthHttpMapper {

    public RequestAuthenticationUseCase.RequestAuthenticationCommand toRequestAuthenticationCommand(
            RequestAuthenticationRequest request
    ) {
        return new RequestAuthenticationUseCase.RequestAuthenticationCommand(new Email(request.email()));
    }

    public VerifyAuthenticationUseCase.VerifyAuthenticationCommand toVerifyAuthenticationCommand(
            VerifyAuthenticationRequest request
    ) {
        return new VerifyAuthenticationUseCase.VerifyAuthenticationCommand(
                new Email(request.email()),
                new AuthenticationCode(request.code())
        );
    }

    public RefreshSessionUseCase.RefreshSessionCommand toRefreshSessionCommand(
            RefreshSessionRequest request
    ) {
        return new RefreshSessionUseCase.RefreshSessionCommand(new SessionRefreshToken(request.refreshToken()));
    }

    public RevokeSessionUseCase.RevokeSessionCommand toRevokeSessionCommand(
            RevokeSessionRequest request
    ) {
        return new RevokeSessionUseCase.RevokeSessionCommand(new SessionRefreshToken(request.refreshToken()));
    }

    public TokenResponse toTokenResponse(
            VerifyAuthenticationUseCase.VerifyAuthenticationResult result
    ) {
        return new TokenResponse(result.accessToken(), result.refreshToken());
    }

    public TokenResponse toTokenResponse(
            RefreshSessionUseCase.RefreshSessionResult result
    ) {
        return new TokenResponse(result.accessToken(), result.refreshToken());
    }

}
