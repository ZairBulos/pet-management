package com.petmanagement.auth.infrastructure.adapter.in.http.dto.response;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
