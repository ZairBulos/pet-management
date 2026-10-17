package com.petmanagement.auth.domain.exception;

public class AuthenticationAlreadyUsedException extends RuntimeException {
    public AuthenticationAlreadyUsedException() {
        super("Authentication code has already been used");
    }
}
