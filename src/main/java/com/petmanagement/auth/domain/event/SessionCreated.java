package com.petmanagement.auth.domain.event;

import org.jmolecules.event.annotation.DomainEvent;

import java.time.Instant;
import java.util.UUID;

@DomainEvent(name = "SessionCreated", namespace = "auth.session")
public record SessionCreated(
        UUID sessionId,
        UUID ownerId,
        Instant occurredOn
) implements org.jmolecules.event.types.DomainEvent {
}
