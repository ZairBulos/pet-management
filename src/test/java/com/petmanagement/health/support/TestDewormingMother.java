package com.petmanagement.health.support;

import com.petmanagement.health.domain.model.valueobject.DewormingId;
import com.petmanagement.health.domain.model.valueobject.DrugDose;
import com.petmanagement.health.domain.model.valueobject.DrugName;
import com.petmanagement.health.domain.model.valueobject.NextDueDate;

import java.time.LocalDate;

public final class TestDewormingMother {

    // === Deworming IDs ===

    public static final DewormingId DEFAULT_DEWORMING_ID =
            DewormingId.of("c9566006-7b81-4d9e-85c3-13e5e4813ec9");
    public static final DewormingId ANOTHER_DEWORMING_ID =
            DewormingId.of("1252e714-9f6c-488f-be49-8d64b4dfcbeb");
    public static final DewormingId NON_EXISTENT_DEWORMING_ID =
            DewormingId.of("5b7bc6a3-9b7e-429b-a075-9ebc6f409af3");

    // === Drug Names ===

    public static final DrugName DRUG_NAME_DRONTAL = new DrugName("Drontal");
    public static final DrugName DRUG_NAME_ADVOCATE = new DrugName("Advocate");
    public static final DrugName DRUG_NAME_ADVANTAGE = new DrugName("Advantage");
    public static final DrugName DRUG_NAME_REVOLUTION = new DrugName("Revolution");

    // === Drug Doses ===

    public static final DrugDose DRUG_DOSE_TABLET = new DrugDose("1 tablet (3mg)");
    public static final DrugDose DRUG_DOSE_LIQUID = new DrugDose("0.4 ml solution");
    public static final DrugDose DRUG_DOSE_SPOT_ON = new DrugDose("1 pipette spot-on");

    // === Next Due Dates ===
    public static final LocalDate DEWORMING_DATE =
            LocalDate.of(2026, 3, 15);
    public static final NextDueDate NEXT_DUE_DATE_THREE_MONTHS =
            new NextDueDate(LocalDate.of(2026, 6, 15));
    public static final NextDueDate NEXT_DUE_DATE_SIX_MONTHS =
            new NextDueDate(LocalDate.of(2026, 9, 15));

    private TestDewormingMother() {
    }

}
