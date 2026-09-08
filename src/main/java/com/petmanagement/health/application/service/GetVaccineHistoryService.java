package com.petmanagement.health.application.service;

import com.petmanagement.health.application.port.in.GetVaccineHistoryUseCase;
import com.petmanagement.health.application.port.out.VaccineRepositoryPort;
import com.petmanagement.health.domain.exception.PetNotFoundException;
import com.petmanagement.health.domain.model.aggregate.Vaccine;
import com.petmanagement.pets.api.PetApi;
import com.petmanagement.shared.domain.model.PageResponse;
import org.springframework.stereotype.Service;

@Service
class GetVaccineHistoryService implements GetVaccineHistoryUseCase {

    private final VaccineRepositoryPort repository;
    private final PetApi petApi;

    public GetVaccineHistoryService(VaccineRepositoryPort repository, PetApi petApi) {
        this.repository = repository;
        this.petApi = petApi;
    }

    @Override
    public PageResponse<Vaccine> execute(GetVaccineHistoryQuery query) {
        var petExists = petApi.existsById(query.petId().value());

        if (!petExists)
            throw new PetNotFoundException();

        return repository.findByPetId(query.petId(), query.pageRequest());
    }

}
