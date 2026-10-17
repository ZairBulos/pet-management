package com.petmanagement.health.domain.exception;

public class VaccineNotFoundException extends RuntimeException {
    public VaccineNotFoundException() {
        super("Vaccine not found");
    }
}
