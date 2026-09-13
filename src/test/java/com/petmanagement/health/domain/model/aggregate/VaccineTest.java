package com.petmanagement.health.domain.model.aggregate;

import com.petmanagement.health.domain.event.VaccineCreated;
import com.petmanagement.health.domain.event.VaccineRescheduled;
import com.petmanagement.health.domain.event.VaccineUpdated;
import com.petmanagement.health.domain.model.valueobject.VaccineId;
import com.petmanagement.health.support.TestCommonMother;
import com.petmanagement.health.support.TestPetIdMother;
import com.petmanagement.health.support.TestVaccineMother;
import com.petmanagement.health.support.VaccineTestBuilder;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class VaccineTest {

    @Nested
    class Creation {

        @Test
        void shouldCreateVaccine() {
            var vaccine = VaccineTestBuilder.aVaccine().build();

            assertNotNull(vaccine.getId());
            assertEquals(TestPetIdMother.EXISTING_PET_ID, vaccine.getPetId());
            assertEquals(TestVaccineMother.VACCINE_VACCINATION_DATE, vaccine.getVaccinationDate());
            assertEquals(TestVaccineMother.VACCINE_NAME_RABIES, vaccine.getVaccineName());
            assertEquals(TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR, vaccine.getNextDueDate());
            assertNotNull(vaccine.getCreatedAt());
            assertNotNull(vaccine.getUpdatedAt());
        }

        @Test
        void shouldPublishVaccineCreatedEvent() {
            var vaccine = VaccineTestBuilder.aVaccine().build();
            var events = vaccine.pullEvents();

            assertEquals(1, events.size());
            assertInstanceOf(VaccineCreated.class, events.getFirst());
        }

        @Test
        void shouldGenerateUniqueIdForEachVaccine() {
            var vaccine1 = VaccineTestBuilder.aVaccine().build();
            var vaccine2 = VaccineTestBuilder.aVaccine().build();

            assertNotEquals(vaccine1.getId(), vaccine2.getId());
        }

        @Test
        void shouldHaveSameCreatedAndUpdatedAtOnCreation() {
            var vaccine = VaccineTestBuilder.aVaccine().build();

            assertEquals(vaccine.getCreatedAt(), vaccine.getUpdatedAt());
        }

        @Test
        void shouldThrowWhenCreatingWithNullPetId() {
            assertThrows(
                    NullPointerException.class,
                    () -> Vaccine.create(
                            null,
                            TestCommonMother.EARLY_DATE,
                            TestVaccineMother.VACCINE_NAME_RABIES,
                            TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR
                    )
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullVaccinationDate() {
            assertThrows(
                    NullPointerException.class,
                    () -> Vaccine.create(
                            TestPetIdMother.EXISTING_PET_ID,
                            null,
                            TestVaccineMother.VACCINE_NAME_RABIES,
                            TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR
                    )
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullVaccineName() {
            assertThrows(
                    NullPointerException.class,
                    () -> Vaccine.create(
                            TestPetIdMother.EXISTING_PET_ID,
                            TestCommonMother.EARLY_DATE,
                            null,
                            TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR
                    )
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullNextDueDate() {
            assertThrows(
                    NullPointerException.class,
                    () -> Vaccine.create(
                            TestPetIdMother.EXISTING_PET_ID,
                            TestCommonMother.EARLY_DATE,
                            TestVaccineMother.VACCINE_NAME_RABIES,
                            null
                    )
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

            var vaccine = Vaccine.reconstitute(
                    id,
                    TestPetIdMother.EXISTING_PET_ID,
                    TestCommonMother.EARLY_DATE,
                    TestVaccineMother.VACCINE_NAME_RABIES,
                    TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR,
                    createdAt,
                    updatedAt
            );

            assertEquals(id, vaccine.getId());
            assertEquals(TestPetIdMother.EXISTING_PET_ID, vaccine.getPetId());
            assertEquals(TestCommonMother.EARLY_DATE, vaccine.getVaccinationDate());
            assertEquals(TestVaccineMother.VACCINE_NAME_RABIES, vaccine.getVaccineName());
            assertEquals(TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR, vaccine.getNextDueDate());
            assertEquals(createdAt, vaccine.getCreatedAt());
            assertEquals(updatedAt, vaccine.getUpdatedAt());
        }

        @Test
        void shouldThrowWhenReconstituteWithNullId() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Vaccine.reconstitute(
                            null,
                            TestPetIdMother.EXISTING_PET_ID,
                            TestCommonMother.EARLY_DATE,
                            TestVaccineMother.VACCINE_NAME_RABIES,
                            TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR,
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
                    () -> Vaccine.reconstitute(
                            VaccineId.generate(),
                            null,
                            TestCommonMother.EARLY_DATE,
                            TestVaccineMother.VACCINE_NAME_RABIES,
                            TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR,
                            now,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullVaccinationDate() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Vaccine.reconstitute(
                            VaccineId.generate(),
                            TestPetIdMother.EXISTING_PET_ID,
                            null,
                            TestVaccineMother.VACCINE_NAME_RABIES,
                            TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR,
                            now,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullVaccineName() {
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Vaccine.reconstitute(
                            VaccineId.generate(),
                            TestPetIdMother.EXISTING_PET_ID,
                            TestCommonMother.EARLY_DATE,
                            null,
                            TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR,
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
                    () -> Vaccine.reconstitute(
                            VaccineId.generate(),
                            TestPetIdMother.EXISTING_PET_ID,
                            TestCommonMother.EARLY_DATE,
                            TestVaccineMother.VACCINE_NAME_RABIES,
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
                    () -> Vaccine.reconstitute(
                            VaccineId.generate(),
                            TestPetIdMother.EXISTING_PET_ID,
                            TestCommonMother.EARLY_DATE,
                            TestVaccineMother.VACCINE_NAME_RABIES,
                            TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR,
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
                    () -> Vaccine.reconstitute(
                            VaccineId.generate(),
                            TestPetIdMother.EXISTING_PET_ID,
                            TestCommonMother.EARLY_DATE,
                            TestVaccineMother.VACCINE_NAME_RABIES,
                            TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR,
                            now,
                            null
                    )
            );
        }

    }

    @Nested
    class Update {

        @Test
        void shouldUpdateVaccine() {
            var vaccine = VaccineTestBuilder.aVaccine().build();
            var newVaccinationDate = TestCommonMother.UPDATE_DATE;
            var newVaccineName = TestVaccineMother.VACCINE_NAME_DISTEMPER;

            vaccine.update(newVaccinationDate, newVaccineName);

            assertEquals(newVaccinationDate, vaccine.getVaccinationDate());
            assertEquals(newVaccineName, vaccine.getVaccineName());
        }

        @Test
        void shouldPublishVaccineUpdatedEvent() {
            var vaccine = VaccineTestBuilder.aVaccine().build();
            vaccine.pullEvents();

            var newVaccinationDate = TestCommonMother.MIDDLE_DATE;
            var newVaccineName = TestVaccineMother.VACCINE_NAME_DISTEMPER;

            vaccine.update(newVaccinationDate, newVaccineName);
            var events = vaccine.pullEvents();

            assertEquals(1, events.size());
            assertInstanceOf(VaccineUpdated.class, events.getFirst());
        }

        @Test
        void shouldThrowWhenUpdatingWithNullVaccinationDate() {
            var vaccine = VaccineTestBuilder.aVaccine().build();

            assertThrows(
                    NullPointerException.class,
                    () -> vaccine.update(null, TestVaccineMother.VACCINE_NAME_DISTEMPER)
            );
        }

        @Test
        void shouldThrowWhenUpdatingWithNullVaccineName() {
            var vaccine = VaccineTestBuilder.aVaccine().build();

            assertThrows(
                    NullPointerException.class,
                    () -> vaccine.update(TestCommonMother.MIDDLE_DATE, null)
            );
        }
    }

    @Nested
    class Reschedule {

        @Test
        void shouldRescheduleNextDueDate() {
            var vaccine = VaccineTestBuilder.aVaccine().build();
            var newNextDueDate = LocalDate.of(2028, 1, 1);

            vaccine.reschedule(newNextDueDate);

            assertEquals(newNextDueDate, vaccine.getNextDueDate().value());
        }

        @Test
        void shouldPublishVaccineRescheduledEvent() {
            var vaccine = VaccineTestBuilder.aVaccine().build();
            vaccine.pullEvents();

            var newNextDueDate = LocalDate.of(2028, 1, 1);

            vaccine.reschedule(newNextDueDate);
            var events = vaccine.pullEvents();

            assertEquals(1, events.size());
            assertInstanceOf(VaccineRescheduled.class, events.getFirst());
        }

        @Test
        void shouldThrowWhenReschedulingWithNullNextDueDate() {
            var vaccine = VaccineTestBuilder.aVaccine().build();

            assertThrows(
                    NullPointerException.class,
                    () -> vaccine.reschedule(null)
            );
        }

        @Test
        void shouldThrowWhenReschedulingToDateEqualToVaccinationDate() {
            var vaccine = VaccineTestBuilder.aVaccine()
                    .withVaccinationDate(TestCommonMother.EARLY_DATE)
                    .build();

            assertThrows(
                    IllegalArgumentException.class,
                    () -> vaccine.reschedule(TestCommonMother.EARLY_DATE)
            );
        }

        @Test
        void shouldThrowWhenReschedulingToDateBeforeVaccinationDate() {
            var vaccine = VaccineTestBuilder.aVaccine()
                    .withVaccinationDate(TestCommonMother.EARLY_DATE)
                    .build();

            var invalidNextDueDate = TestCommonMother.EARLY_DATE.minusDays(1);

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
            var vaccine = VaccineTestBuilder.aVaccine()
                    .withVaccinationDate(TestCommonMother.EARLY_DATE)
                    .withNextDueDate(TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR)
                    .build();

            var today = LocalDate.of(2027, 1, 1);

            var daysRemaining = vaccine.daysRemaining(today);

            assertEquals(243, daysRemaining);
        }

        @Test
        void shouldReturnZeroWhenTodayIsNextDueDate() {
            var vaccine = VaccineTestBuilder.aVaccine()
                    .withNextDueDate(TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR)
                    .build();

            var daysRemaining = vaccine.daysRemaining(TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR.value());

            assertEquals(0, daysRemaining);
        }

        @Test
        void shouldReturnNegativeDaysWhenNextDueDateHasPassed() {
            var vaccine = VaccineTestBuilder.aVaccine()
                    .withVaccinationDate(TestCommonMother.EARLY_DATE)
                    .withNextDueDate(TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR)
                    .build();

            var today = TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR.value().plusDays(9);

            var daysRemaining = vaccine.daysRemaining(today);

            assertEquals(-9, daysRemaining);
        }

        @Test
        void shouldThrowWhenCalculatingDaysRemainingWithNullToday() {
            var vaccine = VaccineTestBuilder.aVaccine().build();

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
            var vaccine = VaccineTestBuilder.aVaccine()
                    .withNextDueDate(TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR)
                    .build();

            var today = TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR.value().minusDays(1);

            var isOverdue = vaccine.isOverdue(today);

            assertFalse(isOverdue);
        }

        @Test
        void shouldNotBeOverdueOnNextDueDate() {
            var vaccine = VaccineTestBuilder.aVaccine()
                    .withNextDueDate(TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR)
                    .build();

            var isOverdue = vaccine.isOverdue(TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR.value());

            assertFalse(isOverdue);
        }

        @Test
        void shouldBeOverdueAfterNextDueDate() {
            var vaccine = VaccineTestBuilder.aVaccine()
                    .withNextDueDate(TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR)
                    .build();

            var today = TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR.value().plusDays(1);

            var isOverdue = vaccine.isOverdue(today);

            assertTrue(isOverdue);
        }

        @Test
        void shouldThrowWhenCheckingOverdueWithNullToday() {
            var vaccine = VaccineTestBuilder.aVaccine().build();

            assertThrows(
                    NullPointerException.class,
                    () -> vaccine.isOverdue(null)
            );
        }

    }

}
