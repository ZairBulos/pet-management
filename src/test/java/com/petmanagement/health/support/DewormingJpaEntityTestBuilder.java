package com.petmanagement.health.support;

import com.petmanagement.health.infrastructure.adapter.out.persistence.entity.DewormingJpaEntity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public final class DewormingJpaEntityTestBuilder {

    private UUID id;
    private UUID petId;
    private LocalDate dewormingDate;
    private String drugName;
    private String drugDose;
    private LocalDate nextDueDate;
    private Instant createdAt;
    private Instant updatedAt;

    private DewormingJpaEntityTestBuilder() {
        this.id = TestDewormingMother.DEFAULT_DEWORMING_ID.value();
        this.petId = TestPetIdMother.EXISTING_PET_ID.value();
        this.dewormingDate = TestDewormingMother.DEWORMING_DATE;
        this.drugName = TestDewormingMother.DRUG_NAME_DRONTAL.value();
        this.drugDose = TestDewormingMother.DRUG_DOSE_TABLET.value();
        this.nextDueDate = TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS.value();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public static DewormingJpaEntityTestBuilder aDewormingJpaEntity() {
        return new DewormingJpaEntityTestBuilder();
    }

    public DewormingJpaEntityTestBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public DewormingJpaEntityTestBuilder withPetId(UUID petId) {
        this.petId = petId;
        return this;
    }

    public DewormingJpaEntityTestBuilder withDewormingDate(LocalDate dewormingDate) {
        this.dewormingDate = dewormingDate;
        return this;
    }

    public DewormingJpaEntityTestBuilder withDrugName(String drugName) {
        this.drugName = drugName;
        return this;
    }

    public DewormingJpaEntityTestBuilder withDrugDose(String drugDose) {
        this.drugDose = drugDose;
        return this;
    }

    public DewormingJpaEntityTestBuilder withNextDueDate(LocalDate nextDueDate) {
        this.nextDueDate = nextDueDate;
        return this;
    }

    public DewormingJpaEntityTestBuilder withCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public DewormingJpaEntityTestBuilder withUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public DewormingJpaEntity build() {
        return new DewormingJpaEntity(
                id,
                petId,
                dewormingDate,
                drugName,
                drugDose,
                nextDueDate,
                createdAt,
                updatedAt
        );
    }

}
