package com.petmanagement.health.infrastructure.adapter.in.http.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateWeightRecordRequest(

        @NotNull(message = "The pet id cannot be null")
        UUID petId,

        @NotNull(message = "The weight date cannot be null")
        @PastOrPresent(message = "The weight date cannot be in the future")
        LocalDate weightDate,

        @NotNull(message = "The weight cannot be null")
        @Positive(message = "The weight must be greater than zero")
        BigDecimal weight

) {
}
