package com.petmanagement.health.application.service;

import com.petmanagement.health.application.port.in.UpdateVaccineUseCase;
import com.petmanagement.health.domain.event.VaccineUpdated;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UpdateVaccineServiceTest {

    private InMemoryVaccineRepository repository;
    private EventPublisherPort publisher;
    private UpdateVaccineService service;

    private static final PetId PET_ID = PetId.of("80a59ea2-7d21-458b-96dc-bd812f14e9f9");
    private static final LocalDate VACCINATION_DATE = LocalDate.of(2026, 5, 1);
    private static final VaccineName VACCINE_NAME = new VaccineName("Rabies");
    private static final NextDueDate NEXT_DUE_DATE = new NextDueDate(LocalDate.of(2027, 5, 1));

    @BeforeEach
    void setUp() {
        repository = new InMemoryVaccineRepository();
        publisher = mock(EventPublisherPort.class);

        service = new UpdateVaccineService(repository, publisher);
    }

    @Test
    void shouldUpdateVaccine() {
        // Given
        var vaccine = Vaccine.create(
                PET_ID,
                VACCINATION_DATE,
                VACCINE_NAME,
                NEXT_DUE_DATE
        );
        repository.save(vaccine);

        var command = new UpdateVaccineUseCase.UpdateVaccineCommand(
                vaccine.getId(),
                LocalDate.of(2026, 6, 1),
                new VaccineName("Distemper")
        );

        // When
        var result = service.execute(command);

        // Then
        assertEquals(command.vaccinationDate(), result.getVaccinationDate());
        assertEquals(command.vaccineName(), result.getVaccineName());
    }

    @Test
    void shouldPublishVaccineUpdatedEvent() {
        // Given
        var vaccine = Vaccine.create(
                PET_ID,
                VACCINATION_DATE,
                VACCINE_NAME,
                NEXT_DUE_DATE
        );
        vaccine.pullEvents();
        repository.save(vaccine);

        var command = new UpdateVaccineUseCase.UpdateVaccineCommand(
                vaccine.getId(),
                LocalDate.of(2026, 6, 1),
                new VaccineName("Distemper")
        );

        // When
        service.execute(command);

        // Then
        var eventCaptor = ArgumentCaptor.forClass(List.class);
        verify(publisher).publishAll(eventCaptor.capture());

        var events = eventCaptor.getValue();
        assertEquals(1, events.size());
        assertInstanceOf(VaccineUpdated.class, events.getFirst());
    }

    @Test
    void shouldThrowWhenVaccineNotFound() {
        // Given
        var command = new UpdateVaccineUseCase.UpdateVaccineCommand(
                VaccineId.of("8b371867-cec0-4448-ace9-6f0ce39471ae"),
                LocalDate.now(),
                new VaccineName("Not Found")
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
        var command = new UpdateVaccineUseCase.UpdateVaccineCommand(
                VaccineId.of("8b371867-cec0-4448-ace9-6f0ce39471ae"),
                LocalDate.now(),
                new VaccineName("Not Found")
        );

        // When/Then
        assertThrows(
                VaccineNotFoundException.class,
                () -> service.execute(command)
        );

        verify(publisher, never()).publishAll(any());
    }

}
