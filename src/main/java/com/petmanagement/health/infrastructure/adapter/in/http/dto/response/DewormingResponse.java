package com.petmanagement.health.infrastructure.adapter.in.http.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public record DewormingResponse(
        UUID id,
        LocalDate dewormingDate,
        String drugName,
        String drugDose,
        LocalDate nextDueDate
) {
}
