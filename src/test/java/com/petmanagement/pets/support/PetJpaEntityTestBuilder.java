package com.petmanagement.pets.support;

import com.petmanagement.pets.infrastructure.adapter.out.persistence.entity.PetJpaEntity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public final class PetJpaEntityTestBuilder {

    private UUID id;
    private UUID ownerId;
    private String name;
    private String species;
    private String breed;
    private String coat;
    private String sex;
    private LocalDate birthDate;
    private Instant createdAt;
    private Instant updatedAt;

    private PetJpaEntityTestBuilder() {
        this.id = TestPetMother.DEFAULT_PET_ID.value();
        this.ownerId = TestOwnerIdMother.EXISTING_OWNER_ID.value();
        this.name = TestPetMother.PET_NAME_BUDDY.value();
        this.species = TestPetMother.SPECIES_DOG.value();
        this.breed = TestPetMother.BREED_GOLDEN_RETRIEVER.value();
        this.coat = TestPetMother.COAT_GOLDEN.value();
        this.sex = TestPetMother.SEX_MALE.name();
        this.birthDate = TestPetMother.BIRTH_DATE_STANDARD;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public static PetJpaEntityTestBuilder aPetJpaEntity() {
        return new PetJpaEntityTestBuilder();
    }

    public PetJpaEntityTestBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public PetJpaEntityTestBuilder withOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public PetJpaEntityTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public PetJpaEntityTestBuilder withSpecies(String species) {
        this.species = species;
        return this;
    }

    public PetJpaEntityTestBuilder withBreed(String breed) {
        this.breed = breed;
        return this;
    }

    public PetJpaEntityTestBuilder withCoat(String coat) {
        this.coat = coat;
        return this;
    }

    public PetJpaEntityTestBuilder withSex(String sex) {
        this.sex = sex;
        return this;
    }

    public PetJpaEntityTestBuilder withBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public PetJpaEntityTestBuilder withCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public PetJpaEntityTestBuilder withUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public PetJpaEntity build() {
        return new PetJpaEntity(
                id,
                ownerId,
                name,
                species,
                breed,
                coat,
                sex,
                birthDate,
                createdAt,
                updatedAt
        );
    }

}
