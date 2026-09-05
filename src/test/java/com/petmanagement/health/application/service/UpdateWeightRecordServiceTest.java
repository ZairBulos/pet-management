package com.petmanagement.health.application.service;

import com.petmanagement.health.application.port.in.UpdateWeightRecordUseCase;
import com.petmanagement.health.domain.exception.WeightRecordNotFoundException;
import com.petmanagement.health.domain.model.aggregate.WeightRecord;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.health.domain.model.valueobject.Weight;
import com.petmanagement.health.domain.model.valueobject.WeightRecordId;
import com.petmanagement.health.support.InMemoryWeightRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UpdateWeightRecordServiceTest {

    private InMemoryWeightRecordRepository repository;
    private UpdateWeightRecordService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryWeightRecordRepository();
        service = new UpdateWeightRecordService(repository);
    }

    @Test
    void shouldUpdateWeightRecord() {
        // Given
        var weightRecord = WeightRecord.create(
                PetId.of("2585c27a-2ee3-4f70-b979-5c75f399faf1"),
                LocalDate.of(2026, 4, 5),
                Weight.of(5.5)
        );
        repository.save(weightRecord);

        var command = new UpdateWeightRecordUseCase.UpdateWeightRecordCommand(
                weightRecord.getId(),
                LocalDate.of(2026, 4, 10),
                Weight.of(5.8)
        );

        // When
        var result = service.execute(command);

        // Then
        assertNotNull(result);
        assertEquals(command.weightDate(), result.getWeightDate());
        assertEquals(weightRecord.getWeight(), result.getWeight());
    }

    @Test
    void shouldThrowWhenWeightRecordNotFound() {
        // Given
        var command = new UpdateWeightRecordUseCase.UpdateWeightRecordCommand(
                WeightRecordId.of("0b48139f-917e-43dc-8f8a-8755e9206d59"),
                LocalDate.now(),
                Weight.of(5.0)
        );

        // When
        assertThrows(
                WeightRecordNotFoundException.class,
                () -> service.execute(command)
        );
    }

}
