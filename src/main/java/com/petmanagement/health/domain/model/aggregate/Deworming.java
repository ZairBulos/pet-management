package com.petmanagement.health.domain.model.aggregate;

import com.petmanagement.health.domain.event.DewormingCreated;
import com.petmanagement.health.domain.event.DewormingRescheduled;
import com.petmanagement.health.domain.model.valueobject.*;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.event.types.DomainEvent;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@AggregateRoot
public record Deworming(
        DewormingId id,
        PetId petId,
        LocalDate dewormingDate,
        DrugName drugName,
        DrugDose drugDose,
        NextDueDate nextDueDate,
        Instant createdAt,
        List<DomainEvent> events
) {

    public Deworming {
        Objects.requireNonNull(id, "Deworming id cannot be null");
        Objects.requireNonNull(petId, "Pet id cannot be null");
        Objects.requireNonNull(dewormingDate, "Deworming date cannot be null");
        Objects.requireNonNull(drugName, "Drug name cannot be null");
        Objects.requireNonNull(drugDose, "Drug dose cannot be null");
        Objects.requireNonNull(nextDueDate, "Next due date cannot be null");
        Objects.requireNonNull(createdAt, "Created at cannot be null");
        Objects.requireNonNull(events, "Events cannot be null");
        events = List.copyOf(events);
    }

    // === Factory ===

    public static Deworming create(
            PetId petId,
            LocalDate dewormingDate,
            DrugName drugName,
            DrugDose drugDose,
            NextDueDate nextDueDate
    ) {
        var id = DewormingId.generate();
        var now = Instant.now();

        return new Deworming(
                id,
                petId,
                dewormingDate,
                drugName,
                drugDose,
                nextDueDate,
                now,
                List.of(new DewormingCreated(
                        id.value(),
                        petId.value(),
                        drugName.value(),
                        nextDueDate.value(),
                        now
                ))
        );
    }

    public static Deworming reconstitute(
            DewormingId id,
            PetId petId,
            LocalDate dewormingDate,
            DrugName drugName,
            DrugDose drugDose,
            NextDueDate nextDueDate,
            Instant createdAt
    ) {
        return new Deworming(id, petId, dewormingDate, drugName, drugDose, nextDueDate, createdAt, List.of());
    }

    // === Business Operations ===

    public Deworming rescheduled(LocalDate newNextDueDate) {
        var newDueDate = NextDueDate.after(dewormingDate, newNextDueDate);

        return new Deworming(
                id,
                petId,
                dewormingDate,
                drugName,
                drugDose,
                newDueDate,
                createdAt,
                List.of(new DewormingRescheduled(
                        id.value(),
                        newDueDate.value(),
                        Instant.now()
                ))
        );
    }

    public long daysRemaining(LocalDate today) {
        return nextDueDate.daysRemaining(today);
    }

    public boolean isOverdue(LocalDate today) {
        return nextDueDate.isOverdue(today);
    }

    // === Events ===

    public List<DomainEvent> pullEvents() {
        return events;
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

}
