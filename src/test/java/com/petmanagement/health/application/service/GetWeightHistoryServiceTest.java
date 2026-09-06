package com.petmanagement.health.application.service;

import com.petmanagement.health.application.port.in.GetWeightHistoryUseCase;
import com.petmanagement.health.domain.exception.PetNotFoundException;
import com.petmanagement.health.domain.model.aggregate.WeightRecord;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.health.domain.model.valueobject.Weight;
import com.petmanagement.health.support.InMemoryWeightRecordRepository;
import com.petmanagement.pets.api.PetApi;
import com.petmanagement.shared.domain.model.PageRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetWeightHistoryServiceTest {

    private InMemoryWeightRecordRepository repository;
    private PetApi petApi;
    private GetWeightHistoryService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryWeightRecordRepository();
        petApi = mock(PetApi.class);

        service = new GetWeightHistoryService(repository, petApi);
    }

    @Test
    void shouldReturnWeightHistoryForPet() {
        // Given
        var petId = PetId.of("d6950537-31f1-4886-ac21-457bd1d0f197");
        var records = List.of(
                WeightRecord.create(petId, LocalDate.of(2026, 1, 10), Weight.of(5.0)),
                WeightRecord.create(petId, LocalDate.of(2026, 3, 10), Weight.of(5.4))
        );
        records.forEach(weightRecord -> repository.save(weightRecord));

        var query = new GetWeightHistoryUseCase.GetWeightHistoryQuery(
                petId, PageRequest.of(0, 10)
        );

        when(petApi.existsById(query.petId().value()))
                .thenReturn(true);

        // When
        var response = service.execute(query);

        // Then
        assertEquals(2, response.content().size());
        assertEquals(2, response.totalElements());
        assertEquals(1, response.totalPages());
        assertEquals(0, response.page());
        assertEquals(10, response.size());
    }

    @Test
    void shouldReturnEmptyPageWhenPetHasNoRecords() {
        // Given
        var petId = PetId.of("0ecfa097-df4e-4d72-8463-50706c30a35b");
        var query = new GetWeightHistoryUseCase.GetWeightHistoryQuery(
                petId, PageRequest.of(0, 10)
        );

        when(petApi.existsById(petId.value()))
                .thenReturn(true);

        // When
        var response = service.execute(query);

        // Then
        assertTrue(response.isEmpty());
        assertEquals(0, response.totalElements());
        assertEquals(0, response.totalPages());
        assertEquals(0, response.numberOfElements());
    }

    @Test
    void shouldThrowWhenPetDoesNotExist() {
        // Given
        var petId = PetId.of("8dd3534d-f7fc-420f-8c50-2d2a5b7607d9");
        var query = new GetWeightHistoryUseCase.GetWeightHistoryQuery(
                petId, PageRequest.of(0, 10)
        );

        when(petApi.existsById(petId.value()))
                .thenReturn(false);

        // When/Then
        assertThrows(
                PetNotFoundException.class,
                () -> service.execute(query)
        );
    }

}
