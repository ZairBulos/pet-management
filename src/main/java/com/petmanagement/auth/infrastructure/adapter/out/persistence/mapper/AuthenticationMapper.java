package com.petmanagement.auth.infrastructure.adapter.out.persistence.mapper;

import com.petmanagement.auth.domain.model.aggregate.Authentication;
import com.petmanagement.auth.domain.model.valueobject.AuthenticationExpiresAt;
import com.petmanagement.auth.domain.model.valueobject.AuthenticationHashedCode;
import com.petmanagement.auth.domain.model.valueobject.AuthenticationId;
import com.petmanagement.auth.domain.model.valueobject.Email;
import com.petmanagement.auth.infrastructure.adapter.out.persistence.entity.AuthenticationJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationMapper {

    public AuthenticationJpaEntity toJpaEntity(Authentication authentication) {
        return new AuthenticationJpaEntity(
                authentication.getId().value(),
                authentication.getEmail().value(),
                authentication.getHashedCode().value(),
                authentication.getExpiresAt().value(),
                authentication.getAuthenticatedAt(),
                authentication.getCreatedAt()
        );
    }

    public Authentication toDomain(AuthenticationJpaEntity entity) {
        return Authentication.reconstitute(
                AuthenticationId.of(entity.getId()),
                new Email(entity.getEmail()),
                new AuthenticationHashedCode(entity.getHashedCode()),
                new AuthenticationExpiresAt(entity.getExpiresAt()),
                entity.getAuthenticatedAt(),
                entity.getCreatedAt()
        );
    }

}
