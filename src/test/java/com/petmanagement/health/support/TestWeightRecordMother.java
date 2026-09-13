package com.petmanagement.health.support;

import com.petmanagement.health.domain.model.valueobject.Weight;
import com.petmanagement.health.domain.model.valueobject.WeightRecordId;

public final class TestWeightRecordMother {

    // === WeightRecord IDs ===

    public static final WeightRecordId DEFAULT_WEIGHT_RECORD_ID =
            WeightRecordId.of("77e808e6-90ce-4846-9637-e69b545ce156");
    public static final WeightRecordId ANOTHER_WEIGHT_RECORD_ID =
            WeightRecordId.of("d4193db0-defd-4bc4-956f-ffc2b9083116");
    public static final WeightRecordId NON_EXISTENT_RECORD_ID =
            WeightRecordId.of("a577d6ad-cf50-4185-8bb3-4f5bb550d4de");

    // === Weights (kg) ===

    public static final Weight LIGHT_WEIGHT = Weight.of(3.4);
    public static final Weight MEDIUM_WEIGHT = Weight.of(5.0);
    public static final Weight HEAVY_WEIGHT = Weight.of(8.5);
    public static final Weight UPDATED_WEIGHT = Weight.of(5.8);

    private TestWeightRecordMother() {
    }

}
