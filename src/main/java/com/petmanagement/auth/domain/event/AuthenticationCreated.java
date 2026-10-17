package com.petmanagement.auth.domain.event;

import org.jmolecules.event.annotation.DomainEvent;

import java.time.Instant;
import java.util.UUID;

@DomainEvent(name = "AuthenticationCreated", namespace = "auth.authentication")
public record AuthenticationCreated(
        UUID authenticationId,
        String email,
        String code,
        Instant occurredOn
) implements org.jmolecules.event.types.DomainEvent {
}
