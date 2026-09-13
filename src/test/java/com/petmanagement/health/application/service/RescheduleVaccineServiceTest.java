package com.petmanagement.health.application.service;

import com.petmanagement.health.domain.event.VaccineRescheduled;
import com.petmanagement.health.domain.exception.VaccineNotFoundException;
import com.petmanagement.health.support.InMemoryVaccineRepository;
import com.petmanagement.health.support.TestVaccineMother;
import com.petmanagement.health.support.VaccineTestBuilder;
import com.petmanagement.shared.application.port.out.EventPublisherPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RescheduleVaccineServiceTest {

    private InMemoryVaccineRepository repository;
    private EventPublisherPort publisher;
    private RescheduleVaccineService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryVaccineRepository();
        publisher = mock(EventPublisherPort.class);

        service = new RescheduleVaccineService(repository, publisher);
    }

    @Nested
    class WhenReschedulingVaccine {

        @Test
        void shouldRescheduleVaccine() {
            // Given
            var vaccine = VaccineTestBuilder.aVaccine().build();
            repository.save(vaccine);

            var command = VaccineTestBuilder.RescheduleVaccineCommandBuilder
                    .aRescheduleVaccineCommand()
                    .withId(vaccine.getId())
                    .build();

            // When
            var result = service.execute(command);

            // Then
            assertNotNull(result);
            assertEquals(command.nextDueDate(), result.getNextDueDate().value());
        }

        @Test
        void shouldPublishVaccineRescheduledEvent() {
            // Given
            var vaccine = VaccineTestBuilder.aVaccine().build();
            vaccine.pullEvents();
            repository.save(vaccine);

            var command = VaccineTestBuilder.RescheduleVaccineCommandBuilder
                    .aRescheduleVaccineCommand()
                    .withId(vaccine.getId())
                    .build();

            // When
            service.execute(command);

            // Then
            var eventCaptor = ArgumentCaptor.forClass(List.class);
            verify(publisher).publishAll(eventCaptor.capture());

            var events = eventCaptor.getValue();
            assertEquals(1, events.size());
            assertInstanceOf(VaccineRescheduled.class, events.getFirst());
        }

    }

    @Nested
    class WhenVaccineDoesNotExist {

        @Test
        void shouldThrowWhenVaccineDoesNotExist() {
            // Given
            var command = VaccineTestBuilder.RescheduleVaccineCommandBuilder
                    .aRescheduleVaccineCommand()
                    .withId(TestVaccineMother.NON_EXISTENT_VACCINE_ID)
                    .build();

            // When/Then
            assertThrows(
                    VaccineNotFoundException.class,
                    () -> service.execute(command)
            );
        }

        @Test
        void shouldNotPublishEventWhenVaccineDoesNotExist() {
            // Given
            var command = VaccineTestBuilder.RescheduleVaccineCommandBuilder
                    .aRescheduleVaccineCommand()
                    .withId(TestVaccineMother.NON_EXISTENT_VACCINE_ID)
                    .build();

            // When/Then
            assertThrows(
                    VaccineNotFoundException.class,
                    () -> service.execute(command)
            );

            verify(publisher, never()).publishAll(any());
        }

    }

}
