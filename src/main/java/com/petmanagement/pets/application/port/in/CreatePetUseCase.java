package com.petmanagement.pets.application.port.in;

import com.petmanagement.pets.domain.model.enums.Sex;
import com.petmanagement.pets.domain.model.valueobject.*;

import java.time.LocalDate;

public interface CreatePetUseCase {
    PetId execute(CreatePetCommand command);

    record CreatePetCommand(
            OwnerId ownerId,
            PetName name,
            Species species,
            Breed breed,
            Coat coat,
            Sex sex,
            LocalDate birthDate
    ) {}
}
