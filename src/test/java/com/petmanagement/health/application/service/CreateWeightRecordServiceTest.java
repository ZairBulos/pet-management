package com.petmanagement.health.application.service;

import com.petmanagement.health.application.port.in.CreateWeightRecordUseCase;
import com.petmanagement.health.domain.exception.PetNotFoundException;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.health.domain.model.valueobject.Weight;
import com.petmanagement.health.support.InMemoryWeightRecordRepository;
import com.petmanagement.pets.api.PetApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

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

    @Test
    void shouldCreateWeightRecord() {
        // Given
        var petId = PetId.of("fe127567-fc1a-47f6-a929-98aa26f957d7");
        var command = new CreateWeightRecordUseCase.CreateWeightRecord(
                petId,
                LocalDate.now(),
                Weight.of(5.5)
        );

        when(petApi.existsById(petId.value())).thenReturn(true);

        // When
        var weightRecordId = service.execute(command);

        // Then
        assertNotNull(weightRecordId);
    }

    @Test
    void shouldThrowWhenPetDoesNotExist() {
        // Given
        var petId = PetId.of("83899454-a505-4c52-ad77-721a2ae3e3c6");
        var command = new CreateWeightRecordUseCase.CreateWeightRecord(
                petId,
                LocalDate.now(),
                Weight.of(3.4)
        );

        when(petApi.existsById(petId.value())).thenReturn(false);

        // When/Then
        assertThrows(
                PetNotFoundException.class,
                () -> service.execute(command)
        );
    }

}
