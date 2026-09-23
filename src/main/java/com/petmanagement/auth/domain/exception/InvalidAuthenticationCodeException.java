package com.petmanagement.auth.domain.exception;

public class InvalidAuthenticationCodeException extends RuntimeException {
    public InvalidAuthenticationCodeException() {
        super("The provided authentication code is invalid");
    }
}
