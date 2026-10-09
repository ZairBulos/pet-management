package com.petmanagement.pets.infrastructure.adapter.in.http.mapper;

import com.petmanagement.pets.application.port.in.CreatePetUseCase;
import com.petmanagement.pets.domain.model.valueobject.*;
import com.petmanagement.pets.infrastructure.adapter.in.http.dto.request.CreatePetRequest;
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

}
