package com.petmanagement.health.domain.model.aggregate;

import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.health.domain.model.valueobject.Weight;
import com.petmanagement.health.domain.model.valueobject.WeightRecordId;
import org.jmolecules.ddd.annotation.AggregateRoot;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

@AggregateRoot
public record WeightRecord(
        WeightRecordId id,
        PetId petId,
        LocalDate weightDate,
        Weight weight,
        Instant createdAt
) {

    public WeightRecord {
        Objects.requireNonNull(id, "Weight record id cannot be null");
        Objects.requireNonNull(petId, "Pet id cannot be null");
        Objects.requireNonNull(weightDate, "Weight date cannot be null");
        Objects.requireNonNull(weight, "Weight cannot be null");
        Objects.requireNonNull(createdAt, "Created at cannot be null");
    }

    // === Factory ===

    public static WeightRecord create(
            PetId petId,
            LocalDate weightDate,
            Weight weight
    ) {
        return new WeightRecord(
                WeightRecordId.generate(),
                petId,
                weightDate,
                weight,
                Instant.now()
        );
    }

    public static WeightRecord reconstitute(
            WeightRecordId id,
            PetId petId,
            LocalDate weightDate,
            Weight weight,
            Instant createdAt
    ) {
        return new WeightRecord(id, petId, weightDate, weight, createdAt);
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

}
