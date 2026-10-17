package com.petmanagement.pets.domain.exception;

public class PetNotFoundException extends RuntimeException {
    public PetNotFoundException() {
        super("Pet not found");
    }
}
