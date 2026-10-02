package com.petmanagement.auth.application.port.out;

import com.petmanagement.auth.domain.model.aggregate.Session;
import com.petmanagement.auth.domain.model.valueobject.OwnerId;
import com.petmanagement.auth.domain.model.valueobject.SessionHashedRefreshToken;

import java.util.List;
import java.util.Optional;

public interface SessionRepositoryPort {
    Optional<Session> findByHashedRefreshToken(SessionHashedRefreshToken hashedRefreshToken);
    List<Session> findActiveByOwnerId(OwnerId ownerId);
    void save(Session session);
}
