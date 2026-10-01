package com.petmanagement.auth.infrastructure.adapter.out.persistence.repository;

import com.petmanagement.auth.infrastructure.adapter.out.persistence.entity.SessionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SessionJpaRepository extends JpaRepository<SessionJpaEntity, UUID> {
}
