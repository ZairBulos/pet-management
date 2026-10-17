package com.petmanagement.owners.support;

import com.petmanagement.owners.domain.model.valueobject.Email;
import com.petmanagement.owners.domain.model.valueobject.OwnerId;
import com.petmanagement.owners.domain.model.valueobject.OwnerName;
import com.petmanagement.owners.domain.model.valueobject.PhoneNumber;

public final class TestOwnerMother {

    // === Owner IDs ===

    public static final OwnerId DEFAULT_OWNER_ID =
            OwnerId.of("5c7a797c-0e71-4f0e-bfff-68928343f104");
    public static final OwnerId ANOTHER_OWNER_ID =
            OwnerId.of("ba1d2526-f2c2-41ff-ab2c-521ef6ddb59d");
    public static final OwnerId NON_EXISTING_OWNER_ID =
            OwnerId.of("7ba15525-6b16-4b37-a4d8-35bf4920a3ae");

    // === Names ===

    public static final OwnerName OWNER_NAME_JOHN = new OwnerName("John Doe");
    public static final OwnerName OWNER_NAME_JANE = new OwnerName("Jane Smith");
    public static final OwnerName OWNER_NAME_ROBERT = new OwnerName("Robert Johnson");
    public static final OwnerName OWNER_NAME_MARIA = new OwnerName("Maria Garcia");

    // === Emails ===

    public static final Email EMAIL_JOHN = new Email("john.doe@example.com");
    public static final Email EMAIL_JANE = new Email("jane.smith@test.com");
    public static final Email EMAIL_ROBERT = new Email("robert.j@workspace.org");
    public static final Email EMAIL_MARIA = new Email("maria.garcia@empresa.es");

    // === Phone numbers ===

    public static final PhoneNumber PHONE_JOHN = new PhoneNumber("+1 (234) 567-8900");
    public static final PhoneNumber PHONE_JANE = new PhoneNumber("9876543210");
    public static final PhoneNumber PHONE_ROBERT = new PhoneNumber("+34 91 123 4567");
    public static final PhoneNumber PHONE_MARIA = new PhoneNumber("(555) 987-6543");

    private TestOwnerMother() {
    }

}
