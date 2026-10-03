package com.petmanagement.auth.infrastructure.adapter.out.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.petmanagement.auth.application.port.out.TokenProviderPort;
import com.petmanagement.auth.domain.model.valueobject.OwnerId;
import com.petmanagement.auth.infrastructure.config.JwtProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.Clock;
import java.util.*;

@Component
class JwtTokenProvider implements TokenProviderPort {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    private static final int REFRESH_TOKEN_BYTES = 32;

    private final SecureRandom random = new SecureRandom();
    private final JwtProperties properties;
    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final Clock clock;

    JwtTokenProvider(JwtProperties properties, Clock clock) {
        this.properties = properties;
        this.clock = clock;
        this.algorithm = Algorithm.HMAC256(properties.secret());
        var verification = (JWTVerifier.BaseVerification) JWT.require(algorithm)
                .withIssuer(properties.issuer())
                .withAudience(properties.audience());
        this.verifier = verification.build(clock);
    }

    @Override
    public AuthTokens generate(OwnerId ownerId) {
        Objects.requireNonNull(ownerId, "ownerId cannot be null");

        return new AuthTokens(
                generateAccessToken(ownerId),
                generateRefreshToken()
        );
    }

    @Override
    public Optional<OwnerId> verifyAccessToken(String accessToken) {
        if (accessToken == null || accessToken.isBlank())
            return Optional.empty();

        try {
            var decodedJwt = verifier.verify(accessToken);

            return Optional.of(OwnerId.of(decodedJwt.getSubject()));
        } catch (JWTVerificationException e) {
            log.error("Invalid, malformed, or expired JWT token={}", e.getMessage());

            return Optional.empty();
        }
    }

    // === Helpers ===

    private String generateAccessToken(OwnerId ownerId) {
        var now = clock.instant();

        return JWT.create()
                .withHeader(Map.of(
                        "alg", "HS256",
                        "typ", "JWT"
                ))
                .withIssuer(properties.issuer())
                .withAudience(properties.audience())
                .withSubject(ownerId.value().toString())
                .withJWTId(UUID.randomUUID().toString())
                .withIssuedAt(now)
                .withNotBefore(now)
                .withExpiresAt(now.plus(properties.accessTokenTtl()))
                .sign(algorithm);
    }

    private String generateRefreshToken() {
        var bytes = new byte[REFRESH_TOKEN_BYTES];
        random.nextBytes(bytes);

        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

}
