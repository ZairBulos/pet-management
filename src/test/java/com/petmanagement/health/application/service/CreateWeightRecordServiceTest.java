package com.petmanagement.health.application.service;

import com.petmanagement.health.domain.exception.PetNotFoundException;
import com.petmanagement.health.support.InMemoryWeightRecordRepository;
import com.petmanagement.health.support.TestPetIdMother;
import com.petmanagement.health.support.WeightRecordTestBuilder;
import com.petmanagement.pets.api.PetApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreateWeightRecordServiceTest {

    private InMemoryWeightRecordRepository repository;
    private PetApi petApi;
    private CreateWeightRecordService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryWeightRecordRepository();
        petApi = mock(PetApi.class);

        service = new CreateWeightRecordService(repository, petApi);
    }

    @Nested
    class WhenCreatingWeightRecord {

        @Test
        void shouldCreateWeightRecord() {
            // Given
            var command = WeightRecordTestBuilder.CreateWeightRecordCommandBuilder
                    .aCreateWeightRecordCommand()
                    .build();

            when(petApi.existsById(command.petId().value()))
                    .thenReturn(true);

            // When
            var result = service.execute(command);

            // Then
            assertNotNull(result);
        }

    }


    @Nested
    class WhenPetDoesNotExist {

        @Test
        void shouldThrowWhenPetDoesNotExist() {
            // Given
            var command = WeightRecordTestBuilder.CreateWeightRecordCommandBuilder
                    .aCreateWeightRecordCommand()
                    .withPetId(TestPetIdMother.NON_EXISTENT_PET_ID)
                    .build();

            when(petApi.existsById(command.petId().value()))
                    .thenReturn(false);

            // When/Then
            assertThrows(
                    PetNotFoundException.class,
                    () -> service.execute(command)
            );
        }

    }

}
