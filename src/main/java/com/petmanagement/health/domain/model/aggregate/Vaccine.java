package com.petmanagement.health.domain.model.aggregate;

import com.petmanagement.health.domain.event.VaccineCreated;
import com.petmanagement.health.domain.event.VaccineRescheduled;
import com.petmanagement.health.domain.model.valueobject.NextDueDate;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.health.domain.model.valueobject.VaccineId;
import com.petmanagement.health.domain.model.valueobject.VaccineName;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.event.types.DomainEvent;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@AggregateRoot
public record Vaccine(
        VaccineId id,
        PetId petId,
        LocalDate vaccinationDate,
        VaccineName vaccineName,
        NextDueDate nextDueDate,
        Instant createdAt,
        List<DomainEvent> events
) {

    public Vaccine {
        Objects.requireNonNull(id, "Vaccine id cannot be null");
        Objects.requireNonNull(petId, "Pet id cannot be null");
        Objects.requireNonNull(vaccinationDate, "Vaccination date cannot be null");
        Objects.requireNonNull(vaccineName, "Vaccine name cannot be null");
        Objects.requireNonNull(nextDueDate, "Next due date cannot be null");
        Objects.requireNonNull(createdAt, "Created at cannot be null");
        Objects.requireNonNull(events, "Events cannot be null");
        events = List.copyOf(events);
    }

    // === Factory ===

    public static Vaccine create(
            PetId petId,
            LocalDate vaccinationDate,
            VaccineName vaccineName,
            NextDueDate nextDueDate
    ) {
        var id = VaccineId.generate();
        var now = Instant.now();

        return new Vaccine(
                id,
                petId,
                vaccinationDate,
                vaccineName,
                nextDueDate,
                now,
                List.of(new VaccineCreated(
                        id.value(),
                        petId.value(),
                        vaccineName.value(),
                        nextDueDate.value(),
                        now
                ))
        );
    }

    public static Vaccine reconstitute(
            VaccineId id,
            PetId petId,
            LocalDate vaccinationDate,
            VaccineName vaccineName,
            NextDueDate nextDueDate,
            Instant createdAt
    ) {
        return new Vaccine(id, petId, vaccinationDate, vaccineName, nextDueDate, createdAt, List.of());
    }

    // === Business Operations ===

    public Vaccine rescheduled(LocalDate newNextDueDate) {
        var newDueDate = NextDueDate.after(vaccinationDate, newNextDueDate);

        return new Vaccine(
                id,
                petId,
                vaccinationDate,
                vaccineName,
                newDueDate,
                createdAt,
                List.of(new VaccineRescheduled(
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
        if (!(o instanceof Vaccine other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
