package com.petmanagement.pets.support;

import com.petmanagement.pets.domain.model.valueobject.OwnerId;

public final class TestOwnerIdMother {

    public static final OwnerId EXISTING_OWNER_ID =
            OwnerId.of("5c7a797c-0e71-4f0e-bfff-68928343f104");
    public static final OwnerId NON_EXISTING_OWNER_ID =
            OwnerId.of("7ba15525-6b16-4b37-a4d8-35bf4920a3ae");
    public static final OwnerId ANOTHER_OWNER_ID =
            OwnerId.of("ba1d2526-f2c2-41ff-ab2c-521ef6ddb59d");

    private TestOwnerIdMother() {
    }

}
