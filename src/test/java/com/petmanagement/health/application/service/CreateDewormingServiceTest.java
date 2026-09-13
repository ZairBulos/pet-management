package com.petmanagement.health.application.service;

import com.petmanagement.health.domain.event.DewormingCreated;
import com.petmanagement.health.domain.exception.PetNotFoundException;
import com.petmanagement.health.support.DewormingTestBuilder;
import com.petmanagement.health.support.InMemoryDewormingRepository;
import com.petmanagement.health.support.TestPetIdMother;
import com.petmanagement.pets.api.PetApi;
import com.petmanagement.shared.application.port.out.EventPublisherPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateDewormingServiceTest {

    private InMemoryDewormingRepository repository;
    private EventPublisherPort publisher;
    private PetApi petApi;
    private CreateDewormingService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryDewormingRepository();
        publisher = mock(EventPublisherPort.class);
        petApi = mock(PetApi.class);

        service = new CreateDewormingService(repository, publisher, petApi);
    }

    @Nested
    class WhenCreatingDeworming {

        @Test
        void shouldCreateDeworming() {
            // Given
            var command = DewormingTestBuilder.CreateDewormingCommandBuilder
                    .aCreateDewormingCommand()
                    .build();

            when(petApi.existsById(command.petId().value()))
                    .thenReturn(true);

            // When
            var dewormingId = service.execute(command);

            // Then
            assertNotNull(dewormingId);
        }

        @Test
        void shouldPublishDewormingCreatedEvent() {
            // Given
            var command = DewormingTestBuilder.CreateDewormingCommandBuilder
                    .aCreateDewormingCommand()
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
            assertInstanceOf(DewormingCreated.class, events.getFirst());
        }

    }

    @Nested
    class WhenPetDoesNotExist {

        @Test
        void shouldThrowWhenPetDoesNotExist() {
            // Given
            var command = DewormingTestBuilder.CreateDewormingCommandBuilder
                    .aCreateDewormingCommand()
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
            var command = DewormingTestBuilder.CreateDewormingCommandBuilder
                    .aCreateDewormingCommand()
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
