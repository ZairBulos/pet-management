package com.petmanagement.health.infrastructure.adapter.in.http;

import com.petmanagement.health.application.port.in.CreateVaccineUseCase;
import com.petmanagement.health.infrastructure.adapter.in.http.dto.request.CreateVaccineRequest;
import com.petmanagement.health.infrastructure.adapter.in.http.mapper.VaccineHttpMapper;
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
@RequestMapping(VaccineController.VACCINES)
class VaccineController {

    static final String VACCINES = "/api/health/vaccines";

    private static final Logger log = LoggerFactory.getLogger(VaccineController.class);

    private final VaccineHttpMapper mapper;
    private final CreateVaccineUseCase createVaccineUseCase;

    VaccineController(
            VaccineHttpMapper mapper,
            CreateVaccineUseCase createVaccineUseCase
    ) {
        this.mapper = mapper;
        this.createVaccineUseCase = createVaccineUseCase;
    }

    @PostMapping
    ResponseEntity<Void> create(@Valid @RequestBody CreateVaccineRequest request) {
        log.info("Creating vaccine request={}", request);

        var vaccineId = createVaccineUseCase.execute(mapper.toCreateVaccineCommand(request));

        log.info("Vaccine created id={}", vaccineId.value());

        var location = URI.create(VACCINES);

        return ResponseEntity.created(location).build();
    }

}
