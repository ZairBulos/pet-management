package com.petmanagement.health.application.service;

import com.petmanagement.health.application.port.in.CreateVaccineUseCase;
import com.petmanagement.health.domain.event.VaccineCreated;
import com.petmanagement.health.domain.exception.PetNotFoundException;
import com.petmanagement.health.domain.model.valueobject.NextDueDate;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.health.domain.model.valueobject.VaccineName;
import com.petmanagement.health.support.InMemoryVaccineRepository;
import com.petmanagement.pets.api.PetApi;
import com.petmanagement.shared.application.port.out.EventPublisherPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateVaccineServiceTest {

    private InMemoryVaccineRepository repository;
    private EventPublisherPort publisher;
    private PetApi petApi;
    private CreateVaccineService service;

    private static final PetId PET_ID = PetId.of("5385f7ff-9048-4942-864e-cc2fd7a5de39");
    private static final LocalDate VACCINATION_DATE = LocalDate.of(2026, 5, 1);
    private static final VaccineName VACCINE_NAME = new VaccineName("Rabies");
    private static final NextDueDate NEXT_DUE_DATE = new NextDueDate(LocalDate.of(2027, 5, 1));

    @BeforeEach
    void setUp() {
        repository = new InMemoryVaccineRepository();
        publisher = mock(EventPublisherPort.class);
        petApi = mock(PetApi.class);

        service = new CreateVaccineService(repository, publisher, petApi);
    }

    @Test
    void shouldCreateVaccine() {
        // Given
        var command = new CreateVaccineUseCase.CreateVaccineCommand(
                PET_ID,
                VACCINATION_DATE,
                VACCINE_NAME,
                NEXT_DUE_DATE
        );

        when(petApi.existsById(PET_ID.value())).thenReturn(true);

        // When
        var vaccineId = service.execute(command);

        // Then
        assertNotNull(vaccineId);
    }

    @Test
    void shouldPublishVaccineCreatedEvent() {
        // Given
        var command = new CreateVaccineUseCase.CreateVaccineCommand(
                PET_ID,
                VACCINATION_DATE,
                VACCINE_NAME,
                NEXT_DUE_DATE
        );

        when(petApi.existsById(PET_ID.value())).thenReturn(true);

        // When
        service.execute(command);

        // Then
        var eventCaptor = ArgumentCaptor.forClass(List.class);
        verify(publisher).publishAll(eventCaptor.capture());

        var events = eventCaptor.getValue();
        assertEquals(1, events.size());
        assertInstanceOf(VaccineCreated.class, events.getFirst());
    }

    @Test
    void shouldThrowWhenPetDoesNotExist() {
        // Given
        var command = new CreateVaccineUseCase.CreateVaccineCommand(
                PET_ID,
                VACCINATION_DATE,
                VACCINE_NAME,
                NEXT_DUE_DATE
        );

        when(petApi.existsById(PET_ID.value())).thenReturn(false);

        // When/Then
        assertThrows(
                PetNotFoundException.class,
                () -> service.execute(command)
        );
    }

    @Test
    void shouldNotPublishEventWhenPetDoesNotExist() {
        // Given
        var command = new CreateVaccineUseCase.CreateVaccineCommand(
                PET_ID,
                VACCINATION_DATE,
                VACCINE_NAME,
                NEXT_DUE_DATE
        );

        when(petApi.existsById(PET_ID.value())).thenReturn(false);

        // When/Then
        assertThrows(
                PetNotFoundException.class,
                () -> service.execute(command)
        );

        // Then
        verify(publisher, never()).publishAll(any());
    }

}
