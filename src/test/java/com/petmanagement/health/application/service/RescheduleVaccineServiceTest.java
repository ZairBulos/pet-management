package com.petmanagement.health.application.service;

import com.petmanagement.health.application.port.in.RescheduleVaccineUseCase;
import com.petmanagement.health.domain.event.VaccineRescheduled;
import com.petmanagement.health.domain.exception.VaccineNotFoundException;
import com.petmanagement.health.domain.model.aggregate.Vaccine;
import com.petmanagement.health.domain.model.valueobject.NextDueDate;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.health.domain.model.valueobject.VaccineId;
import com.petmanagement.health.domain.model.valueobject.VaccineName;
import com.petmanagement.health.support.InMemoryVaccineRepository;
import com.petmanagement.shared.application.port.out.EventPublisherPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RescheduleVaccineServiceTest {

    private InMemoryVaccineRepository repository;
    private EventPublisherPort publisher;
    private RescheduleVaccineService service;

    private static final PetId PET_ID = PetId.of("f7e64880-f033-4c5b-ab7a-a63d87578802");
    private static final LocalDate VACCINATION_DATE = LocalDate.of(2026, 5, 1);
    private static final VaccineName VACCINE_NAME = new VaccineName("Rabies");
    private static final NextDueDate NEXT_DUE_DATE = new NextDueDate(LocalDate.of(2027, 5, 1));

    @BeforeEach
    void setUp() {
        repository = new InMemoryVaccineRepository();
        publisher = mock(EventPublisherPort.class);

        service = new RescheduleVaccineService(repository, publisher);
    }

    @Test
    void shouldRescheduleVaccine() {
        // Given
        var vaccine = Vaccine.create(
                PET_ID,
                VACCINATION_DATE,
                VACCINE_NAME,
                NEXT_DUE_DATE
        );
        repository.save(vaccine);

        var command = new RescheduleVaccineUseCase.RescheduleVaccineCommand(
                vaccine.getId(),
                LocalDate.of(2027, 8, 1)
        );

        // When
        var result = service.execute(command);

        // Then
        assertEquals(command.nextDueDate(), result.getNextDueDate().value());
    }

    @Test
    void shouldPublishVaccineRescheduledEvent() {
        // Given
        var vaccine = Vaccine.create(
                PET_ID,
                VACCINATION_DATE,
                VACCINE_NAME,
                NEXT_DUE_DATE
        );
        vaccine.pullEvents();
        repository.save(vaccine);

        var command = new RescheduleVaccineUseCase.RescheduleVaccineCommand(
                vaccine.getId(),
                LocalDate.of(2027, 8, 1)
        );

        // When
        service.execute(command);

        // Then
        var eventCaptor = ArgumentCaptor.forClass(List.class);
        verify(publisher).publishAll(eventCaptor.capture());

        var events = eventCaptor.getValue();
        assertEquals(1, events.size());
        assertInstanceOf(VaccineRescheduled.class, events.getFirst());
    }

    @Test
    void shouldThrowWhenVaccineNotFound() {
        // Given
        var command = new RescheduleVaccineUseCase.RescheduleVaccineCommand(
                VaccineId.of("6b1a2ff4-ebd0-4027-9760-3bfefcc308d1"),
                LocalDate.of(2027, 8, 1)
        );

        // When/Then
        assertThrows(
                VaccineNotFoundException.class,
                () -> service.execute(command)
        );
    }

    @Test
    void shouldNotPublishEventWhenVaccineDoesNotExist() {
        // Given
        var command = new RescheduleVaccineUseCase.RescheduleVaccineCommand(
                VaccineId.of("6b1a2ff4-ebd0-4027-9760-3bfefcc308d1"),
                LocalDate.of(2027, 8, 1)
        );

        // When/Then
        assertThrows(
                VaccineNotFoundException.class,
                () -> service.execute(command)
        );

        verify(publisher, never()).publishAll(any());
    }

}
