package com.petmanagement.health.infrastructure.adapter.in.http.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

public record CreateVaccineRequest(

        @NotNull(message = "The pet id cannot be null")
        UUID petId,

        @NotNull(message = "The vaccination date cannot be null")
        @PastOrPresent(message = "The vaccination date cannot be in the future")
        LocalDate vaccinationDate,

        @NotBlank(message = "The vaccine name cannot be blank")
        @Size(max = 100, message = "The vaccine name cannot exceed 100 characters")
        String vaccineName,

        @NotNull(message = "The next due date cannot be null")
        @Future(message = "The next due date must be in the future")
        LocalDate nextDueDate

) {

    public CreateVaccineRequest {
        vaccineName = vaccineName == null ? null : vaccineName.trim();
    }

}
