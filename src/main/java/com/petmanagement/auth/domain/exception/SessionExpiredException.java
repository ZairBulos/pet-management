package com.petmanagement.auth.domain.exception;

public class SessionExpiredException extends RuntimeException {
    public SessionExpiredException() {
        super("Session has expired");
    }
}
