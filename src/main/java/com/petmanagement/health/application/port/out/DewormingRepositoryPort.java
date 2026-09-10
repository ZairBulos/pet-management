package com.petmanagement.health.application.port.out;

import com.petmanagement.health.domain.model.aggregate.Deworming;
import com.petmanagement.health.domain.model.valueobject.DewormingId;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.shared.domain.model.PageRequest;
import com.petmanagement.shared.domain.model.PageResponse;

import java.util.Optional;

public interface DewormingRepositoryPort {
    Optional<Deworming> findById(DewormingId dewormingId);
    PageResponse<Deworming> findByPetId(PetId petId, PageRequest pageRequest);
    void save(Deworming deworming);
}
