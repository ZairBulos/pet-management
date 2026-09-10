package com.petmanagement.health.application.service;

import com.petmanagement.health.application.port.in.RescheduleDewormingUseCase;
import com.petmanagement.health.application.port.in.RescheduleVaccineUseCase;
import com.petmanagement.health.domain.event.DewormingRescheduled;
import com.petmanagement.health.domain.exception.DewormingNotFoundException;
import com.petmanagement.health.domain.model.aggregate.Deworming;
import com.petmanagement.health.domain.model.valueobject.*;
import com.petmanagement.health.support.InMemoryDewormingRepository;
import com.petmanagement.shared.application.port.out.EventPublisherPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RescheduleDewormingServiceTest {

    private InMemoryDewormingRepository repository;
    private EventPublisherPort publisher;
    private RescheduleDewormingService service;

    private static final PetId PET_ID = PetId.of("a5ff5e24-adbc-47e9-8403-4be7ba2ef54d");
    private static final LocalDate DEWORMING_DATE = LocalDate.of(2026, 3, 1);
    private static final DrugName DRUG_NAME = new DrugName("Milbemax");
    private static final DrugDose DRUG_DOSE = new DrugDose("1 tablet (2mg)");
    private static final NextDueDate NEXT_DUE_DATE = new NextDueDate(LocalDate.of(2027, 6, 1));

    @BeforeEach
    void setUp() {
        repository = new InMemoryDewormingRepository();
        publisher = mock(EventPublisherPort.class);

        service = new RescheduleDewormingService(repository, publisher);
    }

    @Test
    void shouldRescheduleDeworming() {
        // Given
        var deworming = Deworming.create(
                PET_ID,
                DEWORMING_DATE,
                DRUG_NAME,
                DRUG_DOSE,
                NEXT_DUE_DATE
        );
        repository.save(deworming);

        var command = new RescheduleDewormingUseCase.RescheduleDewormingCommand(
                deworming.getId(),
                LocalDate.of(2026, 7, 1)
        );

        // When
        var result = service.execute(command);

        // Then
        assertEquals(command.nextDueDate(), result.getNextDueDate().value());
    }

    @Test
    void shouldPublishDewormingRescheduledEvent() {
        // Given
        var deworming = Deworming.create(
                PET_ID,
                DEWORMING_DATE,
                DRUG_NAME,
                DRUG_DOSE,
                NEXT_DUE_DATE
        );
        deworming.pullEvents();
        repository.save(deworming);

        var command = new RescheduleDewormingUseCase.RescheduleDewormingCommand(
                deworming.getId(),
                LocalDate.of(2026, 7, 1)
        );

        // When
        service.execute(command);

        // Then
        var eventCaptor = ArgumentCaptor.forClass(List.class);
        verify(publisher).publishAll(eventCaptor.capture());

        var events = eventCaptor.getValue();
        assertEquals(1, events.size());
        assertInstanceOf(DewormingRescheduled.class, events.getFirst());
    }

    @Test
    void shouldThrowWhenDewormingNotFound() {
        // Given
        var command = new RescheduleDewormingUseCase.RescheduleDewormingCommand(
                DewormingId.of("6039bcc1-bfc3-45cf-ae72-32f9d33268e1"),
                LocalDate.of(2026, 7, 1)
        );

        // When/Then
        assertThrows(
                DewormingNotFoundException.class,
                () -> service.execute(command)
        );
    }

    @Test
    void shouldNotPublishEventWhenDewormingDoesNotExist() {
        // Given
        var command = new RescheduleDewormingUseCase.RescheduleDewormingCommand(
                DewormingId.of("6039bcc1-bfc3-45cf-ae72-32f9d33268e1"),
                LocalDate.of(2026, 7, 1)
        );

        // When/Then
        assertThrows(
                DewormingNotFoundException.class,
                () -> service.execute(command)
        );

        verify(publisher, never()).publishAll(any());
    }

}
