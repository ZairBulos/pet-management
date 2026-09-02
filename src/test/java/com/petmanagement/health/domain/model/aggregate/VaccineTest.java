package com.petmanagement.health.domain.model.aggregate;

import com.petmanagement.health.domain.model.valueobject.NextDueDate;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.health.domain.model.valueobject.VaccineId;
import com.petmanagement.health.domain.model.valueobject.VaccineName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class VaccineTest {

    private static final PetId PET_ID = PetId.of("aa84d6ff-27a7-4402-a91f-b562ec5bebf6");
    private static final LocalDate VACCINATION_DATE = LocalDate.of(2026, 9, 1);
    private static final VaccineName VACCINE_NAME = new VaccineName("Rabies");
    private static final LocalDate NEXT_DUE_DATE_VALUE = LocalDate.of(2027, 9, 1);
    private static final NextDueDate NEXT_DUE_DATE = new NextDueDate(NEXT_DUE_DATE_VALUE);

    @Nested
    class Creation {

        @Test
        void shouldCreateVaccine() {
            var vaccine = Vaccine.create(PET_ID, VACCINATION_DATE, VACCINE_NAME, NEXT_DUE_DATE);

            assertNotNull(vaccine.getId());
            assertEquals(PET_ID, vaccine.getPetId());
            assertEquals(VACCINATION_DATE, vaccine.getVaccinationDate());
            assertEquals(VACCINE_NAME, vaccine.getVaccineName());
            assertEquals(NEXT_DUE_DATE, vaccine.getNextDueDate());
            assertNotNull(vaccine.getCreatedAt());
            assertNotNull(vaccine.getUpdatedAt());
        }

        @Test
        void shouldGenerateUniqueIdForEachVaccine() {
            var vaccine1 = Vaccine.create(PET_ID, VACCINATION_DATE, VACCINE_NAME, NEXT_DUE_DATE);
            var vaccine2 = Vaccine.create(PET_ID, VACCINATION_DATE, VACCINE_NAME, NEXT_DUE_DATE);

            assertNotEquals(vaccine1.getId(), vaccine2.getId());
        }

        @Test
        void shouldHaveSameCreatedAndUpdatedAtOnCreation() {
            var vaccine = Vaccine.create(PET_ID, VACCINATION_DATE, VACCINE_NAME, NEXT_DUE_DATE);

            assertEquals(vaccine.getCreatedAt(), vaccine.getUpdatedAt());
        }

        @Test
        void shouldThrowWhenCreatingWithNullPetId() {
            assertThrows(
                    NullPointerException.class,
                    () -> Vaccine.create(null, VACCINATION_DATE, VACCINE_NAME, NEXT_DUE_DATE)
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullVaccinationDate() {
            assertThrows(
                    NullPointerException.class,
                    () -> Vaccine.create(PET_ID, null, VACCINE_NAME, NEXT_DUE_DATE)
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullVaccineName() {
            assertThrows(
                    NullPointerException.class,
                    () -> Vaccine.create(PET_ID, VACCINATION_DATE, null, NEXT_DUE_DATE)
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullNextDueDate() {
            assertThrows(
                    NullPointerException.class,
                    () -> Vaccine.create(PET_ID, VACCINATION_DATE, VACCINE_NAME, null)
            );
        }

    }

    @Nested
    class Reconstitution {

        @Test
        void shouldReconstituteVaccine() {
            var id = VaccineId.generate();
            var createdAt = Instant.parse("2024-01-15T10:00:00Z");
            var updatedAt = Instant.parse("2024-01-20T15:30:00Z");

            var vaccine = Vaccine.reconstitute(id, PET_ID, VACCINATION_DATE, VACCINE_NAME, NEXT_DUE_DATE, createdAt, updatedAt);

            assertEquals(id, vaccine.getId());
            assertEquals(PET_ID, vaccine.getPetId());
            assertEquals(VACCINATION_DATE, vaccine.getVaccinationDate());
            assertEquals(VACCINE_NAME, vaccine.getVaccineName());
            assertEquals(NEXT_DUE_DATE, vaccine.getNextDueDate());
            assertEquals(createdAt, vaccine.getCreatedAt());
            assertEquals(updatedAt, vaccine.getUpdatedAt());
        }

        @Test
        void shouldThrowWhenReconstituteWithNullId() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Vaccine.reconstitute(null, PET_ID, VACCINATION_DATE, VACCINE_NAME, NEXT_DUE_DATE, now, now)
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullPetId() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Vaccine.reconstitute(VaccineId.generate(), null, VACCINATION_DATE, VACCINE_NAME, NEXT_DUE_DATE, now, now)
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullVaccinationDate() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Vaccine.reconstitute(VaccineId.generate(), PET_ID, null, VACCINE_NAME, NEXT_DUE_DATE, now, now)
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullVaccineName() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Vaccine.reconstitute(VaccineId.generate(), PET_ID, VACCINATION_DATE, null, NEXT_DUE_DATE, now, now)
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullNextDueDate() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Vaccine.reconstitute(VaccineId.generate(), PET_ID, VACCINATION_DATE, VACCINE_NAME, null, now, now)
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullCreatedAt() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Vaccine.reconstitute(VaccineId.generate(), PET_ID, VACCINATION_DATE, VACCINE_NAME, NEXT_DUE_DATE, null, now)
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullUpdatedAt() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Vaccine.reconstitute(VaccineId.generate(), PET_ID, VACCINATION_DATE, VACCINE_NAME, NEXT_DUE_DATE, now, null)
            );
        }

    }

    @Nested
    class Update {

        @Test
        void shouldUpdateVaccine() {
            var vaccine = Vaccine.create(PET_ID, VACCINATION_DATE, VACCINE_NAME, NEXT_DUE_DATE);
            var newVaccinationDate = LocalDate.of(2026, 10, 1);
            var newVaccineName = new VaccineName("Distemper");

            vaccine.update(newVaccinationDate, newVaccineName);

            assertEquals(newVaccinationDate, vaccine.getVaccinationDate());
            assertEquals(newVaccineName, vaccine.getVaccineName());
        }

        @Test
        void shouldThrowWhenUpdatingWithNullVaccinationDate() {
            var vaccine = Vaccine.create(PET_ID, VACCINATION_DATE, VACCINE_NAME, NEXT_DUE_DATE);

            assertThrows(
                    NullPointerException.class,
                    () -> vaccine.update(null, new VaccineName("Distemper"))
            );
        }

        @Test
        void shouldThrowWhenUpdatingWithNullVaccineName() {
            var vaccine = Vaccine.create(PET_ID, VACCINATION_DATE, VACCINE_NAME, NEXT_DUE_DATE);

            assertThrows(
                    NullPointerException.class,
                    () -> vaccine.update(LocalDate.of(2026, 10, 1), null)
            );
        }
    }

    @Nested
    class Reschedule {

        @Test
        void shouldRescheduleNextDueDate() {
            var vaccine = Vaccine.create(PET_ID, VACCINATION_DATE, VACCINE_NAME, NEXT_DUE_DATE);
            var newNextDueDate = LocalDate.of(2028, 1, 1);

            vaccine.reschedule(newNextDueDate);

            assertEquals(newNextDueDate, vaccine.getNextDueDate().value());
        }

        @Test
        void shouldThrowWhenReschedulingWithNullNextDueDate() {
            var vaccine = Vaccine.create(PET_ID, VACCINATION_DATE, VACCINE_NAME, NEXT_DUE_DATE);

            assertThrows(
                    NullPointerException.class,
                    () -> vaccine.reschedule(null)
            );
        }

        @Test
        void shouldThrowWhenReschedulingToDateEqualToVaccinationDate() {
            var vaccine = Vaccine.create(PET_ID, VACCINATION_DATE, VACCINE_NAME, NEXT_DUE_DATE);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> vaccine.reschedule(VACCINATION_DATE)
            );
        }

        @Test
        void shouldThrowWhenReschedulingToDateBeforeVaccinationDate() {
            var vaccine = Vaccine.create(PET_ID, VACCINATION_DATE, VACCINE_NAME, NEXT_DUE_DATE);
            var invalidNextDueDate = LocalDate.of(2026, 8, 31);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> vaccine.reschedule(invalidNextDueDate)
            );
        }

    }

    @Nested
    class DaysRemaining {

        @Test
        void shouldReturnDaysRemainingUntilNextDueDate() {
            var vaccine = Vaccine.create(PET_ID, VACCINATION_DATE, VACCINE_NAME, NEXT_DUE_DATE);
            var today = LocalDate.of(2027, 1, 1);

            assertEquals(243, vaccine.daysRemaining(today));
        }

        @Test
        void shouldReturnZeroWhenTodayIsNextDueDate() {
            var vaccine = Vaccine.create(PET_ID, VACCINATION_DATE, VACCINE_NAME, NEXT_DUE_DATE);

            assertEquals(0, vaccine.daysRemaining(NEXT_DUE_DATE_VALUE));
        }

        @Test
        void shouldReturnNegativeDaysWhenNextDueDateHasPassed() {
            var vaccine = Vaccine.create(PET_ID, VACCINATION_DATE, VACCINE_NAME, NEXT_DUE_DATE);
            var today = LocalDate.of(2027, 9, 10);

            assertEquals(-9, vaccine.daysRemaining(today));
        }

        @Test
        void shouldThrowWhenCalculatingDaysRemainingWithNullToday() {
            var vaccine = Vaccine.create(PET_ID, VACCINATION_DATE, VACCINE_NAME, NEXT_DUE_DATE);

            assertThrows(
                    NullPointerException.class,
                    () -> vaccine.daysRemaining(null)
            );
        }

    }

    @Nested
    class IsOverdue {

        @Test
        void shouldNotBeOverdueBeforeNextDueDate() {
            var vaccine = Vaccine.create(PET_ID, VACCINATION_DATE, VACCINE_NAME, NEXT_DUE_DATE);
            var today = LocalDate.of(2027, 8, 31);

            assertFalse(vaccine.isOverdue(today));
        }

        @Test
        void shouldNotBeOverdueOnNextDueDate() {
            var vaccine = Vaccine.create(PET_ID, VACCINATION_DATE, VACCINE_NAME, NEXT_DUE_DATE);

            assertFalse(vaccine.isOverdue(NEXT_DUE_DATE_VALUE));
        }

        @Test
        void shouldBeOverdueAfterNextDueDate() {
            var vaccine = Vaccine.create(PET_ID, VACCINATION_DATE, VACCINE_NAME, NEXT_DUE_DATE);
            var today = LocalDate.of(2027, 9, 2);

            assertTrue(vaccine.isOverdue(today));
        }

        @Test
        void shouldThrowWhenCheckingOverdueWithNullToday() {
            var vaccine = Vaccine.create(PET_ID, VACCINATION_DATE, VACCINE_NAME, NEXT_DUE_DATE);

            assertThrows(
                    NullPointerException.class,
                    () -> vaccine.isOverdue(null)
            );
        }

    }

}
