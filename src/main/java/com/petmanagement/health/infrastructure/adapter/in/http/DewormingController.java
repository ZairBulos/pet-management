package com.petmanagement.health.infrastructure.adapter.in.http;

import com.petmanagement.health.application.port.in.CreateDewormingUseCase;
import com.petmanagement.health.infrastructure.adapter.in.http.dto.request.CreateDewormingRequest;
import com.petmanagement.health.infrastructure.adapter.in.http.mapper.DewormingHttpMapper;
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
@RequestMapping(DewormingController.DEWORMINGS)
class DewormingController {

    static final String DEWORMINGS = "/api/health/dewormings";

    private static final Logger log = LoggerFactory.getLogger(DewormingController.class);

    private final DewormingHttpMapper mapper;
    private final CreateDewormingUseCase createDewormingUseCase;

    DewormingController(
            DewormingHttpMapper mapper,
            CreateDewormingUseCase createDewormingUseCase
    ) {
        this.mapper = mapper;
        this.createDewormingUseCase = createDewormingUseCase;
    }

    @PostMapping
    ResponseEntity<Void> create(@Valid @RequestBody CreateDewormingRequest request) {
        log.info("Creating deworming request={}", request);

        var dewormingId = createDewormingUseCase.execute(mapper.toCreateDewormingCommand(request));

        log.info("Deworming created id={}", dewormingId.value());

        var location = URI.create(DEWORMINGS);

        return ResponseEntity.created(location).build();
    }

}
