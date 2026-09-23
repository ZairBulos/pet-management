package com.petmanagement.auth.domain.exception;

public class AuthenticationExpiredException extends RuntimeException {
    public AuthenticationExpiredException() {
        super("Authentication has expired");
    }
}
