package com.petmanagement.health.domain.model.aggregate;

import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.health.domain.model.valueobject.Weight;
import com.petmanagement.health.domain.model.valueobject.WeightRecordId;
import org.jmolecules.ddd.annotation.AggregateRoot;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

@AggregateRoot
public final class WeightRecord {

    private final WeightRecordId id;
    private final PetId petId;
    private LocalDate weightDate;
    private Weight weight;
    private final Instant createdAt;
    private Instant updatedAt;

    private WeightRecord(
            WeightRecordId id,
            PetId petId,
            LocalDate weightDate,
            Weight weight,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = Objects.requireNonNull(id, "Weight record id cannot be null");
        this.petId = Objects.requireNonNull(petId, "Pet id cannot be null");
        this.weightDate = Objects.requireNonNull(weightDate, "Weight date cannot be null");
        this.weight = Objects.requireNonNull(weight, "Weight cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "Updated at cannot be null");
    }

    // === Factory ===
    public static WeightRecord create(
            PetId petId,
            LocalDate weightDate,
            Weight weight
    ) {
        var now = Instant.now();

        return new WeightRecord(
                WeightRecordId.generate(),
                petId,
                weightDate,
                weight,
                now,
                now
        );
    }

    public static WeightRecord reconstitute(
            WeightRecordId id,
            PetId petId,
            LocalDate weightDate,
            Weight weight,
            Instant createdAt,
            Instant updatedAt
    ) {
        return new WeightRecord(id, petId, weightDate, weight, createdAt, updatedAt);
    }

    // === Business Operations ===
    public void update(LocalDate newWeightDate, Weight newWeight) {
        this.weightDate = Objects.requireNonNull(newWeightDate, "Weight date cannot be null");
        this.weight = Objects.requireNonNull(newWeight, "Weight cannot be null");
        touch();
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }

    // === Getters ===
    public WeightRecordId getId() {
        return id;
    }

    public PetId getPetId() {
        return petId;
    }

    public LocalDate getWeightDate() {
        return weightDate;
    }

    public Weight getWeight() {
        return weight;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    // === Object Methods ===
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WeightRecord other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "WeightRecord{" +
                "id=" + id +
                ", petId=" + petId +
                ", weightDate=" + weightDate +
                ", weight=" + weight +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

}
