package com.petmanagement.auth.domain.exception;

public class SessionReuseDetectedException extends RuntimeException {
    public SessionReuseDetectedException() {
        super("Session refresh token has already been used");
    }
}
