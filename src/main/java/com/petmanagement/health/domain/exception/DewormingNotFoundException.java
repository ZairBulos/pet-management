package com.petmanagement.health.domain.exception;

public class DewormingNotFoundException extends RuntimeException {
    public DewormingNotFoundException() {
        super("Deworming not found");
    }
}
