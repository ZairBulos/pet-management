package com.petmanagement.health.application.port.in;

import com.petmanagement.health.domain.model.aggregate.Deworming;
import com.petmanagement.health.domain.model.valueobject.DewormingId;
import com.petmanagement.health.domain.model.valueobject.DrugDose;
import com.petmanagement.health.domain.model.valueobject.DrugName;

import java.time.LocalDate;

public interface UpdateDewormingUseCase {
    Deworming execute(UpdateDewormingCommand command);

    record UpdateDewormingCommand(
            DewormingId dewormingId,
            LocalDate dewormingDate,
            DrugName drugName,
            DrugDose drugDose
    ) {}
}
