package com.petmanagement.pets.application.port.in;

import com.petmanagement.pets.domain.model.aggregate.Pet;
import com.petmanagement.pets.domain.model.valueobject.PetId;

public interface GetPetUseCase {
    Pet execute(GetPetCommand command);

    record GetPetCommand(PetId petId) {}
}
