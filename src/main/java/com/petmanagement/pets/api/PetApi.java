package com.petmanagement.pets.api;

import java.util.Optional;
import java.util.UUID;

public interface PetApi {
    boolean existsById(UUID petId);

    Optional<PetInfo> getInfoById(UUID petId);

    record PetInfo(String name, UUID ownerId) {}
}
