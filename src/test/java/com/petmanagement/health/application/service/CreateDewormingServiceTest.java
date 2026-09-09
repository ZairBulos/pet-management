package com.petmanagement.health.application.service;

import com.petmanagement.health.application.port.in.CreateDewormingUseCase;
import com.petmanagement.health.domain.event.DewormingCreated;
import com.petmanagement.health.domain.exception.PetNotFoundException;
import com.petmanagement.health.domain.model.valueobject.DrugDose;
import com.petmanagement.health.domain.model.valueobject.DrugName;
import com.petmanagement.health.domain.model.valueobject.NextDueDate;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.health.support.InMemoryDewormingRepository;
import com.petmanagement.pets.api.PetApi;
import com.petmanagement.shared.application.port.out.EventPublisherPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateDewormingServiceTest {

    private InMemoryDewormingRepository repository;
    private EventPublisherPort publisher;
    private PetApi petApi;
    private CreateDewormingService service;

    private static final PetId PET_ID = PetId.of("a5ff5e24-adbc-47e9-8403-4be7ba2ef54d");
    private static final LocalDate DEWORMING_DATE = LocalDate.of(2026, 3, 1);
    private static final DrugName DRUG_NAME = new DrugName("Milbemax");
    private static final DrugDose DRUG_DOSE = new DrugDose("1 tablet (2mg)");
    private static final NextDueDate NEXT_DUE_DATE = new NextDueDate(LocalDate.of(2027, 6, 1));

    @BeforeEach
    void setUp() {
        repository = new InMemoryDewormingRepository();
        publisher = mock(EventPublisherPort.class);
        petApi = mock(PetApi.class);

        service = new CreateDewormingService(repository, publisher, petApi);
    }

    @Test
    void shouldCreateDeworming() {
        // Given
        var command = new CreateDewormingUseCase.CreateDewormingCommand(
                PET_ID,
                DEWORMING_DATE,
                DRUG_NAME,
                DRUG_DOSE,
                NEXT_DUE_DATE
        );

        when(petApi.existsById(PET_ID.value()))
                .thenReturn(true);

        // When
        var dewormingId = service.execute(command);

        // Then
        assertNotNull(dewormingId);
    }

    @Test
    void shouldPublishDewormingCreatedEvent() {
        // Given
        var command = new CreateDewormingUseCase.CreateDewormingCommand(
                PET_ID,
                DEWORMING_DATE,
                DRUG_NAME,
                DRUG_DOSE,
                NEXT_DUE_DATE
        );

        when(petApi.existsById(PET_ID.value()))
                .thenReturn(true);

        // When
        service.execute(command);

        // Then
        var eventCaptor = ArgumentCaptor.forClass(List.class);
        verify(publisher).publishAll(eventCaptor.capture());

        var events = eventCaptor.getValue();
        assertEquals(1, events.size());
        assertInstanceOf(DewormingCreated.class, events.getFirst());
    }

    @Test
    void shouldThrowWhenPetDoesNotExist() {
        // Given
        var command = new CreateDewormingUseCase.CreateDewormingCommand(
                PET_ID,
                DEWORMING_DATE,
                DRUG_NAME,
                DRUG_DOSE,
                NEXT_DUE_DATE
        );

        when(petApi.existsById(PET_ID.value()))
                .thenReturn(false);

        // When/Then
        assertThrows(
                PetNotFoundException.class,
                () -> service.execute(command)
        );
    }

    @Test
    void shouldNotPublishEventWhenPetDoesNotExist() {
        // Given
        var command = new CreateDewormingUseCase.CreateDewormingCommand(
                PET_ID,
                DEWORMING_DATE,
                DRUG_NAME,
                DRUG_DOSE,
                NEXT_DUE_DATE
        );

        when(petApi.existsById(PET_ID.value()))
                .thenReturn(false);

        // When/Then
        assertThrows(
                PetNotFoundException.class,
                () -> service.execute(command)
        );

        verify(publisher, never()).publishAll(any());
    }

}
