package com.petmanagement.health.support;

import com.petmanagement.health.domain.model.valueobject.NextDueDate;
import com.petmanagement.health.domain.model.valueobject.VaccineId;
import com.petmanagement.health.domain.model.valueobject.VaccineName;

import java.time.LocalDate;

public final class TestVaccineMother {

    // === Vaccine IDs ===

    public static final VaccineId DEFAULT_VACCINE_ID =
            VaccineId.of("1354cd00-c3e1-4cca-a289-d39113f3fcf8");
    public static final VaccineId ANOTHER_VACCINE_ID =
            VaccineId.of("f1c060bf-369e-433b-938c-5fac5c1f44fc");
    public static final VaccineId NON_EXISTENT_VACCINE_ID =
            VaccineId.of("d6c47c3c-cab9-49d2-aeef-b3a58a85ab8d");

    // === Vaccine Names ===
    public static final VaccineName VACCINE_NAME_RABIES = new VaccineName("Rabies");
    public static final VaccineName VACCINE_NAME_DISTEMPER = new VaccineName("Distemper");
    public static final VaccineName VACCINE_NAME_FVRCP = new VaccineName("FVRCP");
    public static final VaccineName VACCINE_NAME_DHPP = new VaccineName("DHPP");

    // === Next Due Dates ===
    public static final LocalDate VACCINE_VACCINATION_DATE =
            LocalDate.of(2026, 9, 1);
    public static final NextDueDate NEXT_DUE_DATE_THREE_MONTHS =
            new NextDueDate(LocalDate.of(2026, 12, 1));
    public static final NextDueDate NEXT_DUE_DATE_ONE_YEAR =
            new NextDueDate(LocalDate.of(2027, 9, 1));

    private TestVaccineMother() {
    }

}
