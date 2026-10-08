package com.petmanagement.auth.infrastructure.adapter.in.http.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RevokeSessionRequest(

        @NotBlank(message = "The refresh token cannot be blank")
        String refreshToken

) {

    public RevokeSessionRequest {
        refreshToken = refreshToken == null ? null : refreshToken.trim();
    }

}
