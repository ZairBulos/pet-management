package com.petmanagement.health.application.port.out;

import com.petmanagement.health.domain.model.aggregate.Vaccine;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.health.domain.model.valueobject.VaccineId;
import com.petmanagement.shared.domain.model.PageRequest;
import com.petmanagement.shared.domain.model.PageResponse;

import java.util.Optional;

public interface VaccineRepositoryPort {
    Optional<Vaccine> findById(VaccineId vaccineId);
    PageResponse<Vaccine> findByPetId(PetId petId, PageRequest pageRequest);
    void save(Vaccine vaccine);
}
