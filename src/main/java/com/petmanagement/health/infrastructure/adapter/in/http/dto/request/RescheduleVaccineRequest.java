package com.petmanagement.health.infrastructure.adapter.in.http.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record RescheduleVaccineRequest(

        @NotNull(message = "The vaccine id cannot be null")
        UUID vaccineId,

        @NotNull(message = "The next due date cannot be null")
        @Future(message = "The next due date must be in the future")
        LocalDate nextDueDate

) {
}
