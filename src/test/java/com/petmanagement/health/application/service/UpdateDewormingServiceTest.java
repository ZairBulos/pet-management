package com.petmanagement.health.application.service;

import com.petmanagement.health.domain.event.DewormingUpdated;
import com.petmanagement.health.domain.exception.DewormingNotFoundException;
import com.petmanagement.health.support.DewormingTestBuilder;
import com.petmanagement.health.support.InMemoryDewormingRepository;
import com.petmanagement.health.support.TestDewormingMother;
import com.petmanagement.shared.application.port.out.EventPublisherPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UpdateDewormingServiceTest {

    private InMemoryDewormingRepository repository;
    private EventPublisherPort publisher;
    private UpdateDewormingService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryDewormingRepository();
        publisher = mock(EventPublisherPort.class);

        service = new UpdateDewormingService(repository, publisher);
    }

    @Nested
    class WhenUpdatingDeworming {

        @Test
        void shouldUpdateDeworming() {
            // Given
            var deworming = DewormingTestBuilder.aDeworming().build();
            repository.save(deworming);

            var command = DewormingTestBuilder.UpdateDewormingCommandBuilder
                    .aUpdateDewormingCommand()
                    .withId(deworming.getId())
                    .build();

            // When
            var result = service.execute(command);

            // Then
            assertNotNull(result);
            assertEquals(command.dewormingDate(), result.getDewormingDate());
            assertEquals(command.drugName(), result.getDrugName());
            assertEquals(command.drugDose(), result.getDrugDose());
        }

        @Test
        void shouldPublishDewormingUpdatedEvent() {
            // Given
            var deworming = DewormingTestBuilder.aDeworming().build();
            deworming.pullEvents();
            repository.save(deworming);

            var command = DewormingTestBuilder.UpdateDewormingCommandBuilder
                    .aUpdateDewormingCommand()
                    .withId(deworming.getId())
                    .build();

            // When
            service.execute(command);

            // Then
            var eventCaptor = ArgumentCaptor.forClass(List.class);
            verify(publisher).publishAll(eventCaptor.capture());

            var events = eventCaptor.getValue();
            assertEquals(1, events.size());
            assertInstanceOf(DewormingUpdated.class, events.getFirst());
        }

    }

    @Nested
    class WhenDewormingDoesNotExist {

        @Test
        void shouldThrowWhenDewormingDoesNotExist() {
            // Given
            var command = DewormingTestBuilder.UpdateDewormingCommandBuilder
                    .aUpdateDewormingCommand()
                    .withId(TestDewormingMother.NON_EXISTENT_DEWORMING_ID)
                    .build();

            // When/Then
            assertThrows(
                    DewormingNotFoundException.class,
                    () -> service.execute(command)
            );
        }

        @Test
        void shouldNotPublishEventWhenDewormingDoesNotExist() {
            // Given
            var command = DewormingTestBuilder.UpdateDewormingCommandBuilder
                    .aUpdateDewormingCommand()
                    .withId(TestDewormingMother.NON_EXISTENT_DEWORMING_ID)
                    .build();

            // When/Then
            assertThrows(
                    DewormingNotFoundException.class,
                    () -> service.execute(command)
            );

            verify(publisher, never()).publishAll(any());
        }

    }

}
