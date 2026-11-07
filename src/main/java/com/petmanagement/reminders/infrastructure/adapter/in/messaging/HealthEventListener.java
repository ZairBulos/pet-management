package com.petmanagement.reminders.infrastructure.adapter.in.messaging;

import com.petmanagement.health.domain.event.DewormingCreated;
import com.petmanagement.health.domain.event.DewormingRescheduled;
import com.petmanagement.health.domain.event.VaccineCreated;
import com.petmanagement.health.domain.event.VaccineRescheduled;
import com.petmanagement.reminders.application.port.in.CreateReminderUseCase;
import com.petmanagement.reminders.application.port.in.RescheduleReminderUseCase;
import com.petmanagement.reminders.domain.model.enums.SourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
class HealthEventListener {

    private static final Logger log = LoggerFactory.getLogger(HealthEventListener.class);

    private final CreateReminderUseCase createReminderUseCase;
    private final RescheduleReminderUseCase rescheduleReminderUseCase;

    HealthEventListener(
            CreateReminderUseCase createReminderUseCase,
            RescheduleReminderUseCase rescheduleReminderUseCase
    ) {
        this.createReminderUseCase = createReminderUseCase;
        this.rescheduleReminderUseCase = rescheduleReminderUseCase;
    }

    @ApplicationModuleListener
    void on(VaccineCreated event) {
        log.info("Received VaccineCreated event={}", event);

        var reminderId = createReminderUseCase.execute(new CreateReminderUseCase.CreateReminderCommand(
                event.vaccineId(),
                SourceType.VACCINE,
                event.petId(),
                event.vaccineName(),
                event.nextDueDate()
        ));

        log.info("Vaccine reminder created. reminderId={}", reminderId);
    }

    @ApplicationModuleListener
    void on(DewormingCreated event) {
        log.info("Received DewormingCreated event={}", event);

        var reminderId = createReminderUseCase.execute(new CreateReminderUseCase.CreateReminderCommand(
                event.dewormingId(),
                SourceType.DEWORMING,
                event.petId(),
                event.drugName(),
                event.nextDueDate()
        ));

        log.info("Deworming reminder created. reminderId={}", reminderId);
    }

    @ApplicationModuleListener
    void on(VaccineRescheduled event) {
        log.info("Received VaccineRescheduled event={}", event);

        rescheduleReminderUseCase.execute(new RescheduleReminderUseCase.RescheduleReminderCommand(
                event.vaccineId(),
                SourceType.VACCINE,
                event.nextDueDate()
        ));
    }

    @ApplicationModuleListener
    void on(DewormingRescheduled event) {
        log.info("Received DewormingRescheduled event={}", event);

        rescheduleReminderUseCase.execute(new RescheduleReminderUseCase.RescheduleReminderCommand(
                event.dewormingId(),
                SourceType.DEWORMING,
                event.nextDueDate()
        ));
    }

}
