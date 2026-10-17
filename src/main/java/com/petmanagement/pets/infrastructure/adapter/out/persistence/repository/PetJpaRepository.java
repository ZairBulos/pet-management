package com.petmanagement.pets.infrastructure.adapter.out.persistence.repository;

import com.petmanagement.pets.infrastructure.adapter.out.persistence.entity.PetJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PetJpaRepository extends JpaRepository<PetJpaEntity, UUID> {
    List<PetJpaEntity> findByOwnerId(UUID ownerId);
}
