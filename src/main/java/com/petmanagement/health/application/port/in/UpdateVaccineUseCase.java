package com.petmanagement.health.application.port.in;

import com.petmanagement.health.domain.model.aggregate.Vaccine;
import com.petmanagement.health.domain.model.valueobject.VaccineId;
import com.petmanagement.health.domain.model.valueobject.VaccineName;

import java.time.LocalDate;

public interface UpdateVaccineUseCase {
    Vaccine execute(UpdateVaccineCommand command);

    record UpdateVaccineCommand(
            VaccineId vaccineId,
            LocalDate vaccinationDate,
            VaccineName vaccineName
    ) {}
}
