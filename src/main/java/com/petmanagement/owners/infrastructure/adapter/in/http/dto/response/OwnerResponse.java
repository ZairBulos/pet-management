package com.petmanagement.owners.infrastructure.adapter.in.http.dto.response;

public record OwnerResponse(
        String name,
        String email,
        String phone
) {
}
