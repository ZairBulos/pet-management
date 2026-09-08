package com.petmanagement.health.support;

import com.petmanagement.health.application.port.out.VaccineRepositoryPort;
import com.petmanagement.health.domain.model.aggregate.Vaccine;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.health.domain.model.valueobject.VaccineId;
import com.petmanagement.shared.domain.model.PageRequest;
import com.petmanagement.shared.domain.model.PageResponse;

import java.util.*;

public class InMemoryVaccineRepository implements VaccineRepositoryPort {

    private final Map<VaccineId, Vaccine> vaccines = new HashMap<>();

    @Override
    public Optional<Vaccine> findById(VaccineId vaccineId) {
        return vaccines.values().stream()
                .filter(vaccine -> vaccine.getId().equals(vaccineId))
                .findFirst();
    }

    @Override
    public PageResponse<Vaccine> findByPetId(PetId petId, PageRequest pageRequest) {
        var allRecords = vaccines.values().stream()
                .filter(vaccine -> vaccine.getPetId().equals(petId))
                .sorted(Comparator.comparing(Vaccine::getVaccinationDate).reversed())
                .toList();

        var start = pageRequest.page() * pageRequest.size();
        var end = Math.min(start + pageRequest.size(), allRecords.size());

        var pageContent = start >= allRecords.size()
                ? List.<Vaccine>of()
                : allRecords.subList(start, end);

        return PageResponse.of(
                pageContent,
                pageRequest.page(),
                pageRequest.size(),
                allRecords.size()
        );
    }

    @Override
    public void save(Vaccine vaccine) {
        vaccines.put(vaccine.getId(), vaccine);
    }

    public void clear() {
        vaccines.clear();
    }

}
