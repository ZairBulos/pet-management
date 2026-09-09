package com.petmanagement.health.domain.model.aggregate;

import com.petmanagement.health.domain.event.DewormingCreated;
import com.petmanagement.health.domain.event.DewormingRescheduled;
import com.petmanagement.health.domain.event.DewormingUpdated;
import com.petmanagement.health.domain.model.valueobject.*;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.event.types.DomainEvent;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AggregateRoot
public final class Deworming {

    private final DewormingId id;
    private final PetId petId;
    private LocalDate dewormingDate;
    private DrugName drugName;
    private DrugDose drugDose;
    private NextDueDate nextDueDate;
    private final Instant createdAt;
    private Instant updatedAt;

    private final List<DomainEvent> events = new ArrayList<>();

    private Deworming(
            DewormingId id,
            PetId petId,
            LocalDate dewormingDate,
            DrugName drugName,
            DrugDose drugDose,
            NextDueDate nextDueDate,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = Objects.requireNonNull(id, "Deworming id cannot be null");
        this.petId = Objects.requireNonNull(petId, "Pet id cannot be null");
        this.dewormingDate = Objects.requireNonNull(dewormingDate, "Deworming date cannot be null");
        this.drugName = Objects.requireNonNull(drugName, "Drug name cannot be null");
        this.drugDose = Objects.requireNonNull(drugDose, "Drug dose cannot be null");
        this.nextDueDate = Objects.requireNonNull(nextDueDate, "Next due date cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "Updated at cannot be null");
    }

    // === Factory ===
    public static Deworming create(
            PetId petId,
            LocalDate dewormingDate,
            DrugName drugName,
            DrugDose drugDose,
            NextDueDate nextDueDate
    ) {
        var now = Instant.now();

        var deworming = new Deworming(
                DewormingId.generate(),
                petId,
                dewormingDate,
                drugName,
                drugDose,
                nextDueDate,
                now,
                now
        );

        deworming.events.add(new DewormingCreated(
                deworming.id.value(),
                petId.value(),
                drugName.value(),
                nextDueDate.value(),
                now
        ));

        return deworming;
    }

    public static Deworming reconstitute(
            DewormingId id,
            PetId petId,
            LocalDate dewormingDate,
            DrugName drugName,
            DrugDose drugDose,
            NextDueDate nextDueDate,
            Instant createdAt,
            Instant updatedAt
    ) {
        return new Deworming(id, petId, dewormingDate, drugName, drugDose, nextDueDate, createdAt, updatedAt);
    }

    // === Business Operations ===
    public void update(LocalDate newDewormingDate, DrugName newDrugName, DrugDose newDrugDose) {
        this.dewormingDate = Objects.requireNonNull(newDewormingDate, "Deworming date cannot be null");
        this.drugName = Objects.requireNonNull(newDrugName, "Drug name cannot be null");
        this.drugDose = Objects.requireNonNull(newDrugDose, "Drug dose cannot be null");
        touch();

        events.add(new DewormingUpdated(
                id.value(),
                drugName.value(),
                updatedAt
        ));
    }

    public void reschedule(LocalDate newNextDueDate) {
        this.nextDueDate = NextDueDate.after(dewormingDate, newNextDueDate);
        touch();

        events.add(new DewormingRescheduled(
                id.value(),
                nextDueDate.value(),
                updatedAt
        ));
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

    // === Events ===
    public List<DomainEvent> pullEvents() {
        var pendingEvents = List.copyOf(events);
        events.clear();
        return pendingEvents;
    }

    // === Getters ===
    public DewormingId getId() {
        return id;
    }

    public PetId getPetId() {
        return petId;
    }

    public LocalDate getDewormingDate() {
        return dewormingDate;
    }

    public DrugName getDrugName() {
        return drugName;
    }

    public DrugDose getDrugDose() {
        return drugDose;
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
        if (!(o instanceof Deworming other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Deworming{" +
                "id=" + id +
                ", petId=" + petId +
                ", dewormingDate=" + dewormingDate +
                ", drugName=" + drugName +
                ", drugDose=" + drugDose +
                ", nextDueDate=" + nextDueDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

}
