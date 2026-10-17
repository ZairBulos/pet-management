package com.petmanagement.owners.domain.exception;

public class OwnerAlreadyExistsException extends RuntimeException {
    public OwnerAlreadyExistsException() {
        super("Owner already exists");
    }
}
