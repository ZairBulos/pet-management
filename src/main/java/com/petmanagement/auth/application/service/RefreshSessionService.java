package com.petmanagement.auth.application.service;

import com.petmanagement.auth.application.port.in.RefreshSessionUseCase;
import com.petmanagement.auth.application.port.out.SessionRepositoryPort;
import com.petmanagement.auth.application.port.out.TokenProviderPort;
import com.petmanagement.auth.domain.exception.SessionNotFoundException;
import com.petmanagement.auth.domain.exception.SessionReuseDetectedException;
import com.petmanagement.auth.domain.model.aggregate.Session;
import com.petmanagement.auth.domain.model.enums.SessionRevocationReason;
import com.petmanagement.auth.domain.model.valueobject.OwnerId;
import com.petmanagement.auth.domain.model.valueobject.SessionHashedRefreshToken;
import com.petmanagement.auth.domain.model.valueobject.SessionRefreshToken;
import com.petmanagement.shared.application.port.out.EventPublisherPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class RefreshSessionService implements RefreshSessionUseCase {

    private final SessionRepositoryPort repository;
    private final TokenProviderPort tokenProvider;
    private final EventPublisherPort publisher;

    RefreshSessionService(
            SessionRepositoryPort repository,
            TokenProviderPort tokenProvider,
            EventPublisherPort publisher
    ) {
        this.repository = repository;
        this.tokenProvider = tokenProvider;
        this.publisher = publisher;
    }

    @Override
    @Transactional(noRollbackFor = SessionReuseDetectedException.class)
    public RefreshSessionResult execute(RefreshSessionCommand command) {
        var hashedRefreshToken = SessionHashedRefreshToken.from(command.refreshToken());

        var session = repository.findByHashedRefreshToken(hashedRefreshToken)
                .orElseThrow(SessionNotFoundException::new);

        var tokens = tokenProvider.generate(session.getOwnerId());

        Session newSession;
        try {
            newSession = session.rotate(new SessionRefreshToken(tokens.refreshToken()));
        } catch (SessionReuseDetectedException e) {
            revokeActiveSessions(session.getOwnerId());
            throw e;
        }

        repository.save(session);
        repository.save(newSession);

        publisher.publishAll(session.pullEvents());
        publisher.publishAll(newSession.pullEvents());

        return new RefreshSessionResult(
                tokens.accessToken(),
                tokens.refreshToken()
        );
    }

    private void revokeActiveSessions(OwnerId ownerId) {
        repository.findActiveByOwnerId(ownerId).forEach(session -> {
            session.revoke(SessionRevocationReason.REUSE_DETECTED);
            repository.save(session);
            publisher.publishAll(session.pullEvents());
        });
    }

}
