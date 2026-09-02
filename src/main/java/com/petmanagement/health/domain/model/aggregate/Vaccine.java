package com.petmanagement.health.domain.model.aggregate;

import com.petmanagement.health.domain.model.valueobject.NextDueDate;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.health.domain.model.valueobject.VaccineId;
import com.petmanagement.health.domain.model.valueobject.VaccineName;
import org.jmolecules.ddd.annotation.AggregateRoot;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

@AggregateRoot
public final class Vaccine {

    private final VaccineId id;
    private final PetId petId;
    private LocalDate vaccinationDate;
    private VaccineName vaccineName;
    private NextDueDate nextDueDate;
    private final Instant createdAt;
    private Instant updatedAt;

    private Vaccine(
            VaccineId id,
            PetId petId,
            LocalDate vaccinationDate,
            VaccineName vaccineName,
            NextDueDate nextDueDate,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = Objects.requireNonNull(id, "Vaccine id cannot be null");
        this.petId = Objects.requireNonNull(petId, "Pet id cannot be null");
        this.vaccinationDate = Objects.requireNonNull(vaccinationDate, "Vaccination date cannot be null");
        this.vaccineName = Objects.requireNonNull(vaccineName, "Vaccine name cannot be null");
        this.nextDueDate = Objects.requireNonNull(nextDueDate, "Next due date cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "Updated at cannot be null");
    }

    // === Factory ===
    public static Vaccine create(
            PetId petId,
            LocalDate vaccinationDate,
            VaccineName vaccineName,
            NextDueDate nextDueDate
    ) {
        var now = Instant.now();

        return new Vaccine(
                VaccineId.generate(),
                petId,
                vaccinationDate,
                vaccineName,
                nextDueDate,
                now,
                now
        );
    }

    public static Vaccine reconstitute(
            VaccineId id,
            PetId petId,
            LocalDate vaccinationDate,
            VaccineName vaccineName,
            NextDueDate nextDueDate,
            Instant createdAt,
            Instant updatedAt
    ) {
        return new Vaccine(id, petId, vaccinationDate, vaccineName, nextDueDate, createdAt, updatedAt);
    }

    // === Business Operations ===
    public void update(LocalDate newVaccinationDate, VaccineName newVaccineName) {
        this.vaccinationDate = Objects.requireNonNull(newVaccinationDate, "Vaccination date cannot be null");
        this.vaccineName = Objects.requireNonNull(newVaccineName, "Vaccine name cannot be null");
        touch();
    }

    public void reschedule(LocalDate newNextDueDate) {
        this.nextDueDate = NextDueDate.after(vaccinationDate, newNextDueDate);
        touch();
    }

    public long daysRemaining(LocalDate today) {
        return nextDueDate.daysRemaining(today);
    }

    public boolean isOverdue(LocalDate today) {
        return nextDueDate.isOverdue(today);
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }

    // === Getters ===
    public VaccineId getId() {
        return id;
    }

    public PetId getPetId() {
        return petId;
    }

    public LocalDate getVaccinationDate() {
        return vaccinationDate;
    }

    public VaccineName getVaccineName() {
        return vaccineName;
    }

    public NextDueDate getNextDueDate() {
        return nextDueDate;
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
        if (!(o instanceof Vaccine other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Vaccine{" +
                "id=" + id +
                ", petId=" + petId +
                ", vaccinationDate=" + vaccinationDate +
                ", vaccineName=" + vaccineName +
                ", nextDueDate=" + nextDueDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

}
