package com.petmanagement.owners.infrastructure.adapter.in.http.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateOwnerRequest(

        @NotBlank(message = "The name cannot be blank")
        @Size(min = 2, max = 150, message = "The name must be between 2 and 150 characters")
        String name,

        @NotBlank(message = "The email cannot be blank")
        @Size(max = 254, message = "The email cannot exceed 254 characters")
        @Email(message = "The email must be a valid format")
        String email,

        @NotBlank(message = "The phone number cannot be blank")
        @Pattern(regexp = "^\\+?\\d{7,15}$", message = "The phone number must have between 7 and 15 digits and may start with +")
        String phone

) {

    public CreateOwnerRequest {
        name = name == null ? null : name.trim();
        email = email == null ? null : email.trim();
        phone = phone == null ? null : phone.trim().replaceAll("[\\s()\\-.]", "");
    }

}
