package com.petmanagement.health.support;

import com.petmanagement.health.application.port.out.VaccineRepositoryPort;
import com.petmanagement.health.domain.model.aggregate.Vaccine;
import com.petmanagement.health.domain.model.valueobject.VaccineId;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryVaccineRepository implements VaccineRepositoryPort {

    private final Map<VaccineId, Vaccine> vaccines = new HashMap<>();

    @Override
    public Optional<Vaccine> findById(VaccineId vaccineId) {
        return vaccines.values().stream()
                .filter(vaccine -> vaccine.getId().equals(vaccineId))
                .findFirst();
    }

    @Override
    public void save(Vaccine vaccine) {
        vaccines.put(vaccine.getId(), vaccine);
    }

    public void clear() {
        vaccines.clear();
    }

}
