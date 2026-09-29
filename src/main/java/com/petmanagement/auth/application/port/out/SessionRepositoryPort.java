package com.petmanagement.auth.application.port.out;

import com.petmanagement.auth.domain.model.aggregate.Session;

public interface SessionRepositoryPort {
    void save(Session session);
}
