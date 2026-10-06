package com.petmanagement.owners.infrastructure.adapter.in.http;

import com.petmanagement.owners.application.port.in.CreateOwnerUseCase;
import com.petmanagement.owners.infrastructure.adapter.in.http.dto.request.CreateOwnerRequest;
import com.petmanagement.owners.infrastructure.adapter.in.http.mapper.OwnerHttpMapper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping(OwnerController.OWNERS)
class OwnerController {

    static final String OWNERS = "/api/owners";
    static final String ME = "/me";

    private static final Logger log = LoggerFactory.getLogger(OwnerController.class);

    private final OwnerHttpMapper mapper;
    private final CreateOwnerUseCase createOwnerUseCase;

    OwnerController(
            OwnerHttpMapper mapper,
            CreateOwnerUseCase createOwnerUseCase
    ) {
        this.mapper = mapper;
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

}
