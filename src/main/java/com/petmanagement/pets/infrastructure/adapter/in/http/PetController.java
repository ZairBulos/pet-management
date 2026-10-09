package com.petmanagement.pets.infrastructure.adapter.in.http;

import com.petmanagement.pets.application.port.in.CreatePetUseCase;
import com.petmanagement.pets.infrastructure.adapter.in.http.dto.request.CreatePetRequest;
import com.petmanagement.pets.infrastructure.adapter.in.http.mapper.PetHttpMapper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(PetController.PETS)
class PetController {

    static final String PETS = "/api/pets";

    private static final Logger log = LoggerFactory.getLogger(PetController.class);

    private final PetHttpMapper mapper;
    private final CreatePetUseCase createPetUseCase;

    PetController(
            PetHttpMapper mapper,
            CreatePetUseCase createPetUseCase
    ) {
        this.mapper = mapper;
        this.createPetUseCase = createPetUseCase;
    }

    @PostMapping
    ResponseEntity<Void> create(
            @Valid @RequestBody CreatePetRequest request,
            @AuthenticationPrincipal UUID ownerId
    ) {
        log.info("Creating pet={} ownerId={}", request, ownerId);

        var petId = createPetUseCase.execute(mapper.toCreatePetCommand(ownerId, request));

        log.info("Pet created id={}", petId.value());

        var location = URI.create(PETS + "/" + petId.value());

        return ResponseEntity.created(location).build();
    }

}
