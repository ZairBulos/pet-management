package com.petmanagement.health.application.service;

import com.petmanagement.health.application.port.in.UpdateDewormingUseCase;
import com.petmanagement.health.application.port.out.DewormingRepositoryPort;
import com.petmanagement.health.domain.exception.DewormingNotFoundException;
import com.petmanagement.health.domain.model.aggregate.Deworming;
import com.petmanagement.shared.application.port.out.EventPublisherPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class UpdateDewormingService implements UpdateDewormingUseCase {

    private final DewormingRepositoryPort repository;
    private final EventPublisherPort publisher;

    public UpdateDewormingService(DewormingRepositoryPort repository, EventPublisherPort publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    @Override
    @Transactional
    public Deworming execute(UpdateDewormingCommand command) {
        var deworming = repository.findById(command.dewormingId())
                .orElseThrow(DewormingNotFoundException::new);

        deworming.update(
                command.dewormingDate(),
                command.drugName(),
                command.drugDose()
        );

        repository.save(deworming);

        publisher.publishAll(deworming.pullEvents());

        return deworming;
    }

}
