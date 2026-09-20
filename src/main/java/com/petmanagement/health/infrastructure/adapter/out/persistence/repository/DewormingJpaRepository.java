package com.petmanagement.health.infrastructure.adapter.out.persistence.repository;

import com.petmanagement.health.infrastructure.adapter.out.persistence.entity.DewormingJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface DewormingJpaRepository extends JpaRepository<DewormingJpaEntity, UUID> {

    @Query("SELECT d FROM DewormingJpaEntity d WHERE d.petId = :petId ORDER BY d.dewormingDate DESC")
    Page<DewormingJpaEntity> findByPetId(@Param("petId") UUID petId, Pageable pageab);

}
