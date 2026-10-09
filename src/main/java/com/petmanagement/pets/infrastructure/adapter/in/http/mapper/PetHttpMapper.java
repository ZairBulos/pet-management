package com.petmanagement.pets.infrastructure.adapter.in.http.mapper;

import com.petmanagement.pets.application.port.in.CreatePetUseCase;
import com.petmanagement.pets.application.port.in.GetPetUseCase;
import com.petmanagement.pets.domain.model.aggregate.Pet;
import com.petmanagement.pets.domain.model.valueobject.*;
import com.petmanagement.pets.infrastructure.adapter.in.http.dto.request.CreatePetRequest;
import com.petmanagement.pets.infrastructure.adapter.in.http.dto.response.PetResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PetHttpMapper {

    public CreatePetUseCase.CreatePetCommand toCreatePetCommand(
            UUID ownerId,
            CreatePetRequest request
    ) {
        return new CreatePetUseCase.CreatePetCommand(
                OwnerId.of(ownerId),
                new PetName(request.name()),
                new Species(request.species()),
                new Breed(request.breed()),
                new Coat(request.coat()),
                request.sex(),
                request.birthDate()
        );
    }

    public GetPetUseCase.GetPetCommand toGetPetCommand(UUID petId) {
        return new GetPetUseCase.GetPetCommand(PetId.of(petId));
    }

    public PetResponse toPetResponse(Pet pet) {
        return new PetResponse(
                pet.getId().value(),
                pet.getName().value(),
                pet.getSpecies().value(),
                pet.getBreed().value(),
                pet.getCoat().value(),
                pet.getSex(),
                pet.getBirthDate()
        );
    }

}
