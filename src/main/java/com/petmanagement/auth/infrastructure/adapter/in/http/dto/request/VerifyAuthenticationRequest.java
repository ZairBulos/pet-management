package com.petmanagement.auth.infrastructure.adapter.in.http.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record VerifyAuthenticationRequest(

        @NotBlank(message = "The email cannot be blank")
        @Size(max = 254, message = "The email cannot exceed 254 characters")
        @Email(message = "The email must be a valid format")
        String email,

        @NotBlank(message = "The code cannot be blank")
        @Pattern(regexp = "^\\d{6}$", message = "The code must have 6 digits")
        String code

) {
}
