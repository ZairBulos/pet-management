package com.petmanagement.auth.infrastructure.adapter.out.persistence;

import com.petmanagement.auth.application.port.out.SessionRepositoryPort;
import com.petmanagement.auth.domain.model.aggregate.Session;
import com.petmanagement.auth.infrastructure.adapter.out.persistence.mapper.SessionMapper;
import com.petmanagement.auth.infrastructure.adapter.out.persistence.repository.SessionJpaRepository;
import org.springframework.stereotype.Component;

@Component
class SessionPersistenceAdapter implements SessionRepositoryPort {

    private final SessionJpaRepository repository;
    private final SessionMapper mapper;

    public SessionPersistenceAdapter(SessionJpaRepository repository, SessionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void save(Session session) {
        repository.save(mapper.toJpaEntity(session));
    }

}
