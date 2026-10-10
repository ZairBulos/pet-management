package com.petmanagement.pets.infrastructure.adapter.in.http.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePetRequest(

        @NotBlank(message = "The name cannot be blank")
        @Size(min = 2, max = 100, message = "The name must be between 2 and 100 characters")
        String name

) {

    public UpdatePetRequest {
        name = name == null ? null : name.trim();
    }

}
