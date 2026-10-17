package com.petmanagement.health.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "vaccines", schema = "health_schema")
public class VaccineJpaEntity {

    @Id
    private UUID id;

    @Column(name = "pet_id", nullable = false)
    private UUID petId;

    @Column(name = "vaccination_date", nullable = false)
    private LocalDate vaccinationDate;

    @Column(name = "vaccine_name", nullable = false, length = 100)
    private String vaccineName;

    @Column(name = "next_due_date", nullable = false)
    private LocalDate nextDueDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // === Constructors ===

    protected VaccineJpaEntity() {
        // requeried by JPA
    }

    public VaccineJpaEntity(
            UUID id,
            UUID petId,
            LocalDate vaccinationDate,
            String vaccineName,
            LocalDate nextDueDate,
            Instant createdAt
    ) {
        this.id = id;
        this.petId = petId;
        this.vaccinationDate = vaccinationDate;
        this.vaccineName = vaccineName;
        this.nextDueDate = nextDueDate;
        this.createdAt = createdAt;
    }

    // === Getters & Setters ===

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPetId() {
        return petId;
    }

    public void setPetId(UUID petId) {
        this.petId = petId;
    }

    public LocalDate getVaccinationDate() {
        return vaccinationDate;
    }

    public void setVaccinationDate(LocalDate vaccinationDate) {
        this.vaccinationDate = vaccinationDate;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public LocalDate getNextDueDate() {
        return nextDueDate;
    }

    public void setNextDueDate(LocalDate nextDueDate) {
        this.nextDueDate = nextDueDate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    // === Object Methods ===

    @Override
    public String toString() {
        return "VaccineJpaEntity{" +
                "id=" + id +
                ", petId=" + petId +
                ", vaccinationDate=" + vaccinationDate +
                ", vaccineName='" + vaccineName + '\'' +
                ", nextDueDate=" + nextDueDate +
                ", createdAt=" + createdAt +
                '}';
    }

}
