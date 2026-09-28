package com.petmanagement.auth.domain.event;

import org.jmolecules.event.annotation.DomainEvent;

import java.time.Instant;
import java.util.UUID;

@DomainEvent(name = "SessionRevoked", namespace = "auth.session")
public record SessionRevoked(
        UUID sessionId,
        UUID ownerId,
        String reason,
        Instant occurredOn
) implements org.jmolecules.event.types.DomainEvent {
}
