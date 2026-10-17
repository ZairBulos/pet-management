package com.petmanagement.pets.infrastructure.adapter.in.http.dto.response;

import com.petmanagement.pets.domain.model.enums.Sex;

import java.time.LocalDate;
import java.util.UUID;

public record PetResponse(
        UUID id,
        String name,
        String species,
        String breed,
        String coat,
        Sex sex,
        LocalDate birthDate
) {
}
