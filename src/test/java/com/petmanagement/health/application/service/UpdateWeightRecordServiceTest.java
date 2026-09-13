package com.petmanagement.health.application.service;

import com.petmanagement.health.domain.exception.WeightRecordNotFoundException;
import com.petmanagement.health.support.InMemoryWeightRecordRepository;
import com.petmanagement.health.support.TestWeightRecordMother;
import com.petmanagement.health.support.WeightRecordTestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdateWeightRecordServiceTest {

    private InMemoryWeightRecordRepository repository;
    private UpdateWeightRecordService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryWeightRecordRepository();
        service = new UpdateWeightRecordService(repository);
    }

    @Nested
    class WhenUpdatingWeightRecord {

        @Test
        void shouldUpdateWeightRecord() {
            // Given
            var originalRecord = WeightRecordTestBuilder
                    .aWeightRecord()
                    .build();
            repository.save(originalRecord);

            var command = WeightRecordTestBuilder.UpdateWeightRecordCommandBuilder
                    .aUpdateWeightRecordCommand()
                    .withId(originalRecord.getId())
                    .build();

            // When
            var result = service.execute(command);

            // Then
            assertNotNull(result);
            assertEquals(command.weightDate(), result.getWeightDate());
            assertEquals(command.weight(), result.getWeight());
        }

    }

    @Nested
    class WhenWeightRecordDoesNotExist {

        @Test
        void shouldThrowWhenWeightRecordDoesNotExist() {
            // Given
            var command = WeightRecordTestBuilder.UpdateWeightRecordCommandBuilder
                    .aUpdateWeightRecordCommand()
                    .withId(TestWeightRecordMother.NON_EXISTENT_RECORD_ID)
                    .build();

            // When
            assertThrows(
                    WeightRecordNotFoundException.class,
                    () -> service.execute(command)
            );
        }

    }

}
