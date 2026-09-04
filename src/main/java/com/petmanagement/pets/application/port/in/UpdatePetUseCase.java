package com.petmanagement.pets.application.port.in;

import com.petmanagement.pets.domain.model.aggregate.Pet;
import com.petmanagement.pets.domain.model.valueobject.PetId;
import com.petmanagement.pets.domain.model.valueobject.PetName;

public interface UpdatePetUseCase {
    Pet execute(UpdatePetCommand command);

    record UpdatePetCommand(PetId petId, PetName name) {}
}
