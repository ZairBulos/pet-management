package com.petmanagement.health.support;

import com.petmanagement.health.infrastructure.adapter.out.persistence.entity.WeightRecordJpaEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public final class WeightRecordJpaEntityTestBuilder {

    private UUID id;
    private UUID petId;
    private LocalDate weightDate;
    private BigDecimal weight;
    private Instant createdAt;
    private Instant updatedAt;

    private WeightRecordJpaEntityTestBuilder() {
        this.id = TestWeightRecordMother.DEFAULT_WEIGHT_RECORD_ID.value();
        this.petId = TestPetIdMother.EXISTING_PET_ID.value();
        this.weightDate = TestCommonMother.EARLY_DATE;
        this.weight = TestWeightRecordMother.MEDIUM_WEIGHT.value();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public static WeightRecordJpaEntityTestBuilder aWeightRecordJpaEntity() {
        return new WeightRecordJpaEntityTestBuilder();
    }

    public WeightRecordJpaEntityTestBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public WeightRecordJpaEntityTestBuilder withPetId(UUID petId) {
        this.petId = petId;
        return this;
    }

    public WeightRecordJpaEntityTestBuilder withWeightDate(LocalDate weightDate) {
        this.weightDate = weightDate;
        return this;
    }

    public WeightRecordJpaEntityTestBuilder withWeight(BigDecimal weight) {
        this.weight = weight;
        return this;
    }

    public WeightRecordJpaEntityTestBuilder withCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public WeightRecordJpaEntityTestBuilder withUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public WeightRecordJpaEntity build() {
        return new WeightRecordJpaEntity(
                id,
                petId,
                weightDate,
                weight,
                createdAt,
                updatedAt
        );
    }

}
