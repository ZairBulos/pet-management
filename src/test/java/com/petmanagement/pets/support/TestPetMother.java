package com.petmanagement.pets.support;

import com.petmanagement.pets.domain.model.enums.Sex;
import com.petmanagement.pets.domain.model.valueobject.*;

import java.time.LocalDate;

public final class TestPetMother {

    // === Pet IDs ===

    public static final PetId DEFAULT_PET_ID =
            PetId.of("f5a4adf1-160a-47eb-aa30-91af49fecb7a");
    public static final PetId ANOTHER_PET_ID =
            PetId.of("20748e54-9b9d-46a3-ab1c-6822559141de");
    public static final PetId NON_EXISTENT_PET_ID =
            PetId.of("180ae457-1a2a-42b5-80a4-5002423d3ff4");

    // === Names ===

    public static final PetName PET_NAME_BUDDY = new PetName("Buddy");
    public static final PetName PET_NAME_LUNA = new PetName("Luna");
    public static final PetName PET_NAME_BELLA = new PetName("Bella");
    public static final PetName PET_NAME_CHARLIE = new PetName("Charlie");

    // === Species ===

    public static final Species SPECIES_DOG = new Species("Dog");
    public static final Species SPECIES_CAT = new Species("Cat");

    // === Breeds ===

    public static final Breed BREED_GOLDEN_RETRIEVER = new Breed("Golden Retriever");
    public static final Breed BREED_SIAMESE = new Breed("Siamese");
    public static final Breed BREED_LABRADOR = new Breed("Labrador");
    public static final Breed BREED_MIXED = new Breed("Mixed");

    // === Coats ===

    public static final Coat COAT_GOLDEN = new Coat("Golden");
    public static final Coat COAT_BLACK = new Coat("Black");
    public static final Coat COAT_WHITE = new Coat("White");
    public static final Coat COAT_ORANGE_WHITE = new Coat("Orange-White");

    // === Sex ===

    public static final Sex SEX_MALE = Sex.MALE;
    public static final Sex SEX_FEMALE = Sex.FEMALE;

    // === Birth Dates ===

    public static final LocalDate BIRTH_DATE_STANDARD = LocalDate.of(2020, 5, 15);
    public static final LocalDate BIRTH_DATE_YOUNG = LocalDate.of(2023, 1, 1);
    public static final LocalDate BIRTH_DATE_OLD = LocalDate.of(2018, 3, 20);

    private TestPetMother() {
    }

}
