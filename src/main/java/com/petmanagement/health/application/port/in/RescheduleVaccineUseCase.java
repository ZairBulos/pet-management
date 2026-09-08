package com.petmanagement.health.application.port.in;

import com.petmanagement.health.domain.model.aggregate.Vaccine;
import com.petmanagement.health.domain.model.valueobject.VaccineId;

import java.time.LocalDate;

public interface RescheduleVaccineUseCase {
    Vaccine execute(RescheduleVaccineCommand command);

    record RescheduleVaccineCommand(VaccineId vaccineId, LocalDate nextDueDate) {}
}
