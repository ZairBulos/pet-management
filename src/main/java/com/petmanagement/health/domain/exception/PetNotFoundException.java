package com.petmanagement.health.domain.exception;

public class PetNotFoundException extends RuntimeException {
    public PetNotFoundException() {
        super("Pet not found");
    }
}
