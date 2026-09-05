package com.petmanagement.health.domain.exception;

public class WeightRecordNotFoundException extends RuntimeException {
    public WeightRecordNotFoundException() {
        super("Weight record not found");
    }
}
