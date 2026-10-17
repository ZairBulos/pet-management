package com.petmanagement.health.domain.event;

import org.jmolecules.event.annotation.DomainEvent;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@DomainEvent(name = "VaccineCreated", namespace = "health.vaccine")
public record VaccineCreated(
        UUID vaccineId,
        UUID petId,
        String vaccineName,
        LocalDate nextDueDate,
        Instant occurredOn
) implements org.jmolecules.event.types.DomainEvent {
}
