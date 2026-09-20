package com.petmanagement.health.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "dewormings", schema = "health_schema")
public class DewormingJpaEntity {

    @Id
    private UUID id;

    @Column(name = "pet_id", nullable = false)
    private UUID petId;

    @Column(name = "deworming_date", nullable = false)
    private LocalDate dewormingDate;

    @Column(name = "drug_name", nullable = false, length = 100)
    private String drugName;

    @Column(name = "drug_dose", nullable = false, length = 100)
    private String drugDose;

    @Column(name = "next_due_date", nullable = false)
    private LocalDate nextDueDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    // === Contructors ===

    protected DewormingJpaEntity() {
        // required by JPA
    }

    public DewormingJpaEntity(
            UUID id,
            UUID petId,
            LocalDate dewormingDate,
            String drugName,
            String drugDose,
            LocalDate nextDueDate,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.petId = petId;
        this.dewormingDate = dewormingDate;
        this.drugName = drugName;
        this.drugDose = drugDose;
        this.nextDueDate = nextDueDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public LocalDate getDewormingDate() {
        return dewormingDate;
    }

    public void setDewormingDate(LocalDate dewormingDate) {
        this.dewormingDate = dewormingDate;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDrugDose() {
        return drugDose;
    }

    public void setDrugDose(String drugDose) {
        this.drugDose = drugDose;
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

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    // === Object Methods ===

    @Override
    public String toString() {
        return "DewormingJpaEntity{" +
                "id=" + id +
                ", petId=" + petId +
                ", dewormingDate=" + dewormingDate +
                ", drugName='" + drugName + '\'' +
                ", drugDose='" + drugDose + '\'' +
                ", nextDueDate=" + nextDueDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

}
