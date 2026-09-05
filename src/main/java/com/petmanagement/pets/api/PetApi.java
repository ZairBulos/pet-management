package com.petmanagement.pets.api;

import java.util.UUID;

public interface PetApi {
    boolean existsById(UUID petId);
}
