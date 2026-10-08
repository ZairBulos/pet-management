package com.petmanagement.auth.infrastructure.adapter.in.http.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshSessionRequest(

        @NotBlank(message = "The refresh token cannot be blank")
        String refreshToken

) {

    public RefreshSessionRequest {
        refreshToken = refreshToken == null ? null : refreshToken.trim();
    }

}
