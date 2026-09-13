package com.petmanagement.health.application.service;

import com.petmanagement.health.domain.event.DewormingRescheduled;
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

class RescheduleDewormingServiceTest {

    private InMemoryDewormingRepository repository;
    private EventPublisherPort publisher;
    private RescheduleDewormingService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryDewormingRepository();
        publisher = mock(EventPublisherPort.class);

        service = new RescheduleDewormingService(repository, publisher);
    }

    @Nested
    class WhenReschedulingDeworming {

        @Test
        void shouldRescheduleDeworming() {
            // Given
            var deworming = DewormingTestBuilder.aDeworming().build();
            repository.save(deworming);

            var command = DewormingTestBuilder.RescheduleDewormingCommandBuilder
                    .aRescheduleDewormingCommand()
                    .withId(deworming.getId())
                    .build();

            // When
            var result = service.execute(command);

            // Then
            assertNotNull(result);
            assertEquals(command.nextDueDate(), result.getNextDueDate().value());
        }

        @Test
        void shouldPublishDewormingRescheduledEvent() {
            // Given
            var deworming = DewormingTestBuilder.aDeworming().build();
            deworming.pullEvents();
            repository.save(deworming);

            var command = DewormingTestBuilder.RescheduleDewormingCommandBuilder
                    .aRescheduleDewormingCommand()
                    .withId(deworming.getId())
                    .build();

            // When
            service.execute(command);

            // Then
            var eventCaptor = ArgumentCaptor.forClass(List.class);
            verify(publisher).publishAll(eventCaptor.capture());

            var events = eventCaptor.getValue();
            assertEquals(1, events.size());
            assertInstanceOf(DewormingRescheduled.class, events.getFirst());
        }

    }

    @Nested
    class WhenDewormingDoesNotExist {

        @Test
        void shouldThrowWhenDewormingDoesNotExist() {
            // Given
            var command = DewormingTestBuilder.RescheduleDewormingCommandBuilder
                    .aRescheduleDewormingCommand()
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
            var command = DewormingTestBuilder.RescheduleDewormingCommandBuilder
                    .aRescheduleDewormingCommand()
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
