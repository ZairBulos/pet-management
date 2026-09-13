package com.petmanagement.health.application.service;

import com.petmanagement.health.domain.event.VaccineUpdated;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UpdateVaccineServiceTest {

    private InMemoryVaccineRepository repository;
    private EventPublisherPort publisher;
    private UpdateVaccineService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryVaccineRepository();
        publisher = mock(EventPublisherPort.class);

        service = new UpdateVaccineService(repository, publisher);
    }

    @Nested
    class WhenUpdatingVaccine {

        @Test
        void shouldUpdateVaccine() {
            // Given
            var vaccine = VaccineTestBuilder.aVaccine().build();
            repository.save(vaccine);

            var command = VaccineTestBuilder.UpdateVaccineCommandBuilder
                    .aUpdateVaccineCommand()
                    .withId(vaccine.getId())
                    .build();

            // When
            var result = service.execute(command);

            // Then
            assertNotNull(result);
            assertEquals(command.vaccinationDate(), result.getVaccinationDate());
            assertEquals(command.vaccineName(), result.getVaccineName());
        }

        @Test
        void shouldPublishVaccineUpdatedEvent() {
            // Given
            var vaccine = VaccineTestBuilder.aVaccine().build();
            vaccine.pullEvents();
            repository.save(vaccine);

            var command = VaccineTestBuilder.UpdateVaccineCommandBuilder
                    .aUpdateVaccineCommand()
                    .withId(vaccine.getId())
                    .build();

            // When
            service.execute(command);

            // Then
            var eventCaptor = ArgumentCaptor.forClass(List.class);
            verify(publisher).publishAll(eventCaptor.capture());

            var events = eventCaptor.getValue();
            assertEquals(1, events.size());
            assertInstanceOf(VaccineUpdated.class, events.getFirst());
        }

    }


    @Nested
    class WhenVaccineDoesNotExist {

        @Test
        void shouldThrowWhenVaccineDoesNotExist() {
            // Given
            var command = VaccineTestBuilder.UpdateVaccineCommandBuilder
                    .aUpdateVaccineCommand()
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
            var command = VaccineTestBuilder.UpdateVaccineCommandBuilder
                    .aUpdateVaccineCommand()
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
