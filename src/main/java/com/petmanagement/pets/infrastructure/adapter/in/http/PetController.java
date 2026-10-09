package com.petmanagement.pets.infrastructure.adapter.in.http;

import com.petmanagement.pets.application.port.in.CreatePetUseCase;
import com.petmanagement.pets.application.port.in.GetPetUseCase;
import com.petmanagement.pets.infrastructure.adapter.in.http.dto.request.CreatePetRequest;
import com.petmanagement.pets.infrastructure.adapter.in.http.dto.response.PetResponse;
import com.petmanagement.pets.infrastructure.adapter.in.http.mapper.PetHttpMapper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(PetController.PETS)
class PetController {

    static final String PETS = "/api/pets";
    static final String ID = "/{id}";

    private static final Logger log = LoggerFactory.getLogger(PetController.class);

    private final PetHttpMapper mapper;
    private final CreatePetUseCase createPetUseCase;
    private final GetPetUseCase getPetUseCase;

    PetController(
            PetHttpMapper mapper,
            CreatePetUseCase createPetUseCase,
            GetPetUseCase getPetUseCase
    ) {
        this.mapper = mapper;
        this.createPetUseCase = createPetUseCase;
        this.getPetUseCase = getPetUseCase;
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

    @GetMapping(ID)
    ResponseEntity<PetResponse> get(@PathVariable UUID id) {
        log.info("Getting pet id={}", id);

        var pet = getPetUseCase.execute(mapper.toGetPetCommand(id));

        return ResponseEntity.ok(mapper.toPetResponse(pet));
    }

}
