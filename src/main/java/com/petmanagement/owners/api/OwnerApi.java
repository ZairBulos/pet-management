package com.petmanagement.owners.api;

import java.util.Optional;
import java.util.UUID;

public interface OwnerApi {
    boolean existsByEmail(String email);
    Optional<UUID> getOwnerIdByEmail(String email);
}
