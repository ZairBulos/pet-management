package com.petmanagement.pets.domain.model.aggregate;

import com.petmanagement.pets.domain.model.valueobject.PetId;
import com.petmanagement.pets.support.PetTestBuilder;
import com.petmanagement.pets.support.TestOwnerIdMother;
import com.petmanagement.pets.support.TestPetMother;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;

import static org.junit.jupiter.api.Assertions.*;

class PetTest {

    @Nested
    class Creation {

        @Test
        void shouldCreatePet() {
            var pet = PetTestBuilder.aPet().build();

            assertNotNull(pet.getId());
            assertEquals(TestOwnerIdMother.EXISTING_OWNER_ID, pet.getOwnerId());
            assertEquals(TestPetMother.PET_NAME_BUDDY, pet.getName());
            assertEquals(TestPetMother.SPECIES_DOG, pet.getSpecies());
            assertEquals(TestPetMother.BREED_GOLDEN_RETRIEVER, pet.getBreed());
            assertEquals(TestPetMother.COAT_GOLDEN, pet.getCoat());
            assertEquals(TestPetMother.SEX_MALE, pet.getSex());
            assertEquals(TestPetMother.BIRTH_DATE_STANDARD, pet.getBirthDate());
            assertNotNull(pet.getCreatedAt());
            assertNotNull(pet.getUpdatedAt());
        }

        @Test
        void shouldGenerateUniqueIdForEachPet() {
            var pet1 = PetTestBuilder.aPet().build();
            var pet2 = PetTestBuilder.aPet().build();

            assertNotEquals(pet1.getId(), pet2.getId());
        }

        @Test
        void shouldHaveSameCreatedAndUpdatedAtOnCreation() {
            var pet = PetTestBuilder.aPet().build();

            assertEquals(pet.getCreatedAt(), pet.getUpdatedAt());
        }

        @Test
        void shouldConvertNullBreedToUnknown() {
            var pet = PetTestBuilder.aPet()
                    .withBreedUnknown()
                    .build();

            assertTrue(pet.getBreed().isUnknown());
        }

        @Test
        void shouldThrowWhenCreatingWithNullOwnerId() {
            assertThrows(
                    NullPointerException.class,
                    () -> Pet.create(null,
                            TestPetMother.PET_NAME_BUDDY,
                            TestPetMother.SPECIES_DOG,
                            TestPetMother.BREED_GOLDEN_RETRIEVER,
                            TestPetMother.COAT_GOLDEN,
                            TestPetMother.SEX_MALE,
                            TestPetMother.BIRTH_DATE_STANDARD
                    )
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullPetName() {
            assertThrows(
                    NullPointerException.class,
                    () -> Pet.create(
                            TestOwnerIdMother.EXISTING_OWNER_ID,
                            null,
                            TestPetMother.SPECIES_DOG,
                            TestPetMother.BREED_GOLDEN_RETRIEVER,
                            TestPetMother.COAT_GOLDEN,
                            TestPetMother.SEX_MALE,
                            TestPetMother.BIRTH_DATE_STANDARD
                    )
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullSpecies() {
            assertThrows(
                    NullPointerException.class,
                    () -> Pet.create(
                            TestOwnerIdMother.EXISTING_OWNER_ID,
                            TestPetMother.PET_NAME_BUDDY,
                            null,
                            TestPetMother.BREED_GOLDEN_RETRIEVER,
                            TestPetMother.COAT_GOLDEN,
                            TestPetMother.SEX_MALE,
                            TestPetMother.BIRTH_DATE_STANDARD
                    )
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullCoat() {
            assertThrows(
                    NullPointerException.class,
                    () -> Pet.create(
                            TestOwnerIdMother.EXISTING_OWNER_ID,
                            TestPetMother.PET_NAME_BUDDY,
                            TestPetMother.SPECIES_DOG,
                            TestPetMother.BREED_GOLDEN_RETRIEVER,
                            null,
                            TestPetMother.SEX_MALE,
                            TestPetMother.BIRTH_DATE_STANDARD
                    )
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullSex() {
            assertThrows(
                    NullPointerException.class,
                    () -> Pet.create(
                            TestOwnerIdMother.EXISTING_OWNER_ID,
                            TestPetMother.PET_NAME_BUDDY,
                            TestPetMother.SPECIES_DOG,
                            TestPetMother.BREED_GOLDEN_RETRIEVER,
                            TestPetMother.COAT_GOLDEN,
                            null,
                            TestPetMother.BIRTH_DATE_STANDARD
                    )
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullBirthDate() {
            assertThrows(
                    NullPointerException.class,
                    () -> Pet.create(
                            TestOwnerIdMother.EXISTING_OWNER_ID,
                            TestPetMother.PET_NAME_BUDDY,
                            TestPetMother.SPECIES_DOG,
                            TestPetMother.BREED_GOLDEN_RETRIEVER,
                            TestPetMother.COAT_GOLDEN,
                            TestPetMother.SEX_MALE,
                            null
                    )
            );
        }

    }

    @Nested
    class Reconstitution {

        @Test
        void shouldReconstitutePet() {
            var petId = PetId.generate();
            var createdAt = Instant.parse("2024-01-15T10:00:00Z");
            var updatedAt = Instant.parse("2024-01-20T15:30:00Z");

            var pet = Pet.reconstitute(
                    petId,
                    TestOwnerIdMother.EXISTING_OWNER_ID,
                    TestPetMother.PET_NAME_BUDDY,
                    TestPetMother.SPECIES_DOG,
                    TestPetMother.BREED_GOLDEN_RETRIEVER,
                    TestPetMother.COAT_GOLDEN,
                    TestPetMother.SEX_MALE,
                    TestPetMother.BIRTH_DATE_STANDARD,
                    createdAt,
                    updatedAt
            );

            assertEquals(petId, pet.getId());
            assertEquals(TestOwnerIdMother.EXISTING_OWNER_ID, pet.getOwnerId());
            assertEquals(TestPetMother.PET_NAME_BUDDY, pet.getName());
            assertEquals(TestPetMother.SPECIES_DOG, pet.getSpecies());
            assertEquals(TestPetMother.BREED_GOLDEN_RETRIEVER, pet.getBreed());
            assertEquals(TestPetMother.COAT_GOLDEN, pet.getCoat());
            assertEquals(TestPetMother.SEX_MALE, pet.getSex());
            assertEquals(TestPetMother.BIRTH_DATE_STANDARD, pet.getBirthDate());
            assertEquals(createdAt, pet.getCreatedAt());
            assertEquals(updatedAt, pet.getUpdatedAt());
        }

        @Test
        void shouldThrowWhenReconstituteWithNullPetId() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Pet.reconstitute(
                            null,
                            TestOwnerIdMother.EXISTING_OWNER_ID,
                            TestPetMother.PET_NAME_BUDDY,
                            TestPetMother.SPECIES_DOG,
                            TestPetMother.BREED_GOLDEN_RETRIEVER,
                            TestPetMother.COAT_GOLDEN,
                            TestPetMother.SEX_MALE,
                            TestPetMother.BIRTH_DATE_STANDARD,
                            now,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullOwnerId() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Pet.reconstitute(
                            PetId.generate(), null,
                            TestPetMother.PET_NAME_BUDDY,
                            TestPetMother.SPECIES_DOG,
                            TestPetMother.BREED_GOLDEN_RETRIEVER,
                            TestPetMother.COAT_GOLDEN,
                            TestPetMother.SEX_MALE,
                            TestPetMother.BIRTH_DATE_STANDARD,
                            now,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullName() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Pet.reconstitute(
                            PetId.generate(),
                            TestOwnerIdMother.EXISTING_OWNER_ID,
                            null,
                            TestPetMother.SPECIES_DOG,
                            TestPetMother.BREED_GOLDEN_RETRIEVER,
                            TestPetMother.COAT_GOLDEN,
                            TestPetMother.SEX_MALE,
                            TestPetMother.BIRTH_DATE_STANDARD,
                            now,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullSpecies() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Pet.reconstitute(
                            PetId.generate(),
                            TestOwnerIdMother.EXISTING_OWNER_ID,
                            TestPetMother.PET_NAME_BUDDY,
                            null,
                            TestPetMother.BREED_GOLDEN_RETRIEVER,
                            TestPetMother.COAT_GOLDEN,
                            TestPetMother.SEX_MALE,
                            TestPetMother.BIRTH_DATE_STANDARD,
                            now,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullCoat() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Pet.reconstitute(
                            PetId.generate(),
                            TestOwnerIdMother.EXISTING_OWNER_ID,
                            TestPetMother.PET_NAME_BUDDY,
                            TestPetMother.SPECIES_DOG,
                            TestPetMother.BREED_GOLDEN_RETRIEVER,
                            null,
                            TestPetMother.SEX_MALE,
                            TestPetMother.BIRTH_DATE_STANDARD,
                            now,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullSex() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Pet.reconstitute(
                            PetId.generate(),
                            TestOwnerIdMother.EXISTING_OWNER_ID,
                            TestPetMother.PET_NAME_BUDDY,
                            TestPetMother.SPECIES_DOG,
                            TestPetMother.BREED_GOLDEN_RETRIEVER,
                            TestPetMother.COAT_GOLDEN,
                            null,
                            TestPetMother.BIRTH_DATE_STANDARD,
                            now,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullBirthDate() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Pet.reconstitute(
                            PetId.generate(),
                            TestOwnerIdMother.EXISTING_OWNER_ID,
                            TestPetMother.PET_NAME_BUDDY,
                            TestPetMother.SPECIES_DOG,
                            TestPetMother.BREED_GOLDEN_RETRIEVER,
                            TestPetMother.COAT_GOLDEN,
                            TestPetMother.SEX_MALE,
                            null,
                            now,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullCreatedAt() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Pet.reconstitute(
                            PetId.generate(),
                            TestOwnerIdMother.EXISTING_OWNER_ID,
                            TestPetMother.PET_NAME_BUDDY,
                            TestPetMother.SPECIES_DOG,
                            TestPetMother.BREED_GOLDEN_RETRIEVER,
                            TestPetMother.COAT_GOLDEN,
                            TestPetMother.SEX_MALE,
                            TestPetMother.BIRTH_DATE_STANDARD,
                            null,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullUpdatedAt() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Pet.reconstitute(
                            PetId.generate(),
                            TestOwnerIdMother.EXISTING_OWNER_ID,
                            TestPetMother.PET_NAME_BUDDY,
                            TestPetMother.SPECIES_DOG,
                            TestPetMother.BREED_GOLDEN_RETRIEVER,
                            TestPetMother.COAT_GOLDEN,
                            TestPetMother.SEX_MALE,
                            TestPetMother.BIRTH_DATE_STANDARD,
                            now,
                            null
                    )
            );
        }

    }

    @Nested
    class Rename {

        @Test
        void shouldRenamePet() {
            var pet = PetTestBuilder.aPet().build();
            var newName = TestPetMother.PET_NAME_CHARLIE;

            pet.rename(newName);

            assertEquals(newName, pet.getName());
        }

        @Test
        void shouldThrowWhenRenamingWithNullName() {
            var pet = PetTestBuilder.aPet().build();

            assertThrows(
                    NullPointerException.class,
                    () -> pet.rename(null)
            );
        }

    }

    @Nested
    class AgeInYears {

        @Test
        void shouldCalculateAgeForNewbornPet() {
            var birthDate = LocalDate.now();
            var pet = PetTestBuilder.aPet()
                    .withBirthDate(birthDate)
                    .build();

            var age = pet.ageInYears();

            assertEquals(0, age);
        }

        @Test
        void shouldCalculateAgeForPetBornLastYear() {
            var birthDate = LocalDate.now().minusYears(1).minusDays(1);
            var pet = PetTestBuilder.aPet()
                    .withBirthDate(birthDate)
                    .build();

            var age = pet.ageInYears();

            assertEquals(1, age);
        }

        @Test
        void shouldCalculateAgeForAdultPet() {
            var pet = PetTestBuilder.aPet()
                    .withBirthDate(TestPetMother.BIRTH_DATE_STANDARD)
                    .build();

            var age = pet.ageInYears();

            var expectedAge = Period.between(TestPetMother.BIRTH_DATE_STANDARD, LocalDate.now()).getYears();
            assertEquals(expectedAge, age);
        }

        @Test
        void shouldCalculateAgeForSeniorPet() {
            var pet = PetTestBuilder.aPet()
                    .withBirthDate(TestPetMother.BIRTH_DATE_OLD)
                    .build();

            var age = pet.ageInYears();

            var expectedAge = Period.between(TestPetMother.BIRTH_DATE_OLD, LocalDate.now()).getYears();
            assertEquals(expectedAge, age);
            assertTrue(age >= 6);
        }

        @Test
        void shouldCalculateAgeBeforeBirthdayThisYear() {
            var today = LocalDate.now();
            var birthdayThisYear = today.withDayOfMonth(today.getDayOfMonth() + 1);
            var birthDate = birthdayThisYear.minusYears(5);

            var pet = PetTestBuilder.aPet()
                    .withBirthDate(birthDate)
                    .build();

            var age = pet.ageInYears();

            assertEquals(4, age);
        }

        @Test
        void shouldCalculateAgeAfterBirthdayThisYear() {
            var today = LocalDate.now();
            var birthdayThisYear = today.withDayOfMonth(today.getDayOfMonth() - 1);
            var birthDate = birthdayThisYear.minusYears(5);

            var pet = PetTestBuilder.aPet()
                    .withBirthDate(birthDate)
                    .build();

            var age = pet.ageInYears();

            assertEquals(5, age);
        }

        @Test
        void shouldCalculateOnBirthdayDay() {
            var today = LocalDate.now();
            var birthDate = today.minusYears(3);

            var pet = PetTestBuilder.aPet()
                    .withBirthDate(birthDate)
                    .build();

            var age = pet.ageInYears();

            assertEquals(3, age);
        }

    }

}
