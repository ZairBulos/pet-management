package com.petmanagement.auth.infrastructure.adapter.out.persistence;

import com.petmanagement.auth.application.port.out.AuthenticationRepositoryPort;
import com.petmanagement.auth.domain.model.aggregate.Authentication;
import com.petmanagement.auth.domain.model.valueobject.Email;
import com.petmanagement.auth.infrastructure.adapter.out.persistence.mapper.AuthenticationMapper;
import com.petmanagement.auth.infrastructure.adapter.out.persistence.repository.AuthenticationJpaRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
class AuthenticationPersistenceAdapter implements AuthenticationRepositoryPort {

    private final AuthenticationJpaRepository repository;
    private final AuthenticationMapper mapper;

    public AuthenticationPersistenceAdapter(AuthenticationJpaRepository repository, AuthenticationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Authentication> findActiveByEmail(Email email) {
        return repository.findActiveByEmail(email.value(), Instant.now())
                .map(mapper::toDomain);
    }

    @Override
    public void save(Authentication authentication) {
        repository.save(mapper.toJpaEntity(authentication));
    }

    @Override
    public void replaceActiveAuthentication(Authentication authentication) {
        repository.disableActiveAuthentication(authentication.getEmail().value(), Instant.now());
        repository.save(mapper.toJpaEntity(authentication));
    }

}
