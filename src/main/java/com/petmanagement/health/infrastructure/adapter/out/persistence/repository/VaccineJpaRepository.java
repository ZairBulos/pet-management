package com.petmanagement.health.infrastructure.adapter.out.persistence.repository;

import com.petmanagement.health.infrastructure.adapter.out.persistence.entity.VaccineJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface VaccineJpaRepository extends JpaRepository<VaccineJpaEntity, UUID> {

    @Query("SELECT v FROM VaccineJpaEntity v WHERE v.petId = :petId ORDER BY v.vaccinationDate DESC")
    Page<VaccineJpaEntity> findByPetId(@Param("petId") UUID petId, Pageable pageable);

}
