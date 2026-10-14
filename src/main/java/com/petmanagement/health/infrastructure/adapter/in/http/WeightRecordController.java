package com.petmanagement.health.infrastructure.adapter.in.http;

import com.petmanagement.health.application.port.in.CreateWeightRecordUseCase;
import com.petmanagement.health.infrastructure.adapter.in.http.dto.request.CreateWeightRecordRequest;
import com.petmanagement.health.infrastructure.adapter.in.http.mapper.WeightRecordHttpMapper;
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
@RequestMapping(WeightRecordController.WEIGHT_RECORDS)
class WeightRecordController {

    static final String WEIGHT_RECORDS = "/api/health/weight-records";

    private static final Logger log = LoggerFactory.getLogger(WeightRecordController.class);

    private final WeightRecordHttpMapper mapper;
    private final CreateWeightRecordUseCase createWeightRecordUseCase;

    WeightRecordController(
            WeightRecordHttpMapper mapper,
            CreateWeightRecordUseCase createWeightRecordUseCase
    ) {
        this.mapper = mapper;
        this.createWeightRecordUseCase = createWeightRecordUseCase;
    }

    @PostMapping
    ResponseEntity<Void> create(@Valid @RequestBody CreateWeightRecordRequest request) {
        log.info("Creating weight record request={}", request);

        var weightRecordId = createWeightRecordUseCase.execute(mapper.toCreateWeightRecordCommand(request));

        log.info("Weight record created id={}", weightRecordId.value());

        var location = URI.create(WEIGHT_RECORDS);

        return ResponseEntity.created(location).build();
    }

}
