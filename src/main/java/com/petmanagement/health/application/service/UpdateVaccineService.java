package com.petmanagement.health.application.service;

import com.petmanagement.health.application.port.in.UpdateVaccineUseCase;
import com.petmanagement.health.application.port.out.VaccineRepositoryPort;
import com.petmanagement.health.domain.exception.VaccineNotFoundException;
import com.petmanagement.health.domain.model.aggregate.Vaccine;
import com.petmanagement.shared.application.port.out.EventPublisherPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class UpdateVaccineService implements UpdateVaccineUseCase {

    private final VaccineRepositoryPort repository;
    private final EventPublisherPort publisher;

    public UpdateVaccineService(VaccineRepositoryPort repository, EventPublisherPort publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    @Override
    @Transactional
    public Vaccine execute(UpdateVaccineCommand command) {
        var vaccine = repository.findById(command.vaccineId())
                .orElseThrow(VaccineNotFoundException::new);

        vaccine.update(command.vaccinationDate(), command.vaccineName());

        repository.save(vaccine);

        publisher.publishAll(vaccine.pullEvents());

        return vaccine;
    }

}
