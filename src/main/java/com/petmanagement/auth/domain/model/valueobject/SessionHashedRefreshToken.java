package com.petmanagement.auth.domain.model.valueobject;

import org.jmolecules.ddd.annotation.ValueObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Objects;

@ValueObject
public record SessionHashedRefreshToken(String value) {

    private static final String ALGORITHM = "SHA-256";

    public SessionHashedRefreshToken {
        Objects.requireNonNull(value, "Session hashed refresh token cannot be null");
    }

    public static SessionHashedRefreshToken from(SessionRefreshToken refreshToken) {
        try {
            Objects.requireNonNull(refreshToken, "Refresh token cannot be null");

            var digest = MessageDigest.getInstance(ALGORITHM);
            var bytes = digest.digest(refreshToken.value().getBytes(StandardCharsets.UTF_8));
            return new SessionHashedRefreshToken(HexFormat.of().formatHex(bytes));
        } catch (NoSuchAlgorithmException _) {
            throw new IllegalStateException("Algorithm not available");
        }
    }

}
