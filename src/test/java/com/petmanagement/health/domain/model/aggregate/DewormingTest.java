package com.petmanagement.health.domain.model.aggregate;

import com.petmanagement.health.domain.model.valueobject.*;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DewormingTest {

    private static final PetId PET_ID = PetId.of("9bcdaa1e-3e9e-4501-9f29-735a3bb807ea");
    private static final LocalDate DEWORMING_DATE = LocalDate.of(2026, 9, 1);
    private static final DrugName DRUG_NAME = new DrugName("Milbemax");
    private static final DrugDose DRUG_DOSE = new DrugDose("1 tablet");
    private static final LocalDate NEXT_DUE_DATE_VALUE = LocalDate.of(2026, 12, 1);
    private static final NextDueDate NEXT_DUE_DATE = NextDueDate.after(DEWORMING_DATE, NEXT_DUE_DATE_VALUE);

    @Nested
    class Creation {

        @Test
        void shouldCreateDeworming() {
            var deworming = Deworming.create(PET_ID, DEWORMING_DATE, DRUG_NAME, DRUG_DOSE, NEXT_DUE_DATE);

            assertNotNull(deworming.getId());
            assertEquals(PET_ID, deworming.getPetId());
            assertEquals(DEWORMING_DATE, deworming.getDewormingDate());
            assertEquals(DRUG_NAME, deworming.getDrugName());
            assertEquals(DRUG_DOSE, deworming.getDrugDose());
            assertEquals(NEXT_DUE_DATE, deworming.getNextDueDate());
            assertNotNull(deworming.getCreatedAt());
            assertNotNull(deworming.getUpdatedAt());
        }

        @Test
        void shouldGenerateUniqueIdForEachDeworming() {
            var deworming1 = Deworming.create(PET_ID, DEWORMING_DATE, DRUG_NAME, DRUG_DOSE, NEXT_DUE_DATE);
            var deworming2 = Deworming.create(PET_ID, DEWORMING_DATE, DRUG_NAME, DRUG_DOSE, NEXT_DUE_DATE);

            assertNotEquals(deworming1.getId(), deworming2.getId());
        }

        @Test
        void shouldHaveSameCreatedAndUpdatedAtOnCreation() {
            var deworming = Deworming.create(PET_ID, DEWORMING_DATE, DRUG_NAME, DRUG_DOSE, NEXT_DUE_DATE);

            assertEquals(deworming.getCreatedAt(), deworming.getUpdatedAt());
        }

        @Test
        void shouldThrowWhenCreatingWithNullPetId() {
            assertThrows(
                    NullPointerException.class,
                    () -> Deworming.create(null, DEWORMING_DATE, DRUG_NAME, DRUG_DOSE, NEXT_DUE_DATE)
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullDewormingDate() {
            assertThrows(
                    NullPointerException.class,
                    () -> Deworming.create(PET_ID, null, DRUG_NAME, DRUG_DOSE, NEXT_DUE_DATE)
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullDrugName() {
            assertThrows(
                    NullPointerException.class,
                    () -> Deworming.create(PET_ID, DEWORMING_DATE, null, DRUG_DOSE, NEXT_DUE_DATE)
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullDrugDose() {
            assertThrows(
                    NullPointerException.class,
                    () -> Deworming.create(PET_ID, DEWORMING_DATE, DRUG_NAME, null, NEXT_DUE_DATE)
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullNextDueDate() {
            assertThrows(
                    NullPointerException.class,
                    () -> Deworming.create(PET_ID, DEWORMING_DATE, DRUG_NAME, DRUG_DOSE, null)
            );
        }

    }

    @Nested
    class Reconstitution {

        @Test
        void shouldReconstituteDeworming() {
            var id = DewormingId.generate();
            var createdAt = Instant.parse("2024-01-15T10:00:00Z");
            var updatedAt = Instant.parse("2024-01-20T15:30:00Z");

            var deworming = Deworming.reconstitute(id, PET_ID, DEWORMING_DATE, DRUG_NAME, DRUG_DOSE, NEXT_DUE_DATE, createdAt, updatedAt);

            assertEquals(id, deworming.getId());
            assertEquals(PET_ID, deworming.getPetId());
            assertEquals(DEWORMING_DATE, deworming.getDewormingDate());
            assertEquals(DRUG_NAME, deworming.getDrugName());
            assertEquals(DRUG_DOSE, deworming.getDrugDose());
            assertEquals(NEXT_DUE_DATE, deworming.getNextDueDate());
            assertEquals(createdAt, deworming.getCreatedAt());
            assertEquals(updatedAt, deworming.getUpdatedAt());
        }

        @Test
        void shouldThrowWhenReconstituteWithNullId() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Deworming.reconstitute(null, PET_ID, DEWORMING_DATE, DRUG_NAME, DRUG_DOSE, NEXT_DUE_DATE, now, now)
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullPetId() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Deworming.reconstitute(DewormingId.generate(), null, DEWORMING_DATE, DRUG_NAME, DRUG_DOSE, NEXT_DUE_DATE, now, now)
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullDewormingDate() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Deworming.reconstitute(DewormingId.generate(), PET_ID, null, DRUG_NAME, DRUG_DOSE, NEXT_DUE_DATE, now, now)
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullDrugName() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Deworming.reconstitute(DewormingId.generate(), PET_ID, DEWORMING_DATE, null, DRUG_DOSE, NEXT_DUE_DATE, now, now)
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullDrugDose() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Deworming.reconstitute(DewormingId.generate(), PET_ID, DEWORMING_DATE, DRUG_NAME, null, NEXT_DUE_DATE, now, now)
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullNextDueDate() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Deworming.reconstitute(DewormingId.generate(), PET_ID, DEWORMING_DATE, DRUG_NAME, DRUG_DOSE, null, now, now)
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullCreatedAt() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Deworming.reconstitute(DewormingId.generate(), PET_ID, DEWORMING_DATE, DRUG_NAME, DRUG_DOSE, NEXT_DUE_DATE, null, now)
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullUpdatedAt() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Deworming.reconstitute(DewormingId.generate(), PET_ID, DEWORMING_DATE, DRUG_NAME, DRUG_DOSE, NEXT_DUE_DATE, now, null)
            );
        }

    }

    @Nested
    class Update {

        @Test
        void shouldUpdateDeworming() {
            var deworming = Deworming.create(PET_ID, DEWORMING_DATE, DRUG_NAME, DRUG_DOSE, NEXT_DUE_DATE);
            var newDewormingDate = LocalDate.of(2026, 9, 15);
            var newDrugName = new DrugName("Drontal");
            var newDrugDose = new DrugDose("2 tablets");

            deworming.update(newDewormingDate, newDrugName, newDrugDose);

            assertEquals(newDewormingDate, deworming.getDewormingDate());
            assertEquals(newDrugName, deworming.getDrugName());
            assertEquals(newDrugDose, deworming.getDrugDose());
        }

        @Test
        void shouldThrowWhenUpdatingWithNullDewormingDate() {
            var deworming = Deworming.create(PET_ID, DEWORMING_DATE, DRUG_NAME, DRUG_DOSE, NEXT_DUE_DATE);

            assertThrows(
                    NullPointerException.class,
                    () -> deworming.update(null, DRUG_NAME, DRUG_DOSE)
            );
        }

        @Test
        void shouldThrowWhenUpdatingWithNullDrugName() {
            var deworming = Deworming.create(PET_ID, DEWORMING_DATE, DRUG_NAME, DRUG_DOSE, NEXT_DUE_DATE);

            assertThrows(
                    NullPointerException.class,
                    () -> deworming.update(DEWORMING_DATE, null, DRUG_DOSE)
            );
        }

        @Test
        void shouldThrowWhenUpdatingWithNullDrugDose() {
            var deworming = Deworming.create(PET_ID, DEWORMING_DATE, DRUG_NAME, DRUG_DOSE, NEXT_DUE_DATE);

            assertThrows(
                    NullPointerException.class,
                    () -> deworming.update(DEWORMING_DATE, DRUG_NAME, null)
            );
        }

    }

    @Nested
    class Reschedule {

        @Test
        void shouldRescheduleNextDueDate() {
            var deworming = Deworming.create(PET_ID, DEWORMING_DATE, DRUG_NAME, DRUG_DOSE, NEXT_DUE_DATE);
            var newNextDueDate = LocalDate.of(2027, 1, 1);

            deworming.reschedule(newNextDueDate);

            assertEquals(newNextDueDate, deworming.getNextDueDate().value());
        }

        @Test
        void shouldThrowWhenReschedulingWithNullNextDueDate() {
            var deworming = Deworming.create(PET_ID, DEWORMING_DATE, DRUG_NAME, DRUG_DOSE, NEXT_DUE_DATE);

            assertThrows(
                    NullPointerException.class,
                    () -> deworming.reschedule(null)
            );
        }

        @Test
        void shouldThrowWhenReschedulingToDateEqualToDewormingDate() {
            var deworming = Deworming.create(PET_ID, DEWORMING_DATE, DRUG_NAME, DRUG_DOSE, NEXT_DUE_DATE);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> deworming.reschedule(DEWORMING_DATE)
            );
        }

        @Test
        void shouldThrowWhenReschedulingToDateBeforeDewormingDate() {
            var deworming = Deworming.create(PET_ID, DEWORMING_DATE, DRUG_NAME, DRUG_DOSE, NEXT_DUE_DATE);
            var invalidNextDueDate = LocalDate.of(2026, 8, 31);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> deworming.reschedule(invalidNextDueDate)
            );
        }

    }

    @Nested
    class DaysRemaining {

        @Test
        void shouldReturnDaysRemainingUntilNextDueDate() {
            var deworming = Deworming.create(PET_ID, DEWORMING_DATE, DRUG_NAME, DRUG_DOSE, NEXT_DUE_DATE);
            var today = LocalDate.of(2026, 10, 1);

            assertEquals(61, deworming.daysRemaining(today));
        }

        @Test
        void shouldReturnZeroWhenTodayIsNextDueDate() {
            var deworming = Deworming.create(PET_ID, DEWORMING_DATE, DRUG_NAME, DRUG_DOSE, NEXT_DUE_DATE);

            assertEquals(0, deworming.daysRemaining(NEXT_DUE_DATE_VALUE));
        }

        @Test
        void shouldReturnNegativeDaysWhenNextDueDateHasPassed() {
            var deworming = Deworming.create(PET_ID, DEWORMING_DATE, DRUG_NAME, DRUG_DOSE, NEXT_DUE_DATE);
            var today = LocalDate.of(2026, 12, 10);

            assertEquals(-9, deworming.daysRemaining(today));
        }

        @Test
        void shouldThrowWhenCalculatingDaysRemainingWithNullToday() {
            var deworming = Deworming.create(PET_ID, DEWORMING_DATE, DRUG_NAME, DRUG_DOSE, NEXT_DUE_DATE);

            assertThrows(
                    NullPointerException.class,
                    () -> deworming.daysRemaining(null)
            );
        }

    }

    @Nested
    class IsOverdue {

        @Test
        void shouldNotBeOverdueBeforeNextDueDate() {
            var deworming = Deworming.create(PET_ID, DEWORMING_DATE, DRUG_NAME, DRUG_DOSE, NEXT_DUE_DATE);
            var today = LocalDate.of(2026, 11, 30);

            assertFalse(deworming.isOverdue(today));
        }

        @Test
        void shouldNotBeOverdueOnNextDueDate() {
            var deworming = Deworming.create(PET_ID, DEWORMING_DATE, DRUG_NAME, DRUG_DOSE, NEXT_DUE_DATE);

            assertFalse(deworming.isOverdue(NEXT_DUE_DATE_VALUE));
        }

        @Test
        void shouldBeOverdueAfterNextDueDate() {
            var deworming = Deworming.create(PET_ID, DEWORMING_DATE, DRUG_NAME, DRUG_DOSE, NEXT_DUE_DATE);
            var today = LocalDate.of(2026, 12, 2);

            assertTrue(deworming.isOverdue(today));
        }

        @Test
        void shouldThrowWhenCheckingOverdueWithNullToday() {
            var deworming = Deworming.create(PET_ID, DEWORMING_DATE, DRUG_NAME, DRUG_DOSE, NEXT_DUE_DATE);

            assertThrows(
                    NullPointerException.class,
                    () -> deworming.isOverdue(null)
            );
        }

    }

}
