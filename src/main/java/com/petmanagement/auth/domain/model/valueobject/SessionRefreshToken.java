package com.petmanagement.auth.domain.model.valueobject;

import org.jmolecules.ddd.annotation.ValueObject;

import java.util.Objects;

@ValueObject
public record SessionRefreshToken(String value) {

    public SessionRefreshToken {
        Objects.requireNonNull(value, "Session refresh token cannot be null");
    }

}
