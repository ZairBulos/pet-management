package com.petmanagement.health.application.port.in;

import com.petmanagement.health.domain.model.aggregate.Deworming;
import com.petmanagement.health.domain.model.valueobject.DewormingId;

import java.time.LocalDate;

public interface RescheduleDewormingUseCase {
    Deworming execute(RescheduleDewormingCommand command);

    record RescheduleDewormingCommand(
            DewormingId dewormingId,
            LocalDate nextDueDate
    ) {}
}
