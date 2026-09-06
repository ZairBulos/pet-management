package com.petmanagement.health.domain.event;

import org.jmolecules.event.annotation.DomainEvent;

import java.time.Instant;
import java.util.UUID;

@DomainEvent(name = "VaccineUpdated", namespace = "health.vaccine")
public record VaccineUpdated(
        UUID vaccineId,
        String vaccineName,
        Instant occurredOn
) implements org.jmolecules.event.types.DomainEvent {
}
