package com.petmanagement.health.application.port.out;

import com.petmanagement.health.domain.model.aggregate.Vaccine;

public interface VaccineRepositoryPort {
    void save(Vaccine vaccine);
}
