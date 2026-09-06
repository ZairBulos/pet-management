package com.petmanagement.shared.infrastructure.adapter.out.messaging;

import com.petmanagement.shared.application.port.out.EventPublisherPort;
import org.jmolecules.event.annotation.DomainEventPublisher;
import org.jmolecules.event.types.DomainEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class EventPublisherAdapter implements EventPublisherPort {

    private final ApplicationEventPublisher publisher;

    public EventPublisherAdapter(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    @DomainEventPublisher(type = DomainEventPublisher.PublisherType.INTERNAL)
    public void publish(DomainEvent event) {
        publisher.publishEvent(event);
    }

    @Override
    public void publishAll(List<DomainEvent> events) {
        events.forEach(this::publish);
    }

}
