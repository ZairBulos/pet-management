package com.petmanagement.owners.infrastructure.adapter.in.http;

import com.petmanagement.owners.application.port.in.CreateOwnerUseCase;
import com.petmanagement.owners.application.port.in.GetOwnerUseCase;
import com.petmanagement.owners.infrastructure.adapter.in.http.dto.request.CreateOwnerRequest;
import com.petmanagement.owners.infrastructure.adapter.in.http.dto.response.OwnerResponse;
import com.petmanagement.owners.infrastructure.adapter.in.http.mapper.OwnerHttpMapper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(OwnerController.OWNERS)
class OwnerController {

    static final String OWNERS = "/api/owners";
    static final String ME = "/me";

    private static final Logger log = LoggerFactory.getLogger(OwnerController.class);

    private final OwnerHttpMapper mapper;
    private final GetOwnerUseCase getOwnerUseCase;
    private final CreateOwnerUseCase createOwnerUseCase;

    OwnerController(
            OwnerHttpMapper mapper,
            GetOwnerUseCase getOwnerUseCase,
            CreateOwnerUseCase createOwnerUseCase
    ) {
        this.mapper = mapper;
        this.getOwnerUseCase = getOwnerUseCase;
        this.createOwnerUseCase = createOwnerUseCase;
    }

    @PostMapping
    ResponseEntity<Void> create(@Valid @RequestBody CreateOwnerRequest request) {
        log.info("Creating owner={}", request);

        var ownerId = createOwnerUseCase.execute(mapper.toCreateOwnerCommand(request));

        log.info("Owner created id={}", ownerId.value());

        var location = URI.create(OWNERS + ME);

        return ResponseEntity.created(location).build();
    }

    @GetMapping(ME)
    ResponseEntity<OwnerResponse> me(@AuthenticationPrincipal UUID ownerId) {
        log.info("Getting owner id={}", ownerId);

        var owner = getOwnerUseCase.execute(mapper.toGetOwnerUseCase(ownerId));

        log.info("Owner={}", owner);

        var response = mapper.toOwnerResponse(owner);

        return ResponseEntity.ok().body(response);
    }

}
