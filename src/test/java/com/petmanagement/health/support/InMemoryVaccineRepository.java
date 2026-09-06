package com.petmanagement.health.support;

import com.petmanagement.health.application.port.out.VaccineRepositoryPort;
import com.petmanagement.health.domain.model.aggregate.Vaccine;

import java.util.ArrayList;
import java.util.List;

public class InMemoryVaccineRepository implements VaccineRepositoryPort {

    private final List<Vaccine> vaccines = new ArrayList<>();

    @Override
    public void save(Vaccine vaccine) {
        vaccines.add(vaccine);
    }

    public void clear() {
        vaccines.clear();
    }

}
