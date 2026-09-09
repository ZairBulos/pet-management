package com.petmanagement.health.domain.event;

import org.jmolecules.event.annotation.DomainEvent;

import java.time.Instant;
import java.util.UUID;

@DomainEvent(name = "DewormingUpdated", namespace = "health.deworming")
public record DewormingUpdated(
        UUID dewormingId,
        String drugName,
        Instant occurredOn
) implements org.jmolecules.event.types.DomainEvent {
}
