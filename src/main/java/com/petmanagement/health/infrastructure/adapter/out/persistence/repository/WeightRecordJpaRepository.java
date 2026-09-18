package com.petmanagement.health.infrastructure.adapter.out.persistence.repository;

import com.petmanagement.health.infrastructure.adapter.out.persistence.entity.WeightRecordJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface WeightRecordJpaRepository extends JpaRepository<WeightRecordJpaEntity, UUID> {

    @Query("SELECT wr FROM WeightRecordJpaEntity wr WHERE wr.petId = :petId ORDER BY wr.weightDate DESC")
    Page<WeightRecordJpaEntity> findByPetId(@Param("petId") UUID petId, Pageable pageable);

}
