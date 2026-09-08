package com.petmanagement.health.application.port.out;

import com.petmanagement.health.domain.model.aggregate.Vaccine;
import com.petmanagement.health.domain.model.valueobject.VaccineId;

import java.util.Optional;

public interface VaccineRepositoryPort {
    Optional<Vaccine> findById(VaccineId vaccineId);
    void save(Vaccine vaccine);
}
