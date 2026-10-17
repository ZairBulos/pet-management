package com.petmanagement.health.infrastructure.adapter.in.http;

import com.petmanagement.health.application.port.in.CreateDewormingUseCase;
import com.petmanagement.health.application.port.in.GetDewormingHistoryUseCase;
import com.petmanagement.health.application.port.in.RescheduleDewormingUseCase;
import com.petmanagement.health.infrastructure.adapter.in.http.dto.request.CreateDewormingRequest;
import com.petmanagement.health.infrastructure.adapter.in.http.dto.request.RescheduleDewormingRequest;
import com.petmanagement.health.infrastructure.adapter.in.http.dto.response.DewormingResponse;
import com.petmanagement.health.infrastructure.adapter.in.http.mapper.DewormingHttpMapper;
import com.petmanagement.shared.domain.model.PageResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(DewormingController.DEWORMINGS)
class DewormingController {

    static final String DEWORMINGS = "/api/health/dewormings";

    private static final Logger log = LoggerFactory.getLogger(DewormingController.class);

    private final DewormingHttpMapper mapper;
    private final CreateDewormingUseCase createDewormingUseCase;
    private final GetDewormingHistoryUseCase getDewormingHistoryUseCase;
    private final RescheduleDewormingUseCase rescheduleDewormingUseCase;

    DewormingController(
            DewormingHttpMapper mapper,
            CreateDewormingUseCase createDewormingUseCase,
            GetDewormingHistoryUseCase getDewormingHistoryUseCase,
            RescheduleDewormingUseCase rescheduleDewormingUseCase
    ) {
        this.mapper = mapper;
        this.createDewormingUseCase = createDewormingUseCase;
        this.getDewormingHistoryUseCase = getDewormingHistoryUseCase;
        this.rescheduleDewormingUseCase = rescheduleDewormingUseCase;
    }

    @PostMapping
    ResponseEntity<Void> create(@Valid @RequestBody CreateDewormingRequest request) {
        log.info("Creating deworming request={}", request);

        var dewormingId = createDewormingUseCase.execute(mapper.toCreateDewormingCommand(request));

        log.info("Deworming created id={}", dewormingId.value());

        var location = URI.create(DEWORMINGS);

        return ResponseEntity.created(location).build();
    }

    @GetMapping
    ResponseEntity<PageResponse<DewormingResponse>> history(
            @RequestParam UUID petId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("Getting deworming history petId={} page={} size={}", petId, page, size);

        var history = getDewormingHistoryUseCase.execute(mapper.toGetDewormingHistoryQuery(petId, page, size));

        log.info("Deworming history petId={} totalElements={}", petId, history.totalElements());

        return ResponseEntity.ok().body(mapper.toDewormingHistoryResponse(history));
    }

    @PatchMapping
    ResponseEntity<DewormingResponse> reschedule(@Valid @RequestBody RescheduleDewormingRequest request) {
        log.info("Rescheduling deworming request={}", request);

        var deworming = rescheduleDewormingUseCase.execute(mapper.toRescheduleDewormingCommand(request));

        log.info("Deworming rescheduled id={}", deworming.id().value());

        return ResponseEntity.ok().body(mapper.toDewormingResponse(deworming));
    }

}
