package com.petmanagement.health.infrastructure.adapter.in.http;

import com.petmanagement.health.application.port.in.CreateVaccineUseCase;
import com.petmanagement.health.application.port.in.GetVaccineHistoryUseCase;
import com.petmanagement.health.application.port.in.RescheduleVaccineUseCase;
import com.petmanagement.health.infrastructure.adapter.in.http.dto.request.CreateVaccineRequest;
import com.petmanagement.health.infrastructure.adapter.in.http.dto.request.RescheduleVaccineRequest;
import com.petmanagement.health.infrastructure.adapter.in.http.dto.response.VaccineResponse;
import com.petmanagement.health.infrastructure.adapter.in.http.mapper.VaccineHttpMapper;
import com.petmanagement.shared.domain.model.PageResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(VaccineController.VACCINES)
class VaccineController {

    static final String VACCINES = "/api/health/vaccines";

    private static final Logger log = LoggerFactory.getLogger(VaccineController.class);

    private final VaccineHttpMapper mapper;
    private final CreateVaccineUseCase createVaccineUseCase;
    private final GetVaccineHistoryUseCase getVaccineHistoryUseCase;
    private final RescheduleVaccineUseCase rescheduleVaccineUseCase;

    VaccineController(
            VaccineHttpMapper mapper,
            CreateVaccineUseCase createVaccineUseCase,
            GetVaccineHistoryUseCase getVaccineHistoryUseCase,
            RescheduleVaccineUseCase rescheduleVaccineUseCase
    ) {
        this.mapper = mapper;
        this.createVaccineUseCase = createVaccineUseCase;
        this.getVaccineHistoryUseCase = getVaccineHistoryUseCase;
        this.rescheduleVaccineUseCase = rescheduleVaccineUseCase;
    }

    @PostMapping
    ResponseEntity<Void> create(@Valid @RequestBody CreateVaccineRequest request) {
        log.info("Creating vaccine request={}", request);

        var vaccineId = createVaccineUseCase.execute(mapper.toCreateVaccineCommand(request));

        log.info("Vaccine created id={}", vaccineId.value());

        var location = URI.create(VACCINES);

        return ResponseEntity.created(location).build();
    }

    @GetMapping
    ResponseEntity<PageResponse<VaccineResponse>> history(
            @RequestParam UUID petId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("Getting vaccine history petId={} page={} size={}", petId, page, size);

        var history = getVaccineHistoryUseCase.execute(mapper.toGetVaccineHistoryQuery(petId, page, size));

        log.info("Vaccine history petId={} totalElements={}", petId, history.totalElements());

        return ResponseEntity.ok().body(mapper.toVaccineHistoryResponse(history));
    }

    @PatchMapping
    ResponseEntity<VaccineResponse> reschedule(@Valid @RequestBody RescheduleVaccineRequest request) {
        log.info("Rescheduling vaccine request={}", request);

        var vaccine = rescheduleVaccineUseCase.execute(mapper.toRescheduleVaccineCommand(request));

        log.info("Vaccine rescheduled id={}", vaccine.id().value());

        return ResponseEntity.ok().body(mapper.toVaccineResponse(vaccine));
    }

}
