package com.petmanagement.auth.infrastructure.adapter.out.persistence.repository;

import com.petmanagement.auth.infrastructure.adapter.out.persistence.entity.SessionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionJpaRepository extends JpaRepository<SessionJpaEntity, UUID> {

    Optional<SessionJpaEntity> findByHashedRefreshToken(String hashedRefreshToken);

    @Query("""
            SELECT s FROM SessionJpaEntity s
            WHERE
                s.ownerId = :ownerId
                AND s.revokedAt IS NULL
                AND s.expiresAt >= :now
            """)
    List<SessionJpaEntity> findActiveByOwnerId(
            @Param("ownerId") UUID ownerId,
            @Param("now") Instant now
    );

}
