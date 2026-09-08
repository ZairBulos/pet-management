package com.petmanagement.health.application.service;

import com.petmanagement.health.application.port.in.GetVaccineHistoryUseCase;
import com.petmanagement.health.domain.exception.PetNotFoundException;
import com.petmanagement.health.domain.model.aggregate.Vaccine;
import com.petmanagement.health.domain.model.valueobject.NextDueDate;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.health.domain.model.valueobject.VaccineName;
import com.petmanagement.health.support.InMemoryVaccineRepository;
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

class GetVaccineHistoryServiceTest {

    private InMemoryVaccineRepository repository;
    private PetApi petApi;
    private GetVaccineHistoryService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryVaccineRepository();
        petApi = mock(PetApi.class);

        service = new GetVaccineHistoryService(repository, petApi);
    }

    @Test
    void shouldReturnVaccineHistoryForPet() {
        // Given
        var petId = PetId.of("035936a4-ec63-4796-be9a-61c089bfc145");
        var records = List.of(
                Vaccine.create(
                        petId,
                        LocalDate.now(),
                        new VaccineName("FVRCP"),
                        new NextDueDate(LocalDate.now().plusMonths(1))
                ),
                Vaccine.create(
                        petId,
                        LocalDate.now(),
                        new VaccineName("Rabies"),
                        new NextDueDate(LocalDate.now().plusYears(1))
                )
        );
        records.forEach(vaccine -> repository.save(vaccine));

        var query = new GetVaccineHistoryUseCase.GetVaccineHistoryQuery(
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
        var petId = PetId.of("34c705b1-5025-4d90-b2f6-a8e5ca190b85");
        var query = new GetVaccineHistoryUseCase.GetVaccineHistoryQuery(
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
        var petId = PetId.of("34359e7c-7f55-4789-b5bf-6c4cf1bcb83f");
        var query = new GetVaccineHistoryUseCase.GetVaccineHistoryQuery(
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
