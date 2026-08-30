package com.petmanagement.pets.domain.model.aggregate;

import com.petmanagement.pets.domain.model.enums.Sex;
import com.petmanagement.pets.domain.model.valueobject.*;
import org.jmolecules.ddd.annotation.AggregateRoot;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

@AggregateRoot
public final class Pet {

    private final PetId id;
    private final OwnerId ownerId;
    private PetName name;
    private final Species species;
    private final Breed breed;
    private final Coat coat;
    private final Sex sex;
    private final LocalDate birthDate;
    private final Instant createdAt;
    private Instant updatedAt;

    private Pet(
            PetId id,
            OwnerId ownerId,
            PetName name,
            Species species,
            Breed breed,
            Coat coat,
            Sex sex,
            LocalDate birthDate,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = Objects.requireNonNull(id, "Pet id must not be null");
        this.ownerId = Objects.requireNonNull(ownerId, "Owner id must not be null");
        this.name = Objects.requireNonNull(name, "Pet name must not be null");
        this.species = Objects.requireNonNull(species, "Species must not be null");
        this.breed = breed;
        this.coat = Objects.requireNonNull(coat, "Coat must not be null");
        this.sex = Objects.requireNonNull(sex, "Sex must not be null");
        this.birthDate = Objects.requireNonNull(birthDate, "Birth date must not be null");
        this.createdAt = Objects.requireNonNull(createdAt, "Creation timestamp must not be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "Update timestamp must not be null");
    }

    // === Factory ===
    public static Pet create(
            OwnerId ownerId,
            PetName name,
            Species species,
            Breed breed,
            Coat coat,
            Sex sex,
            LocalDate birthDate
    ) {
        var now = Instant.now();

        return new Pet(
                PetId.generate(),
                ownerId,
                name,
                species,
                breed == null ? Breed.unknown() : breed,
                coat,
                sex,
                birthDate,
                now,
                now
        );
    }

    public static Pet reconstitute(
            PetId id,
            OwnerId ownerId,
            PetName name,
            Species species,
            Breed breed,
            Coat coat,
            Sex sex,
            LocalDate birthDate,
            Instant createdAt,
            Instant updatedAt
    ) {
        return new Pet(id, ownerId, name, species, breed, coat, sex, birthDate, createdAt, updatedAt);
    }

    // === Business Operation ===
    public void rename(PetName newName) {
        this.name = Objects.requireNonNull(newName, "The pet's name cannot be null");
        touch();
    }

    public int ageInYears() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }

    // === Getters ===
    public PetId getId() {
        return id;
    }

    public OwnerId getOwnerId() {
        return ownerId;
    }

    public PetName getName() {
        return name;
    }

    public Species getSpecies() {
        return species;
    }

    public Breed getBreed() {
        return breed;
    }

    public Coat getCoat() {
        return coat;
    }

    public Sex getSex() {
        return sex;
    }

    public LocalDate getBirthDate() {
        return birthDate;
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
        if (!(o instanceof Pet other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", ownerId=" + ownerId +
                ", name=" + name +
                ", species=" + species +
                ", breed=" + breed +
                ", coat=" + coat +
                ", sex=" + sex +
                ", birthDate=" + birthDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

}
