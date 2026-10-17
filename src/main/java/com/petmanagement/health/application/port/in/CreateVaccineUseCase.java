package com.petmanagement.health.application.port.in;

import com.petmanagement.health.domain.model.valueobject.NextDueDate;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.health.domain.model.valueobject.VaccineId;
import com.petmanagement.health.domain.model.valueobject.VaccineName;

import java.time.LocalDate;

public interface CreateVaccineUseCase {
    VaccineId execute(CreateVaccineCommand command);

    record CreateVaccineCommand(
            PetId petId,
            LocalDate vaccinationDate,
            VaccineName vaccineName,
            NextDueDate nextDueDate
    ) {}
}
