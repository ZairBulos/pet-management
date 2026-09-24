package com.petmanagement.auth.application.service;

import com.petmanagement.auth.application.port.in.RequestAuthenticationUseCase;
import com.petmanagement.auth.application.port.out.AuthenticationRepositoryPort;
import com.petmanagement.auth.domain.exception.OwnerNotFoundException;
import com.petmanagement.auth.domain.model.aggregate.Authentication;
import com.petmanagement.owners.api.OwnerApi;
import com.petmanagement.shared.application.port.out.EventPublisherPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.UnaryOperator;

@Service
class RequestAuthenticationService implements RequestAuthenticationUseCase {

    private final AuthenticationRepositoryPort repository;
    private final EventPublisherPort publisher;
    private final UnaryOperator<String> hasher;
    private final OwnerApi ownerApi;

    public RequestAuthenticationService(
            AuthenticationRepositoryPort repository,
            EventPublisherPort publisher,
            UnaryOperator<String> hasher,
            OwnerApi ownerApi
    ) {
        this.repository = repository;
        this.publisher = publisher;
        this.hasher = hasher;
        this.ownerApi = ownerApi;
    }

    @Override
    @Transactional
    public void execute(RequestAuthenticationCommand command) {
        var ownerExists = ownerApi.existsByEmail(command.email().value());

        if (!ownerExists)
            throw new OwnerNotFoundException();

        var authentication = Authentication.create(
                command.email(),
                hasher
        );

        repository.replaceActiveAuthentication(authentication);

        publisher.publishAll(authentication.pullEvents());
    }

}
