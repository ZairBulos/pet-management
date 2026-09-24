package com.petmanagement.auth.domain.exception;

public class OwnerNotFoundException extends RuntimeException {
    public OwnerNotFoundException() {
        super("Owner not found");
    }
}
