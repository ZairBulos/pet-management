package com.petmanagement.health.application.service;

import com.petmanagement.health.application.port.in.GetDewormingHistoryUseCase;
import com.petmanagement.health.application.port.out.DewormingRepositoryPort;
import com.petmanagement.health.domain.exception.PetNotFoundException;
import com.petmanagement.health.domain.model.aggregate.Deworming;
import com.petmanagement.pets.api.PetApi;
import com.petmanagement.shared.domain.model.PageResponse;
import org.springframework.stereotype.Service;

@Service
class GetDewormingHistoryService implements GetDewormingHistoryUseCase {

    private final DewormingRepositoryPort repository;
    private final PetApi petApi;

    public GetDewormingHistoryService(DewormingRepositoryPort repository, PetApi petApi) {
        this.repository = repository;
        this.petApi = petApi;
    }

    @Override
    public PageResponse<Deworming> execute(GetDewormingHistoryQuery query) {
        var petExists = petApi.existsById(query.petId().value());

        if (!petExists)
            throw new PetNotFoundException();

        return repository.findByPetId(query.petId(), query.pageRequest());
    }

}
