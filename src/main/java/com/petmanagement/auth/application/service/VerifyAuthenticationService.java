package com.petmanagement.auth.application.service;

import com.petmanagement.auth.application.port.in.VerifyAuthenticationUseCase;
import com.petmanagement.auth.application.port.out.AuthenticationRepositoryPort;
import com.petmanagement.auth.application.port.out.SessionRepositoryPort;
import com.petmanagement.auth.application.port.out.TokenProviderPort;
import com.petmanagement.auth.domain.exception.ActiveAuthenticationNotFoundException;
import com.petmanagement.auth.domain.exception.OwnerNotFoundException;
import com.petmanagement.auth.domain.model.aggregate.Session;
import com.petmanagement.auth.domain.model.valueobject.OwnerId;
import com.petmanagement.auth.domain.model.valueobject.SessionRefreshToken;
import com.petmanagement.owners.api.OwnerApi;
import com.petmanagement.shared.application.port.out.EventPublisherPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.BiPredicate;

@Service
class VerifyAuthenticationService implements VerifyAuthenticationUseCase {

    private final AuthenticationRepositoryPort authenticationRepository;
    private final SessionRepositoryPort sessionRepository;
    private final TokenProviderPort tokenProvider;
    private final BiPredicate<String, String> verifier;
    private final EventPublisherPort publisher;
    private final OwnerApi ownerApi;

    VerifyAuthenticationService(
            AuthenticationRepositoryPort authenticationRepository,
            SessionRepositoryPort sessionRepository,
            TokenProviderPort tokenProvider,
            BiPredicate<String, String> verifier,
            EventPublisherPort publisher,
            OwnerApi ownerApi
    ) {
        this.authenticationRepository = authenticationRepository;
        this.sessionRepository = sessionRepository;
        this.tokenProvider = tokenProvider;
        this.verifier = verifier;
        this.publisher = publisher;
        this.ownerApi = ownerApi;
    }

    @Override
    @Transactional
    public VerifyAuthenticationResult execute(VerifyAuthenticationCommand command) {
        var ownerId = ownerApi.getOwnerIdByEmail(command.email().value())
                .map(OwnerId::of)
                .orElseThrow(OwnerNotFoundException::new);

        var authentication = authenticationRepository.findActiveByEmail(command.email())
                .orElseThrow(ActiveAuthenticationNotFoundException::new);

        authentication.authenticate(command.code(), verifier);
        authenticationRepository.save(authentication);

        var tokens = tokenProvider.generate(ownerId);

        var session = Session.create(
                ownerId,
                new SessionRefreshToken(tokens.refreshToken())
        );
        sessionRepository.save(session);

        publisher.publishAll(session.pullEvents());

        return new VerifyAuthenticationResult(
                tokens.accessToken(),
                tokens.refreshToken()
        );
    }

}
