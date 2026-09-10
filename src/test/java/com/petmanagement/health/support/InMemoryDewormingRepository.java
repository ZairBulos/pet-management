package com.petmanagement.health.support;

import com.petmanagement.health.application.port.out.DewormingRepositoryPort;
import com.petmanagement.health.domain.model.aggregate.Deworming;
import com.petmanagement.health.domain.model.valueobject.DewormingId;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.shared.domain.model.PageRequest;
import com.petmanagement.shared.domain.model.PageResponse;

import java.util.*;

public class InMemoryDewormingRepository implements DewormingRepositoryPort {

    private final Map<DewormingId, Deworming> dewormings = new HashMap<>();

    @Override
    public Optional<Deworming> findById(DewormingId dewormingId) {
        return dewormings.values().stream()
                .filter(deworming -> deworming.getId().equals(dewormingId))
                .findFirst();
    }

    @Override
    public PageResponse<Deworming> findByPetId(PetId petId, PageRequest pageRequest) {
        var allRecords = dewormings.values().stream()
                .filter(deworming -> deworming.getPetId().equals(petId))
                .sorted(Comparator.comparing(Deworming::getDewormingDate).reversed())
                .toList();

        var start = pageRequest.page() * pageRequest.size();
        var end = Math.min(start + pageRequest.size(), allRecords.size());

        var pageContent = start >= allRecords.size()
                ? List.<Deworming>of()
                : allRecords.subList(start, end);

        return PageResponse.of(
                pageContent,
                pageRequest.page(),
                pageRequest.size(),
                allRecords.size()
        );
    }

    @Override
    public void save(Deworming deworming) {
        dewormings.put(deworming.getId(), deworming);
    }

    public void clear() {
        dewormings.clear();
    }

}
