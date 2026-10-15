package com.petmanagement.health.infrastructure.adapter.in.http.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record WeightRecordResponse(
        UUID id,
        LocalDate weightDate,
        BigDecimal weight
) {
}
