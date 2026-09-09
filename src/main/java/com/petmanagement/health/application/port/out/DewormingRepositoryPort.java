package com.petmanagement.health.application.port.out;

import com.petmanagement.health.domain.model.aggregate.Deworming;
import com.petmanagement.health.domain.model.valueobject.DewormingId;

import java.util.Optional;

public interface DewormingRepositoryPort {
    Optional<Deworming> findById(DewormingId dewormingId);
    void save(Deworming deworming);
}
