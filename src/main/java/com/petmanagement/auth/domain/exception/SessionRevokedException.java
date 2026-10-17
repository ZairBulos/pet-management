package com.petmanagement.auth.domain.exception;

public class SessionRevokedException extends RuntimeException {
    public SessionRevokedException() {
        super("Session has been revoked");
    }
}
