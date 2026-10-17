package com.petmanagement.owners.infrastructure.adapter.out.persistence.repository;

import com.petmanagement.owners.infrastructure.adapter.out.persistence.entity.OwnerJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OwnerJpaRepository extends JpaRepository<OwnerJpaEntity, UUID> {
    Optional<OwnerJpaEntity> findByEmail(String email);
}
