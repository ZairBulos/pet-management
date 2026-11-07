package com.petmanagement.reminders.infrastructure.adapter.out.persistence.repository;

import com.petmanagement.reminders.infrastructure.adapter.out.persistence.entity.ReminderJpaEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReminderJpaRepository extends JpaRepository<ReminderJpaEntity, UUID> {

    @EntityGraph(attributePaths = "notifications")
    Optional<ReminderJpaEntity> findBySourceId(UUID sourceId);

}
