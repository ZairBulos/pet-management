package com.petmanagement.health.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "weight_records", schema = "health_schema")
public class WeightRecordJpaEntity {

    @Id
    private UUID id;

    @Column(name = "pet_id", nullable = false)
    private UUID petId;

    @Column(name = "weight_date", nullable = false)
    private LocalDate weightDate;

    @Column(name = "weight", nullable = false, precision = 10, scale = 2)
    private BigDecimal weight;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    // === Constructors ===

    protected WeightRecordJpaEntity() {
        // required by JPA
    }

    public WeightRecordJpaEntity(
            UUID id,
            UUID petId,
            LocalDate weightDate,
            BigDecimal weight,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.petId = petId;
        this.weightDate = weightDate;
        this.weight = weight;
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

    public LocalDate getWeightDate() {
        return weightDate;
    }

    public void setWeightDate(LocalDate weightDate) {
        this.weightDate = weightDate;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
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
        return "WeightRecordEntity{" +
                "id=" + id +
                ", petId=" + petId +
                ", weightDate=" + weightDate +
                ", weight=" + weight +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

}
