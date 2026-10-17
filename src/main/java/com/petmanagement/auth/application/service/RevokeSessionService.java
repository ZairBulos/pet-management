package com.petmanagement.auth.application.service;

import com.petmanagement.auth.application.port.in.RevokeSessionUseCase;
import com.petmanagement.auth.application.port.out.SessionRepositoryPort;
import com.petmanagement.auth.domain.exception.SessionNotFoundException;
import com.petmanagement.auth.domain.model.enums.SessionRevocationReason;
import com.petmanagement.auth.domain.model.valueobject.SessionHashedRefreshToken;
import com.petmanagement.shared.application.port.out.EventPublisherPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class RevokeSessionService implements RevokeSessionUseCase {

    private final SessionRepositoryPort repository;
    private final EventPublisherPort publisher;

    RevokeSessionService(SessionRepositoryPort repository, EventPublisherPort publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    @Override
    @Transactional
    public void execute(RevokeSessionCommand command) {
        var hashedRefreshToken = SessionHashedRefreshToken.from(command.refreshToken());

        var session = repository.findByHashedRefreshToken(hashedRefreshToken)
                .orElseThrow(SessionNotFoundException::new);

        session.revoke(SessionRevocationReason.LOGOUT);

        repository.save(session);

        publisher.publishAll(session.pullEvents());
    }

}
