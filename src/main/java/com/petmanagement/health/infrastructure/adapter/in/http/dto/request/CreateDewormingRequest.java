package com.petmanagement.health.infrastructure.adapter.in.http.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

public record CreateDewormingRequest(

        @NotNull(message = "The pet id cannot be null")
        UUID petId,

        @NotNull(message = "The deworming date cannot be null")
        @PastOrPresent(message = "The deworming date cannot be in the future")
        LocalDate dewormingDate,

        @NotBlank(message = "The drug name cannot be blank")
        @Size(max = 100, message = "The drug name cannot exceed 100 characters")
        String drugName,

        @NotBlank(message = "The drug dose cannot be blank")
        @Size(max = 100, message = "The drug dose cannot exceed 100 characters")
        String drugDose,

        @NotNull(message = "The next due date cannot be null")
        @Future(message = "The next due date must be in the future")
        LocalDate nextDueDate

) {

    public CreateDewormingRequest {
        drugName = drugName == null ? null : drugName.trim();
        drugDose = drugDose == null ? null : drugDose.trim();
    }

}
