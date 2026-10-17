package com.petmanagement.pets.infrastructure.adapter.in.http.dto.request;

import com.petmanagement.pets.domain.model.enums.Sex;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreatePetRequest(

        @NotBlank(message = "The name cannot be blank")
        @Size(min = 2, max = 100, message = "The name must be between 2 and 100 characters")
        String name,

        @NotBlank(message = "The species cannot be blank")
        @Size(max = 60, message = "The species cannot exceed 60 characters")
        String species,

        @Size(max = 100, message = "The breed cannot exceed 100 characters")
        String breed,

        @NotBlank(message = "The coat cannot be blank")
        @Size(max = 100, message = "The coat cannot exceed 100 characters")
        String coat,

        @NotNull(message = "The sex cannot be null")
        Sex sex,

        @NotNull(message = "The birth date cannot be null")
        @PastOrPresent(message = "The birth date cannot be in the future")
        LocalDate birthDate

) {

    public CreatePetRequest {
        name = name == null ? null : name.trim();
        species = species == null ? null : species.trim();
        breed = breed == null || breed.isBlank() ? null : breed.trim();
        coat = coat == null ? null : coat.trim();
    }

}
