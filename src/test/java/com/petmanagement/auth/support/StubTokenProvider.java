package com.petmanagement.auth.support;

import com.petmanagement.auth.application.port.out.TokenProviderPort;
import com.petmanagement.auth.domain.model.valueobject.OwnerId;

import java.util.Optional;

public final class StubTokenProvider implements TokenProviderPort {

    public static final String DEFAULT_ACCESS_TOKEN = "default-access-token";
    public static final String DEFAULT_REFRESH_TOKEN = TestSessionMother.DEFAULT_PLAIN_REFRESH_TOKEN.value();

    @Override
    public AuthTokens generate(OwnerId ownerId) {
        return new AuthTokens(
                DEFAULT_ACCESS_TOKEN,
                DEFAULT_REFRESH_TOKEN
        );
    }

    @Override
    public Optional<OwnerId> verifyAccessToken(String accessToken) {
        return DEFAULT_ACCESS_TOKEN.equals(accessToken)
                ? Optional.of(TestSessionMother.EXISTING_OWNER_ID)
                : Optional.empty();
    }

}
