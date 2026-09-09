package com.petmanagement.health.application.port.in;

import com.petmanagement.health.domain.model.valueobject.*;

import java.time.LocalDate;

public interface CreateDewormingUseCase {
    DewormingId execute(CreateDewormingCommand command);

    record CreateDewormingCommand(
            PetId petId,
            LocalDate dewormingDate,
            DrugName drugName,
            DrugDose drugDose,
            NextDueDate nextDueDate
    ) {}
}
