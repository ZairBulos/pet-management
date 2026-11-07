package com.petmanagement.owners.api;

import java.util.Optional;
import java.util.UUID;

public interface OwnerApi {
    boolean existsByEmail(String email);

    Optional<UUID> getOwnerIdByEmail(String email);

    Optional<OwnerContact> getContactById(UUID ownerId);

    record OwnerContact(String name, String email, String phone) {}
}
