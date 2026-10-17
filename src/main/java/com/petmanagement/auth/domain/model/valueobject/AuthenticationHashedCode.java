package com.petmanagement.auth.domain.model.valueobject;

import org.jmolecules.ddd.annotation.ValueObject;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.UnaryOperator;

@ValueObject
public record AuthenticationHashedCode(String value) {

    public AuthenticationHashedCode {
        Objects.requireNonNull(value, "Authentication hashed code cannot not be null");
    }

    public static AuthenticationHashedCode from(String plainCode, UnaryOperator<String> hasher) {
        Objects.requireNonNull(hasher, "Plain code cannot not be null");
        Objects.requireNonNull(plainCode, "Hasher cannot not be null");

        return new AuthenticationHashedCode(hasher.apply(plainCode));
    }

    public boolean matches(String plainCode, BiPredicate<String, String> verifier) {
        Objects.requireNonNull(plainCode, "Plain code cannot not be null");
        Objects.requireNonNull(verifier, "Verifier cannot not be null");

        return verifier.test(plainCode, value);
    }

}
