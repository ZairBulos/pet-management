package com.petmanagement.auth.infrastructure.adapter.out.persistence.mapper;

import com.petmanagement.auth.domain.model.aggregate.Session;
import com.petmanagement.auth.domain.model.enums.SessionRevocationReason;
import com.petmanagement.auth.domain.model.valueobject.OwnerId;
import com.petmanagement.auth.domain.model.valueobject.SessionExpiresAt;
import com.petmanagement.auth.domain.model.valueobject.SessionHashedRefreshToken;
import com.petmanagement.auth.domain.model.valueobject.SessionId;
import com.petmanagement.auth.infrastructure.adapter.out.persistence.entity.SessionJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class SessionMapper {

    public SessionJpaEntity toJpaEntity(Session session) {
        return new SessionJpaEntity(
                session.getId().value(),
                session.getOwnerId().value(),
                session.getHashedRefreshToken().value(),
                session.getExpiresAt().value(),
                session.getRevokedAt(),
                session.getRevocationReason() != null
                        ? session.getRevocationReason().name()
                        : null,
                session.getCreatedAt()
        );
    }

    public Session toDomain(SessionJpaEntity entity) {
        return Session.reconstitute(
                SessionId.of(entity.getId()),
                OwnerId.of(entity.getOwnerId()),
                new SessionHashedRefreshToken(entity.getHashedRefreshToken()),
                new SessionExpiresAt(entity.getExpiresAt()),
                entity.getRevokedAt(),
                entity.getRevocationReason() != null
                        ? SessionRevocationReason.valueOf(entity.getRevocationReason())
                        : null,
                entity.getCreatedAt()
        );
    }

}
