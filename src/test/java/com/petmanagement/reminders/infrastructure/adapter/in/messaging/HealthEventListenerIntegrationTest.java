package com.petmanagement.reminders.infrastructure.adapter.in.messaging;

import com.petmanagement.health.domain.event.DewormingCreated;
import com.petmanagement.health.domain.event.DewormingRescheduled;
import com.petmanagement.health.domain.event.VaccineCreated;
import com.petmanagement.health.domain.event.VaccineRescheduled;
import com.petmanagement.reminders.application.port.out.ReminderRepositoryPort;
import com.petmanagement.reminders.domain.model.valueobject.SourceId;
import com.petmanagement.reminders.support.TestReminderMother;
import com.petmanagement.support.ModuleTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.Scenario;

import java.time.Instant;
import java.util.UUID;

import static com.petmanagement.reminders.support.ReminderTestBuilder.aReminder;
import static org.assertj.core.api.Assertions.assertThat;

@ModuleTest
class HealthEventListenerIntegrationTest {

    @Autowired
    private ReminderRepositoryPort repository;

    @Nested
    class WhenVaccineEventsAreReceived {

        @Test
        void shouldCreateReminderWhenVaccineCreated(Scenario scenario) {
            // Given
            var sourceId = SourceId.of(UUID.randomUUID());

            var event = new VaccineCreated(
                    sourceId.value(),
                    TestReminderMother.EXISTING_PET_ID.value(),
                    TestReminderMother.VACCINE_TITLE.value(),
                    TestReminderMother.DUE_DATE_THREE_MONTHS.value(),
                    Instant.now()
            );

            // When/Then
            scenario.publish(event)
                    .andWaitForStateChange(() -> repository.findBySourceId(sourceId).orElse(null))
                    .andVerify(reminder -> {
                        assertThat(reminder).isNotNull();
                        assertThat(reminder.getPetId().value()).isEqualTo(event.petId());
                        assertThat(reminder.getDueDate().value()).isEqualTo(event.nextDueDate());
                    });
        }

        @Test
        void shouldRescheduleReminderWhenVaccineRescheduled(Scenario scenario) {
            // Given
            var sourceId = SourceId.of(UUID.randomUUID());

            repository.save(aReminder().vaccine().withSourceId(sourceId).build());

            var event = new VaccineRescheduled(
                    sourceId.value(),
                    TestReminderMother.DUE_DATE_SIX_MONTHS.value(),
                    Instant.now()
            );

            // When/Then
            scenario.publish(event)
                    .andWaitForStateChange(() -> repository.findBySourceId(sourceId).orElse(null))
                    .andVerify(reminder -> {
                        assertThat(reminder).isNotNull();
                        assertThat(reminder.getDueDate().value()).isEqualTo(event.nextDueDate());
                    });
        }

    }

    @Nested
    class WhenDewormingEventsAreReceived {

        @Test
        void shouldCreateReminderWhenDewormingCreated(Scenario scenario) {
            // Given
            var sourceId = SourceId.of(UUID.randomUUID());

            var event = new DewormingCreated(
                    sourceId.value(),
                    TestReminderMother.EXISTING_PET_ID.value(),
                    TestReminderMother.DEWORMING_TITLE.value(),
                    TestReminderMother.DUE_DATE_THREE_MONTHS.value(),
                    Instant.now()
            );

            // When/Then
            scenario.publish(event)
                    .andWaitForStateChange(() -> repository.findBySourceId(sourceId).orElse(null))
                    .andVerify(reminder -> {
                        assertThat(reminder).isNotNull();
                        assertThat(reminder.getPetId().value()).isEqualTo(event.petId());
                        assertThat(reminder.getDueDate().value()).isEqualTo(event.nextDueDate());
                    });
        }

        @Test
        void shouldRescheduleReminderWhenDewormingRescheduled(Scenario scenario) {
            // Given
            var sourceId = SourceId.of(UUID.randomUUID());

            repository.save(aReminder().deworming().withSourceId(sourceId).build());

            var event = new DewormingRescheduled(
                    sourceId.value(),
                    TestReminderMother.DUE_DATE_SIX_MONTHS.value(),
                    Instant.now()
            );

            // When/Then
            scenario.publish(event)
                    .andWaitForStateChange(() -> repository.findBySourceId(sourceId).orElse(null))
                    .andVerify(reminder -> {
                        assertThat(reminder).isNotNull();
                        assertThat(reminder.getDueDate().value()).isEqualTo(event.nextDueDate());
                    });
        }

    }

}
