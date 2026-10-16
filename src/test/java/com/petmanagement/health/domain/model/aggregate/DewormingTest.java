package com.petmanagement.health.domain.model.aggregate;

import com.petmanagement.health.domain.event.DewormingCreated;
import com.petmanagement.health.domain.event.DewormingRescheduled;
import com.petmanagement.health.domain.model.valueobject.DewormingId;
import com.petmanagement.health.support.DewormingTestBuilder;
import com.petmanagement.health.support.TestDewormingMother;
import com.petmanagement.health.support.TestPetIdMother;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class DewormingTest {

    @Nested
    class Creation {

        @Test
        void shouldCreateDeworming() {
            var deworming = DewormingTestBuilder.aDeworming().build();

            assertNotNull(deworming.id());
            assertEquals(TestPetIdMother.EXISTING_PET_ID, deworming.petId());
            assertEquals(TestDewormingMother.DEWORMING_DATE, deworming.dewormingDate());
            assertEquals(TestDewormingMother.DRUG_NAME_DRONTAL, deworming.drugName());
            assertEquals(TestDewormingMother.DRUG_DOSE_TABLET, deworming.drugDose());
            assertEquals(TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS, deworming.nextDueDate());
            assertNotNull(deworming.createdAt());
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

            assertNotEquals(deworming1.id(), deworming2.id());
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

            var deworming = Deworming.reconstitute(
                    id,
                    TestPetIdMother.EXISTING_PET_ID,
                    TestDewormingMother.DEWORMING_DATE,
                    TestDewormingMother.DRUG_NAME_DRONTAL,
                    TestDewormingMother.DRUG_DOSE_TABLET,
                    TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS,
                    createdAt
            );

            assertEquals(id, deworming.id());
            assertEquals(TestPetIdMother.EXISTING_PET_ID, deworming.petId());
            assertEquals(TestDewormingMother.DEWORMING_DATE, deworming.dewormingDate());
            assertEquals(TestDewormingMother.DRUG_NAME_DRONTAL, deworming.drugName());
            assertEquals(TestDewormingMother.DRUG_DOSE_TABLET, deworming.drugDose());
            assertEquals(TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS, deworming.nextDueDate());
            assertEquals(createdAt, deworming.createdAt());
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
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullPetId() {
            var id = DewormingId.generate();
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Deworming.reconstitute(
                            id,
                            null,
                            TestDewormingMother.DEWORMING_DATE,
                            TestDewormingMother.DRUG_NAME_DRONTAL,
                            TestDewormingMother.DRUG_DOSE_TABLET,
                            TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullDewormingDate() {
            var id = DewormingId.generate();
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Deworming.reconstitute(
                            id,
                            TestPetIdMother.EXISTING_PET_ID,
                            null,
                            TestDewormingMother.DRUG_NAME_DRONTAL,
                            TestDewormingMother.DRUG_DOSE_TABLET,
                            TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullDrugName() {
            var id = DewormingId.generate();
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Deworming.reconstitute(
                            id,
                            TestPetIdMother.EXISTING_PET_ID,
                            TestDewormingMother.DEWORMING_DATE,
                            null,
                            TestDewormingMother.DRUG_DOSE_TABLET,
                            TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullDrugDose() {
            var id = DewormingId.generate();
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Deworming.reconstitute(
                            id,
                            TestPetIdMother.EXISTING_PET_ID,
                            TestDewormingMother.DEWORMING_DATE,
                            TestDewormingMother.DRUG_NAME_DRONTAL,
                            null,
                            TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullNextDueDate() {
            var id = DewormingId.generate();
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Deworming.reconstitute(
                            id,
                            TestPetIdMother.EXISTING_PET_ID,
                            TestDewormingMother.DEWORMING_DATE,
                            TestDewormingMother.DRUG_NAME_DRONTAL,
                            TestDewormingMother.DRUG_DOSE_TABLET,
                            null,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullCreatedAt() {
            var id = DewormingId.generate();

            assertThrows(
                    NullPointerException.class,
                    () -> Deworming.reconstitute(
                            id,
                            TestPetIdMother.EXISTING_PET_ID,
                            TestDewormingMother.DEWORMING_DATE,
                            TestDewormingMother.DRUG_NAME_DRONTAL,
                            TestDewormingMother.DRUG_DOSE_TABLET,
                            TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS,
                            null
                    )
            );
        }

    }

    @Nested
    class Reschedule {

        @Test
        void shouldReturnNewDewormingWithRescheduledNextDueDate() {
            var deworming = DewormingTestBuilder.aDeworming().build();
            var newNextDueDate = TestDewormingMother.NEXT_DUE_DATE_SIX_MONTHS.value();

            var rescheduled = deworming.rescheduled(newNextDueDate);

            assertEquals(newNextDueDate, rescheduled.nextDueDate().value());
        }

        @Test
        void shouldKeepIdentityAndOtherFieldsWhenRescheduling() {
            var deworming = DewormingTestBuilder.aDeworming().build();

            var rescheduled = deworming.rescheduled(TestDewormingMother.NEXT_DUE_DATE_SIX_MONTHS.value());

            assertEquals(deworming.id(), rescheduled.id());
            assertEquals(deworming.petId(), rescheduled.petId());
            assertEquals(deworming.dewormingDate(), rescheduled.dewormingDate());
            assertEquals(deworming.drugName(), rescheduled.drugName());
            assertEquals(deworming.drugDose(), rescheduled.drugDose());
            assertEquals(deworming.createdAt(), rescheduled.createdAt());
        }

        @Test
        void shouldPublishDewormingRescheduledEvent() {
            var deworming = DewormingTestBuilder.aDeworming().build();

            var rescheduled = deworming.rescheduled(TestDewormingMother.NEXT_DUE_DATE_SIX_MONTHS.value());
            var events = rescheduled.pullEvents();

            assertEquals(1, events.size());
            assertInstanceOf(DewormingRescheduled.class, events.getFirst());
        }

        @Test
        void shouldThrowWhenReschedulingWithNullNextDueDate() {
            var deworming = DewormingTestBuilder.aDeworming().build();

            assertThrows(
                    NullPointerException.class,
                    () -> deworming.rescheduled(null)
            );
        }

        @Test
        void shouldThrowWhenReschedulingToDateEqualToDewormingDate() {
            var deworming = DewormingTestBuilder.aDeworming()
                    .withDewormingDate(TestDewormingMother.DEWORMING_DATE)
                    .build();

            assertThrows(
                    IllegalArgumentException.class,
                    () -> deworming.rescheduled(TestDewormingMother.DEWORMING_DATE)
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
                    () -> deworming.rescheduled(invalidNextDueDate)
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

            var today = TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS.value().minusDays(61);

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
                    .withDewormingDate(TestDewormingMother.DEWORMING_DATE)
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
