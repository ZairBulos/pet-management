package com.petmanagement.auth.application.port.out;

import com.petmanagement.auth.domain.model.valueobject.OwnerId;

import java.util.Objects;

public interface TokenProviderPort {
    AuthTokens generate(OwnerId ownerId);

    record AuthTokens(String accessToken, String refreshToken) {

        public AuthTokens {
            Objects.requireNonNull(accessToken, "Access token cannot be null");
            Objects.requireNonNull(refreshToken, "Refresh token cannot be null");
        }

    }
}
