package com.petmanagement.health.infrastructure.adapter.in.http.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public record VaccineResponse(
        UUID id,
        LocalDate vaccinationDate,
        String vaccineName,
        LocalDate nextDueDate
) {
}
