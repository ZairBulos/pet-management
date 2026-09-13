package com.petmanagement.health.domain.model.aggregate;

import com.petmanagement.health.domain.event.DewormingCreated;
import com.petmanagement.health.domain.event.DewormingRescheduled;
import com.petmanagement.health.domain.event.DewormingUpdated;
import com.petmanagement.health.domain.model.valueobject.DewormingId;
import com.petmanagement.health.support.DewormingTestBuilder;
import com.petmanagement.health.support.TestCommonMother;
import com.petmanagement.health.support.TestDewormingMother;
import com.petmanagement.health.support.TestPetIdMother;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DewormingTest {

    @Nested
    class Creation {

        @Test
        void shouldCreateDeworming() {
            var deworming = DewormingTestBuilder.aDeworming().build();

            assertNotNull(deworming.getId());
            assertEquals(TestPetIdMother.EXISTING_PET_ID, deworming.getPetId());
            assertEquals(TestDewormingMother.DEWORMING_DATE, deworming.getDewormingDate());
            assertEquals(TestDewormingMother.DRUG_NAME_DRONTAL, deworming.getDrugName());
            assertEquals(TestDewormingMother.DRUG_DOSE_TABLET, deworming.getDrugDose());
            assertEquals(TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS, deworming.getNextDueDate());
            assertNotNull(deworming.getCreatedAt());
            assertNotNull(deworming.getUpdatedAt());
        }

        @Test
        void shouldPublishDewormingCreatedEvent() {
            var deworming = DewormingTestBuilder.aDeworming().build();
            var events = deworming.pullEvents();

            assertEquals(1, events.size());
            assertInstanceOf(DewormingCreated.class, events.getFirst());
        }

        @Test
        void shouldGenerateUniqueIdForEachDeworming() {
            var deworming1 = DewormingTestBuilder.aDeworming().build();
            var deworming2 = DewormingTestBuilder.aDeworming().build();

            assertNotEquals(deworming1.getId(), deworming2.getId());
        }

        @Test
        void shouldHaveSameCreatedAndUpdatedAtOnCreation() {
            var deworming = DewormingTestBuilder.aDeworming().build();

            assertEquals(deworming.getCreatedAt(), deworming.getUpdatedAt());
        }

        @Test
        void shouldThrowWhenCreatingWithNullPetId() {
            assertThrows(
                    NullPointerException.class,
                    () -> Deworming.create(
                            null,
                            TestDewormingMother.DEWORMING_DATE,
                            TestDewormingMother.DRUG_NAME_DRONTAL,
                            TestDewormingMother.DRUG_DOSE_TABLET,
                            TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS
                    )
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullDewormingDate() {
            assertThrows(
                    NullPointerException.class,
                    () -> Deworming.create(
                            TestPetIdMother.EXISTING_PET_ID,
                            null,
                            TestDewormingMother.DRUG_NAME_DRONTAL,
                            TestDewormingMother.DRUG_DOSE_TABLET,
                            TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS
                    )
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullDrugName() {
            assertThrows(
                    NullPointerException.class,
                    () -> Deworming.create(
                            TestPetIdMother.EXISTING_PET_ID,
                            TestDewormingMother.DEWORMING_DATE,
                            null,
                            TestDewormingMother.DRUG_DOSE_TABLET,
                            TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS
                    )
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullDrugDose() {
            assertThrows(
                    NullPointerException.class,
                    () -> Deworming.create(
                            TestPetIdMother.EXISTING_PET_ID,
                            TestDewormingMother.DEWORMING_DATE,
                            TestDewormingMother.DRUG_NAME_DRONTAL,
                            null,
                            TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS
                    )
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullNextDueDate() {
            assertThrows(
                    NullPointerException.class,
                    () -> Deworming.create(
                            TestPetIdMother.EXISTING_PET_ID,
                            TestDewormingMother.DEWORMING_DATE,
                            TestDewormingMother.DRUG_NAME_DRONTAL,
                            TestDewormingMother.DRUG_DOSE_TABLET,
                            null
                    )
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

            var deworming = Deworming.reconstitute(
                    id,
                    TestPetIdMother.EXISTING_PET_ID,
                    TestDewormingMother.DEWORMING_DATE,
                    TestDewormingMother.DRUG_NAME_DRONTAL,
                    TestDewormingMother.DRUG_DOSE_TABLET,
                    TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS,
                    createdAt,
                    updatedAt
            );

            assertEquals(id, deworming.getId());
            assertEquals(TestPetIdMother.EXISTING_PET_ID, deworming.getPetId());
            assertEquals(TestDewormingMother.DEWORMING_DATE, deworming.getDewormingDate());
            assertEquals(TestDewormingMother.DRUG_NAME_DRONTAL, deworming.getDrugName());
            assertEquals(TestDewormingMother.DRUG_DOSE_TABLET, deworming.getDrugDose());
            assertEquals(TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS, deworming.getNextDueDate());
            assertEquals(createdAt, deworming.getCreatedAt());
            assertEquals(updatedAt, deworming.getUpdatedAt());
        }

        @Test
        void shouldThrowWhenReconstituteWithNullId() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Deworming.reconstitute(
                            null,
                            TestPetIdMother.EXISTING_PET_ID,
                            TestDewormingMother.DEWORMING_DATE,
                            TestDewormingMother.DRUG_NAME_DRONTAL,
                            TestDewormingMother.DRUG_DOSE_TABLET,
                            TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS,
                            now,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullPetId() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Deworming.reconstitute(
                            DewormingId.generate(),
                            null,
                            TestDewormingMother.DEWORMING_DATE,
                            TestDewormingMother.DRUG_NAME_DRONTAL,
                            TestDewormingMother.DRUG_DOSE_TABLET,
                            TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS,
                            now,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullDewormingDate() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Deworming.reconstitute(
                            DewormingId.generate(),
                            TestPetIdMother.EXISTING_PET_ID,
                            null,
                            TestDewormingMother.DRUG_NAME_DRONTAL,
                            TestDewormingMother.DRUG_DOSE_TABLET,
                            TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS,
                            now,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullDrugName() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Deworming.reconstitute(
                            DewormingId.generate(),
                            TestPetIdMother.EXISTING_PET_ID,
                            TestDewormingMother.DEWORMING_DATE,
                            null,
                            TestDewormingMother.DRUG_DOSE_TABLET,
                            TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS,
                            now,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullDrugDose() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Deworming.reconstitute(
                            DewormingId.generate(),
                            TestPetIdMother.EXISTING_PET_ID,
                            TestDewormingMother.DEWORMING_DATE,
                            TestDewormingMother.DRUG_NAME_DRONTAL,
                            null,
                            TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS,
                            now,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullNextDueDate() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Deworming.reconstitute(
                            DewormingId.generate(),
                            TestPetIdMother.EXISTING_PET_ID,
                            TestDewormingMother.DEWORMING_DATE,
                            TestDewormingMother.DRUG_NAME_DRONTAL,
                            TestDewormingMother.DRUG_DOSE_TABLET,
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
                    () -> Deworming.reconstitute(
                            DewormingId.generate(),
                            TestPetIdMother.EXISTING_PET_ID,
                            TestDewormingMother.DEWORMING_DATE,
                            TestDewormingMother.DRUG_NAME_DRONTAL,
                            TestDewormingMother.DRUG_DOSE_TABLET,
                            TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS,
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
                    () -> Deworming.reconstitute(
                            DewormingId.generate(),
                            TestPetIdMother.EXISTING_PET_ID,
                            TestDewormingMother.DEWORMING_DATE,
                            TestDewormingMother.DRUG_NAME_DRONTAL,
                            TestDewormingMother.DRUG_DOSE_TABLET,
                            TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS,
                            now,
                            null
                    )
            );
        }

    }

    @Nested
    class Update {

        @Test
        void shouldUpdateDeworming() {
            var deworming = DewormingTestBuilder.aDeworming().build();
            var newDewormingDate = TestCommonMother.UPDATE_DATE;
            var newDrugName = TestDewormingMother.DRUG_NAME_ADVOCATE;
            var newDrugDose = TestDewormingMother.DRUG_DOSE_LIQUID;

            deworming.update(newDewormingDate, newDrugName, newDrugDose);

            assertEquals(newDewormingDate, deworming.getDewormingDate());
            assertEquals(newDrugName, deworming.getDrugName());
            assertEquals(newDrugDose, deworming.getDrugDose());
        }

        @Test
        void shouldPublishDewormingUpdatedEvent() {
            var deworming = DewormingTestBuilder.aDeworming().build();
            deworming.pullEvents();

            var newDewormingDate = TestCommonMother.UPDATE_DATE;
            var newDrugName = TestDewormingMother.DRUG_NAME_ADVOCATE;
            var newDrugDose = TestDewormingMother.DRUG_DOSE_LIQUID;

            deworming.update(newDewormingDate, newDrugName, newDrugDose);
            var events = deworming.pullEvents();

            assertEquals(1, events.size());
            assertInstanceOf(DewormingUpdated.class, events.getFirst());
        }

        @Test
        void shouldThrowWhenUpdatingWithNullDewormingDate() {
            var deworming = DewormingTestBuilder.aDeworming().build();

            assertThrows(
                    NullPointerException.class,
                    () -> deworming.update(
                            null,
                            TestDewormingMother.DRUG_NAME_ADVOCATE,
                            TestDewormingMother.DRUG_DOSE_LIQUID
                    )
            );
        }

        @Test
        void shouldThrowWhenUpdatingWithNullDrugName() {
            var deworming = DewormingTestBuilder.aDeworming().build();

            assertThrows(
                    NullPointerException.class,
                    () -> deworming.update(
                            TestCommonMother.UPDATE_DATE,
                            null,
                            TestDewormingMother.DRUG_DOSE_LIQUID
                    )
            );
        }

        @Test
        void shouldThrowWhenUpdatingWithNullDrugDose() {
            var deworming = DewormingTestBuilder.aDeworming().build();

            assertThrows(
                    NullPointerException.class,
                    () -> deworming.update(
                            TestCommonMother.UPDATE_DATE,
                            TestDewormingMother.DRUG_NAME_ADVOCATE,
                            null
                    )
            );
        }

    }

    @Nested
    class Reschedule {

        @Test
        void shouldRescheduleNextDueDate() {
            var deworming = DewormingTestBuilder.aDeworming().build();
            var newNextDueDate = TestDewormingMother.NEXT_DUE_DATE_SIX_MONTHS.value();

            deworming.reschedule(newNextDueDate);

            assertEquals(newNextDueDate, deworming.getNextDueDate().value());
        }

        @Test
        void shouldPublishDewormingRescheduledEvent() {
            var deworming = DewormingTestBuilder.aDeworming().build();
            deworming.pullEvents();

            var newNextDueDate = TestDewormingMother.NEXT_DUE_DATE_SIX_MONTHS.value();

            deworming.reschedule(newNextDueDate);
            var events = deworming.pullEvents();

            assertEquals(1, events.size());
            assertInstanceOf(DewormingRescheduled.class, events.getFirst());
        }

        @Test
        void shouldThrowWhenReschedulingWithNullNextDueDate() {
            var deworming = DewormingTestBuilder.aDeworming().build();

            assertThrows(
                    NullPointerException.class,
                    () -> deworming.reschedule(null)
            );
        }

        @Test
        void shouldThrowWhenReschedulingToDateEqualToDewormingDate() {
            var deworming = DewormingTestBuilder.aDeworming()
                    .withDewormingDate(TestDewormingMother.DEWORMING_DATE)
                    .build();

            assertThrows(
                    IllegalArgumentException.class,
                    () -> deworming.reschedule(TestDewormingMother.DEWORMING_DATE)
            );
        }

        @Test
        void shouldThrowWhenReschedulingToDateBeforeDewormingDate() {
            var deworming = DewormingTestBuilder.aDeworming()
                    .withDewormingDate(TestDewormingMother.DEWORMING_DATE)
                    .build();

            var invalidNextDueDate = TestDewormingMother.DEWORMING_DATE.minusDays(1);

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
            var deworming = DewormingTestBuilder.aDeworming()
                    .withDewormingDate(TestDewormingMother.DEWORMING_DATE)
                    .withNextDueDate(TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS)
                    .build();

            var today = LocalDate.of(2026, 4, 15);

            var daysRemaining = deworming.daysRemaining(today);

            assertEquals(61, daysRemaining);
        }

        @Test
        void shouldReturnZeroWhenTodayIsNextDueDate() {
            var deworming = DewormingTestBuilder.aDeworming()
                    .withNextDueDate(TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS)
                    .build();

            var daysRemaining = deworming.daysRemaining(TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS.value());

            assertEquals(0, daysRemaining);
        }

        @Test
        void shouldReturnNegativeDaysWhenNextDueDateHasPassed() {
            var deworming = DewormingTestBuilder.aDeworming()
                    .withNextDueDate(TestDewormingMother.DEWORMING_DATE)
                    .withNextDueDate(TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS)
                    .build();

            var today = TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS.value().plusDays(10);

            var daysRemaining = deworming.daysRemaining(today);

            assertEquals(-10, daysRemaining);
        }

        @Test
        void shouldThrowWhenCalculatingDaysRemainingWithNullToday() {
            var deworming = DewormingTestBuilder.aDeworming().build();

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
            var deworming = DewormingTestBuilder.aDeworming()
                    .withNextDueDate(TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS)
                    .build();

            var today = TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS.value().minusDays(1);

            var isOverdue = deworming.isOverdue(today);

            assertFalse(isOverdue);
        }

        @Test
        void shouldNotBeOverdueOnNextDueDate() {
            var deworming = DewormingTestBuilder.aDeworming()
                    .withNextDueDate(TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS)
                    .build();

            var isOverdue = deworming.isOverdue(TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS.value());

            assertFalse(isOverdue);
        }

        @Test
        void shouldBeOverdueAfterNextDueDate() {
            var deworming = DewormingTestBuilder.aDeworming()
                    .withNextDueDate(TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS)
                    .build();

            var today = TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS.value().plusDays(1);

            var isOverdue = deworming.isOverdue(today);

            assertTrue(isOverdue);
        }

        @Test
        void shouldThrowWhenCheckingOverdueWithNullToday() {
            var deworming = DewormingTestBuilder.aDeworming().build();

            assertThrows(
                    NullPointerException.class,
                    () -> deworming.isOverdue(null)
            );
        }

    }

}
