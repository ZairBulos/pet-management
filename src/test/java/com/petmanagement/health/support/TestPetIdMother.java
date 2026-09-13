package com.petmanagement.health.support;

import com.petmanagement.health.domain.model.valueobject.PetId;

public final class TestPetIdMother {

    public static final PetId EXISTING_PET_ID = PetId.of("f5a4adf1-160a-47eb-aa30-91af49fecb7a");
    public static final PetId NON_EXISTENT_PET_ID = PetId.of("180ae457-1a2a-42b5-80a4-5002423d3ff4");
    public static final PetId ANOTHER_PET_ID = PetId.of("20748e54-9b9d-46a3-ab1c-6822559141de");

    private TestPetIdMother() {
    }

}
