package com.petmanagement.health.application.service;

import com.petmanagement.health.application.port.in.CreateVaccineUseCase;
import com.petmanagement.health.application.port.out.VaccineRepositoryPort;
import com.petmanagement.health.domain.exception.PetNotFoundException;
import com.petmanagement.health.domain.model.aggregate.Vaccine;
import com.petmanagement.health.domain.model.valueobject.VaccineId;
import com.petmanagement.pets.api.PetApi;
import com.petmanagement.shared.application.port.out.EventPublisherPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class CreateVaccineService implements CreateVaccineUseCase {

    private final VaccineRepositoryPort repository;
    private final EventPublisherPort publisher;
    private final PetApi petApi;

    public CreateVaccineService(VaccineRepositoryPort repository, EventPublisherPort publisher, PetApi petApi) {
        this.repository = repository;
        this.publisher = publisher;
        this.petApi = petApi;
    }

    @Override
    @Transactional
    public VaccineId execute(CreateVaccineCommand command) {
        var exists = petApi.existsById(command.petId().value());

        if (!exists)
            throw new PetNotFoundException();

        var vaccine = Vaccine.create(
                command.petId(),
                command.vaccinationDate(),
                command.vaccineName(),
                command.nextDueDate()
        );

        repository.save(vaccine);

        publisher.publishAll(vaccine.pullEvents());

        return vaccine.getId();
    }

}
