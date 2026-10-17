package com.petmanagement.auth.domain.model.valueobject;

import org.jmolecules.ddd.annotation.ValueObject;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@ValueObject
public record AuthenticationCode(String value) {

    public AuthenticationCode {
        Objects.requireNonNull(value, "Authentication code cannot not be null");
    }

    public static AuthenticationCode generate() {
        var random = ThreadLocalRandom.current().nextInt(100000, 999999);
        return new AuthenticationCode(String.valueOf(random));
    }

}
