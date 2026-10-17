package com.petmanagement.health.domain.event;

import org.jmolecules.event.annotation.DomainEvent;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@DomainEvent(name = "DewormingCreated", namespace = "health.deworming")
public record DewormingCreated(
        UUID dewormingId,
        UUID petId,
        String drugName,
        LocalDate nextDueDate,
        Instant occurredOn
) implements org.jmolecules.event.types.DomainEvent {
}
