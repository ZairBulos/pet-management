package com.petmanagement.health.support;

import com.petmanagement.health.infrastructure.adapter.out.persistence.entity.VaccineJpaEntity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public final class VaccineJpaEntityTestBuilder {

    private UUID id;
    private UUID petId;
    private LocalDate vaccinationDate;
    private String vaccineName;
    private LocalDate nextDueDate;
    private Instant createdAt;
    private Instant updatedAt;

    private VaccineJpaEntityTestBuilder() {
        this.id = TestVaccineMother.DEFAULT_VACCINE_ID.value();
        this.petId = TestPetIdMother.EXISTING_PET_ID.value();
        this.vaccinationDate = TestVaccineMother.VACCINE_VACCINATION_DATE;
        this.vaccineName = TestVaccineMother.VACCINE_NAME_RABIES.value();
        this.nextDueDate = TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR.value();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public static VaccineJpaEntityTestBuilder aVaccineJpaEntity() {
        return new VaccineJpaEntityTestBuilder();
    }

    public VaccineJpaEntityTestBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public VaccineJpaEntityTestBuilder withPetId(UUID petId) {
        this.petId = petId;
        return this;
    }

    public VaccineJpaEntityTestBuilder withVaccinationDate(LocalDate vaccinationDate) {
        this.vaccinationDate = vaccinationDate;
        return this;
    }

    public VaccineJpaEntityTestBuilder withVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
        return this;
    }

    public VaccineJpaEntityTestBuilder withNextDueDate(LocalDate nextDueDate) {
        this.nextDueDate = nextDueDate;
        return this;
    }

    public VaccineJpaEntityTestBuilder withCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public VaccineJpaEntityTestBuilder withUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public VaccineJpaEntity build() {
        return new VaccineJpaEntity(
                id,
                petId,
                vaccinationDate,
                vaccineName,
                nextDueDate,
                createdAt,
                updatedAt
        );
    }

}
