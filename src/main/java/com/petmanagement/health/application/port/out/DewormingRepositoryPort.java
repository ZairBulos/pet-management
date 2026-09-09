package com.petmanagement.health.application.port.out;

import com.petmanagement.health.domain.model.aggregate.Deworming;

public interface DewormingRepositoryPort {
    void save(Deworming deworming);
}
