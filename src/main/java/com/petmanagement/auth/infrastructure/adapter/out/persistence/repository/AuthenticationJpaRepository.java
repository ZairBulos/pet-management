package com.petmanagement.auth.infrastructure.adapter.out.persistence.repository;

import com.petmanagement.auth.infrastructure.adapter.out.persistence.entity.AuthenticationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface AuthenticationJpaRepository extends JpaRepository<AuthenticationJpaEntity, UUID> {

    @Query("""
            SELECT a FROM AuthenticationJpaEntity a
            WHERE
                a.email = :email
                AND a.expiresAt >= :now
                AND a.authenticatedAt IS NULL
            ORDER BY a.createdAt DESC
            LIMIT 1
            """)
    Optional<AuthenticationJpaEntity> findActiveByEmail(
            @Param("email") String email,
            @Param("now") Instant now
    );

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            UPDATE AuthenticationJpaEntity a
            SET a.authenticatedAt = :now
            WHERE
                a.email = :email
                AND a.authenticatedAt IS NULL
            """)
    void disableActiveAuthentication(
            @Param("email") String email,
            @Param("now") Instant now
    );

}
