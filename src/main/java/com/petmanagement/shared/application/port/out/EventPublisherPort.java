package com.petmanagement.shared.application.port.out;

import org.jmolecules.event.types.DomainEvent;

import java.util.List;

public interface EventPublisherPort {
    void publish(DomainEvent event);
    void publishAll(List<DomainEvent> events);
}
