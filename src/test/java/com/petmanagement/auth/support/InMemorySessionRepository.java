package com.petmanagement.auth.support;

import com.petmanagement.auth.application.port.out.SessionRepositoryPort;
import com.petmanagement.auth.domain.model.aggregate.Session;
import com.petmanagement.auth.domain.model.valueobject.OwnerId;
import com.petmanagement.auth.domain.model.valueobject.SessionHashedRefreshToken;
import com.petmanagement.auth.domain.model.valueobject.SessionId;

import java.util.*;

public final class InMemorySessionRepository implements SessionRepositoryPort {

    private final Map<SessionId, Session> sessions = new HashMap<>();

    @Override
    public Optional<Session> findByHashedRefreshToken(SessionHashedRefreshToken hashedRefreshToken) {
        return sessions.values().stream()
                .filter(session -> session.getHashedRefreshToken().equals(hashedRefreshToken))
                .findFirst();
    }

    @Override
    public void save(Session session) {
        sessions.put(session.getId(), session);
    }

    // === Helpers ===

    public void clear() {
        sessions.clear();
    }

    public int size() {
        return sessions.size();
    }

    public List<Session> findAllByOwnerId(OwnerId ownerId) {
        return sessions.values().stream()
                .filter(session -> session.getOwnerId().equals(ownerId))
                .sorted(Comparator.comparing(Session::getCreatedAt))
                .toList();
    }

    public void saveAll(Session... sessions) {
        for (Session session : sessions) {
            save(session);
        }
    }

}
