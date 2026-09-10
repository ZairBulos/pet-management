package com.petmanagement.health.application.service;

import com.petmanagement.health.application.port.in.GetDewormingHistoryUseCase;
import com.petmanagement.health.domain.exception.PetNotFoundException;
import com.petmanagement.health.domain.model.aggregate.Deworming;
import com.petmanagement.health.domain.model.valueobject.DrugDose;
import com.petmanagement.health.domain.model.valueobject.DrugName;
import com.petmanagement.health.domain.model.valueobject.NextDueDate;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.health.support.InMemoryDewormingRepository;
import com.petmanagement.pets.api.PetApi;
import com.petmanagement.shared.domain.model.PageRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetDewormingHistoryServiceTest {

    private InMemoryDewormingRepository repository;
    private PetApi petApi;
    private GetDewormingHistoryService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryDewormingRepository();
        petApi = mock(PetApi.class);

        service = new GetDewormingHistoryService(repository, petApi);
    }

    @Test
    void shouldReturnDewormingHistoryForPet() {
        // Given
        var petId = PetId.of("d74822e6-870f-451e-8cbb-1cea5886fa68");
        var records = List.of(
                Deworming.create(
                        petId,
                        LocalDate.now(),
                        new DrugName("Drontal"),
                        new DrugDose("1 tablet (3mg)"),
                        new NextDueDate(LocalDate.now().plusMonths(3))
                ),
                Deworming.create(
                        petId,
                        LocalDate.now(),
                        new DrugName("Advocate"),
                        new DrugDose("0.4 ml solution"),
                        new NextDueDate(LocalDate.now().plusMonths(6))
                )
        );
        records.forEach(deworming -> repository.save(deworming));

        var query = new GetDewormingHistoryUseCase.GetDewormingHistoryQuery(
                petId, PageRequest.of(0, 10)
        );

        when(petApi.existsById(petId.value()))
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
        var petId = PetId.of("2325c8e9-0511-48d9-ac8a-fcee19c34e45");
        var query = new GetDewormingHistoryUseCase.GetDewormingHistoryQuery(
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
        var petId = PetId.of("73bd7afa-f0aa-4b14-bc22-1153a82f9434");
        var query = new GetDewormingHistoryUseCase.GetDewormingHistoryQuery(
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
