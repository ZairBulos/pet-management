package com.petmanagement.health.support;

import com.petmanagement.health.application.port.out.DewormingRepositoryPort;
import com.petmanagement.health.domain.model.aggregate.Deworming;
import com.petmanagement.health.domain.model.valueobject.DewormingId;

import java.util.HashMap;
import java.util.Map;

public class InMemoryDewormingRepository implements DewormingRepositoryPort {

    private final Map<DewormingId, Deworming> dewormings = new HashMap<>();

    @Override
    public void save(Deworming deworming) {
        dewormings.put(deworming.getId(), deworming);
    }

    public void clear() {
        dewormings.clear();
    }

}
