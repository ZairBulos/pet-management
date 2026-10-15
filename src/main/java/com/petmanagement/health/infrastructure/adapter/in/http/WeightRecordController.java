package com.petmanagement.health.infrastructure.adapter.in.http;

import com.petmanagement.health.application.port.in.CreateWeightRecordUseCase;
import com.petmanagement.health.application.port.in.GetWeightHistoryUseCase;
import com.petmanagement.health.infrastructure.adapter.in.http.dto.request.CreateWeightRecordRequest;
import com.petmanagement.health.infrastructure.adapter.in.http.dto.response.WeightRecordResponse;
import com.petmanagement.health.infrastructure.adapter.in.http.mapper.WeightRecordHttpMapper;
import com.petmanagement.shared.domain.model.PageResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(WeightRecordController.WEIGHT_RECORDS)
class WeightRecordController {

    static final String WEIGHT_RECORDS = "/api/health/weight-records";

    private static final Logger log = LoggerFactory.getLogger(WeightRecordController.class);

    private final WeightRecordHttpMapper mapper;
    private final CreateWeightRecordUseCase createWeightRecordUseCase;
    private final GetWeightHistoryUseCase getWeightHistoryUseCase;

    WeightRecordController(
            WeightRecordHttpMapper mapper,
            CreateWeightRecordUseCase createWeightRecordUseCase,
            GetWeightHistoryUseCase getWeightHistoryUseCase
    ) {
        this.mapper = mapper;
        this.createWeightRecordUseCase = createWeightRecordUseCase;
        this.getWeightHistoryUseCase = getWeightHistoryUseCase;
    }

    @PostMapping
    ResponseEntity<Void> create(@Valid @RequestBody CreateWeightRecordRequest request) {
        log.info("Creating weight record request={}", request);

        var weightRecordId = createWeightRecordUseCase.execute(mapper.toCreateWeightRecordCommand(request));

        log.info("Weight record created id={}", weightRecordId.value());

        var location = URI.create(WEIGHT_RECORDS);

        return ResponseEntity.created(location).build();
    }

    @GetMapping
    ResponseEntity<PageResponse<WeightRecordResponse>> history(
            @RequestParam UUID petId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("Getting weight history petId={} page={} size={}", petId, page, size);

        var history = getWeightHistoryUseCase.execute(mapper.toGetWeightHistoryQuery(petId, page, size));

        log.info("Weight history petId={} totalElements={}", petId, history.totalElements());

        return ResponseEntity.ok().body(mapper.toWeightHistoryResponse(history));
    }

}
