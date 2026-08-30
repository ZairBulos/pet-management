package com.petmanagement.pets.domain.model.aggregate;

import com.petmanagement.pets.domain.model.enums.Sex;
import com.petmanagement.pets.domain.model.valueobject.*;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;

import static org.junit.jupiter.api.Assertions.*;

class PetTest {

    private static final OwnerId OWNER_ID = OwnerId.of("550e8400-e29b-41d4-a716-446655440000");
    private static final PetName NAME = new PetName("Max");
    private static final Species SPECIES = new Species("Dog");
    private static final Breed BREED = new Breed("Labrador");
    private static final Coat COAT = new Coat("Brown");
    private static final Sex SEX = Sex.MALE;
    private static final LocalDate BIRTH_DATE = LocalDate.of(2020, 1, 15);

    @Nested
    class Creation {

        @Test
        void shouldCreatePet() {
            var pet = Pet.create(OWNER_ID, NAME, SPECIES, BREED, COAT, SEX, BIRTH_DATE);

            assertNotNull(pet.getId());
            assertEquals(OWNER_ID, pet.getOwnerId());
            assertEquals(NAME, pet.getName());
            assertEquals(SPECIES, pet.getSpecies());
            assertEquals(BREED, pet.getBreed());
            assertEquals(COAT, pet.getCoat());
            assertEquals(SEX, pet.getSex());
            assertEquals(BIRTH_DATE, pet.getBirthDate());
            assertNotNull(pet.getCreatedAt());
            assertNotNull(pet.getUpdatedAt());
        }

        @Test
        void shouldGenerateUniqueIdForEachPet() {
            var pet1 = Pet.create(OWNER_ID, NAME, SPECIES, BREED, COAT, SEX, BIRTH_DATE);
            var pet2 = Pet.create(OWNER_ID, NAME, SPECIES, BREED, COAT, SEX, BIRTH_DATE);

            assertNotEquals(pet1.getId(), pet2.getId());
        }

        @Test
        void shouldHaveSameCreatedAndUpdatedAtOnCreation() {
            var pet = Pet.create(OWNER_ID, NAME, SPECIES, BREED, COAT, SEX, BIRTH_DATE);

            assertEquals(pet.getCreatedAt(), pet.getUpdatedAt());
        }

        @Test
        void shouldConvertNullBreedToUnknown() {
            var pet = Pet.create(OWNER_ID, NAME, SPECIES, null, COAT, SEX, BIRTH_DATE);

            assertTrue(pet.getBreed().isUnknown());
        }

        @Test
        void shouldThrowWhenCreatingWithNullOwnerId() {
            assertThrows(
                    NullPointerException.class,
                    () -> Pet.create(null, NAME, SPECIES, BREED, COAT, SEX, BIRTH_DATE)
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullPetName() {
            assertThrows(
                    NullPointerException.class,
                    () -> Pet.create(OWNER_ID, null, SPECIES, BREED, COAT, SEX, BIRTH_DATE)
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullSpecies() {
            assertThrows(
                    NullPointerException.class,
                    () -> Pet.create(OWNER_ID, NAME, null, BREED, COAT, SEX, BIRTH_DATE)
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullCoat() {
            assertThrows(
                    NullPointerException.class,
                    () -> Pet.create(OWNER_ID, NAME, SPECIES, BREED, null, SEX, BIRTH_DATE)
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullSex() {
            assertThrows(
                    NullPointerException.class,
                    () -> Pet.create(OWNER_ID, NAME, SPECIES, BREED, COAT, null, BIRTH_DATE)
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullBirthDate() {
            assertThrows(
                    NullPointerException.class,
                    () -> Pet.create(OWNER_ID, NAME, SPECIES, BREED, COAT, SEX, null)
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

            var pet = Pet.reconstitute(petId, OWNER_ID, NAME, SPECIES, BREED, COAT, SEX, BIRTH_DATE, createdAt, updatedAt);

            assertEquals(petId, pet.getId());
            assertEquals(OWNER_ID, pet.getOwnerId());
            assertEquals(NAME, pet.getName());
            assertEquals(SPECIES, pet.getSpecies());
            assertEquals(BREED, pet.getBreed());
            assertEquals(COAT, pet.getCoat());
            assertEquals(SEX, pet.getSex());
            assertEquals(BIRTH_DATE, pet.getBirthDate());
            assertEquals(createdAt, pet.getCreatedAt());
            assertEquals(updatedAt, pet.getUpdatedAt());
        }

        @Test
        void shouldThrowWhenReconstituteWithNullPetId() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Pet.reconstitute(null, OWNER_ID, NAME, SPECIES, BREED, COAT, SEX, BIRTH_DATE, now, now)
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullOwnerId() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Pet.reconstitute(PetId.generate(), null, NAME, SPECIES, BREED, COAT, SEX, BIRTH_DATE, now, now)
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullName() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Pet.reconstitute(PetId.generate(), OWNER_ID, null, SPECIES, BREED, COAT, SEX, BIRTH_DATE, now, now)
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullSpecies() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Pet.reconstitute(PetId.generate(), OWNER_ID, NAME, null, BREED, COAT, SEX, BIRTH_DATE, now, now)
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullCoat() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Pet.reconstitute(PetId.generate(), OWNER_ID, NAME, SPECIES, BREED, null, SEX, BIRTH_DATE, now, now)
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullSex() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Pet.reconstitute(PetId.generate(), OWNER_ID, NAME, SPECIES, BREED, COAT, null, BIRTH_DATE, now, now)
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullBirthDate() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Pet.reconstitute(PetId.generate(), OWNER_ID, NAME, SPECIES, BREED, COAT, SEX, null, now, now)
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullCreatedAt() {
            assertThrows(NullPointerException.class,
                    () -> Pet.reconstitute(PetId.generate(), OWNER_ID, NAME, SPECIES, BREED, COAT, SEX, BIRTH_DATE, null, Instant.now())
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullUpdatedAt() {
            assertThrows(NullPointerException.class,
                    () -> Pet.reconstitute(PetId.generate(), OWNER_ID, NAME, SPECIES, BREED, COAT, SEX, BIRTH_DATE, Instant.now(), null)
            );
        }

    }

    @Nested
    class Rename {

        @Test
        void shouldRenamePet() {
            var pet = Pet.create(OWNER_ID, NAME, SPECIES, BREED, COAT, SEX, BIRTH_DATE);
            var newName = new PetName("Luna");

            pet.rename(newName);

            assertEquals(newName, pet.getName());
        }

        @Test
        void shouldThrowWhenRenamingWithNullName() {
            var pet = Pet.create(OWNER_ID, NAME, SPECIES, BREED, COAT, SEX, BIRTH_DATE);

            assertThrows(NullPointerException.class, () -> pet.rename(null));
        }

    }

    @Nested
    class AgeInYears {

        @Test
        void shouldCalculateAgeForNewbornPet() {
            var birthDate = LocalDate.now();
            var pet = Pet.create(OWNER_ID, NAME, SPECIES, BREED, COAT, SEX, birthDate);

            var age = pet.ageInYears();

            assertEquals(0, age);
        }

        @Test
        void shouldCalculateAgeForPetBornLastYear() {
            var birthDate = LocalDate.now().minusYears(1).minusDays(1);
            var pet = Pet.create(OWNER_ID, NAME, SPECIES, BREED, COAT, SEX, birthDate);

            var age = pet.ageInYears();

            assertEquals(1, age);
        }

        @Test
        void shouldCalculateAgeForAdultPet() {
            var birthDate = LocalDate.of(2015, 6, 15);
            var pet = Pet.create(OWNER_ID, NAME, SPECIES, BREED, COAT, SEX, birthDate);

            var age = pet.ageInYears();

            var expectedAge = Period.between(birthDate, LocalDate.now()).getYears();
            assertEquals(expectedAge, age);
        }

        @Test
        void shouldCalculateAgeForSeniorPet() {
            var birthDate = LocalDate.of(2005, 3, 1);
            var pet = Pet.create(OWNER_ID, NAME, SPECIES, BREED, COAT, SEX, birthDate);

            var age = pet.ageInYears();

            var expectedAge = Period.between(birthDate, LocalDate.now()).getYears();
            assertEquals(expectedAge, age);
            assertTrue(age >= 10);
        }

        @Test
        void shouldCalculateAgeBeforeBirthdayThisYear() {
            var today = LocalDate.now();
            var birthdayThisYear = today.withDayOfMonth(today.getDayOfMonth() + 1);
            var birthDate = birthdayThisYear.minusYears(5);

            var pet = Pet.create(OWNER_ID, NAME, SPECIES, BREED, COAT, SEX, birthDate);
            var age = pet.ageInYears();

            assertEquals(4, age);
        }

        @Test
        void shouldCalculateAgeAfterBirthdayThisYear() {
            var today = LocalDate.now();
            var birthdayThisYear = today.withDayOfMonth(today.getDayOfMonth() - 1);
            var birthDate = birthdayThisYear.minusYears(5);

            var pet = Pet.create(OWNER_ID, NAME, SPECIES, BREED, COAT, SEX, birthDate);
            var age = pet.ageInYears();

            assertEquals(5, age);
        }

        @Test
        void shouldCalculageOnBirthdayDay() {
            var today = LocalDate.now();
            var birthDate = today.minusYears(3);

            var pet = Pet.create(OWNER_ID, NAME, SPECIES, BREED, COAT, SEX, birthDate);
            var age = pet.ageInYears();

            assertEquals(3, age);
        }

    }

}
