package com.petmanagement.health.application.service;

import com.petmanagement.health.domain.event.VaccineCreated;
import com.petmanagement.health.domain.exception.PetNotFoundException;
import com.petmanagement.health.support.InMemoryVaccineRepository;
import com.petmanagement.health.support.TestPetIdMother;
import com.petmanagement.health.support.VaccineTestBuilder;
import com.petmanagement.pets.api.PetApi;
import com.petmanagement.shared.application.port.out.EventPublisherPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateVaccineServiceTest {

    private InMemoryVaccineRepository repository;
    private EventPublisherPort publisher;
    private PetApi petApi;
    private CreateVaccineService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryVaccineRepository();
        publisher = mock(EventPublisherPort.class);
        petApi = mock(PetApi.class);

        service = new CreateVaccineService(repository, publisher, petApi);
    }

    @Nested
    class WhenCreatingVaccine {

        @Test
        void shouldCreateVaccine() {
            // Given
            var command = VaccineTestBuilder.CreateVaccineCommandBuilder
                    .aCreateVaccineCommand()
                    .build();

            when(petApi.existsById(command.petId().value()))
                    .thenReturn(true);

            // When
            var result = service.execute(command);

            // Then
            assertNotNull(result);
        }

        @Test
        void shouldPublishVaccineCreatedEvent() {
            // Given
            var command = VaccineTestBuilder.CreateVaccineCommandBuilder
                    .aCreateVaccineCommand()
                    .build();

            when(petApi.existsById(command.petId().value()))
                    .thenReturn(true);

            // When
            service.execute(command);

            // Then
            var eventCaptor = ArgumentCaptor.forClass(List.class);
            verify(publisher).publishAll(eventCaptor.capture());

            var events = eventCaptor.getValue();
            assertEquals(1, events.size());
            assertInstanceOf(VaccineCreated.class, events.getFirst());
        }

    }

    @Nested
    class WhenPetDoesNotExist {

        @Test
        void shouldThrowWhenPetDoesNotExist() {
            // Given
            var command = VaccineTestBuilder.CreateVaccineCommandBuilder
                    .aCreateVaccineCommand()
                    .withPetId(TestPetIdMother.NON_EXISTENT_PET_ID)
                    .build();

            when(petApi.existsById(command.petId().value()))
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
            var command = VaccineTestBuilder.CreateVaccineCommandBuilder
                    .aCreateVaccineCommand()
                    .withPetId(TestPetIdMother.NON_EXISTENT_PET_ID)
                    .build();

            when(petApi.existsById(command.petId().value()))
                    .thenReturn(false);

            // When/Then
            assertThrows(
                    PetNotFoundException.class,
                    () -> service.execute(command)
            );

            verify(publisher, never()).publishAll(any());
        }

    }

}
