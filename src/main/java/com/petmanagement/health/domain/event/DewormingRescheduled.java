package com.petmanagement.health.domain.event;

import org.jmolecules.event.annotation.DomainEvent;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@DomainEvent(name = "DewormingRescheduled", namespace = "health.deworming")
public record DewormingRescheduled(
        UUID dewormingId,
        LocalDate nextDueDate,
        Instant occurredOn
) implements org.jmolecules.event.types.DomainEvent {
}
