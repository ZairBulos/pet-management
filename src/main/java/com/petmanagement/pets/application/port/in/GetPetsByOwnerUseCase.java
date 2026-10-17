package com.petmanagement.pets.application.port.in;

import com.petmanagement.pets.domain.model.aggregate.Pet;
import com.petmanagement.pets.domain.model.valueobject.OwnerId;

import java.util.List;

public interface GetPetsByOwnerUseCase {
    List<Pet> execute(GetPetsByOwnerQuery query);

    record GetPetsByOwnerQuery(OwnerId ownerId) {}
}
