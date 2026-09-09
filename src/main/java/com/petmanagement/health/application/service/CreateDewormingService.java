package com.petmanagement.health.application.service;

import com.petmanagement.health.application.port.in.CreateDewormingUseCase;
import com.petmanagement.health.application.port.out.DewormingRepositoryPort;
import com.petmanagement.health.domain.exception.PetNotFoundException;
import com.petmanagement.health.domain.model.aggregate.Deworming;
import com.petmanagement.health.domain.model.valueobject.DewormingId;
import com.petmanagement.pets.api.PetApi;
import com.petmanagement.shared.application.port.out.EventPublisherPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class CreateDewormingService implements CreateDewormingUseCase {

    private final DewormingRepositoryPort repository;
    private final EventPublisherPort publisher;
    private final PetApi petApi;

    public CreateDewormingService(DewormingRepositoryPort repository, EventPublisherPort publisher, PetApi petApi) {
        this.repository = repository;
        this.publisher = publisher;
        this.petApi = petApi;
    }

    @Override
    @Transactional
    public DewormingId execute(CreateDewormingCommand command) {
        var petExists = petApi.existsById(command.petId().value());

        if (!petExists)
            throw new PetNotFoundException();

        var deworming = Deworming.create(
                command.petId(),
                command.dewormingDate(),
                command.drugName(),
                command.drugDose(),
                command.nextDueDate()
        );

        repository.save(deworming);

        publisher.publishAll(deworming.pullEvents());

        return deworming.getId();
    }

}
