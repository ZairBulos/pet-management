package com.petmanagement.pets.application.port.in;

import com.petmanagement.pets.domain.model.aggregate.Pet;
import com.petmanagement.pets.domain.model.enums.Sex;
import com.petmanagement.pets.domain.model.valueobject.*;

import java.time.LocalDate;

public interface UpdatePetUseCase {
    Pet execute(UpdatePetCommand command);

    record UpdatePetCommand(
            PetId petId,
            PetName name,
            Species species,
            Breed breed,
            Coat coat,
            Sex sex,
            LocalDate birthDate
    ) {
    }
}
