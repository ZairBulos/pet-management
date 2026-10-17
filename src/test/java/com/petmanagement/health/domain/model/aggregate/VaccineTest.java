package com.petmanagement.health.domain.model.aggregate;

import com.petmanagement.health.domain.event.VaccineCreated;
import com.petmanagement.health.domain.event.VaccineRescheduled;
import com.petmanagement.health.domain.model.valueobject.VaccineId;
import com.petmanagement.health.support.TestCommonMother;
import com.petmanagement.health.support.TestPetIdMother;
import com.petmanagement.health.support.TestVaccineMother;
import com.petmanagement.health.support.VaccineTestBuilder;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class VaccineTest {

    @Nested
    class Creation {

        @Test
        void shouldCreateVaccine() {
            var vaccine = VaccineTestBuilder.aVaccine().build();

            assertNotNull(vaccine.id());
            assertEquals(TestPetIdMother.EXISTING_PET_ID, vaccine.petId());
            assertEquals(TestVaccineMother.VACCINE_VACCINATION_DATE, vaccine.vaccinationDate());
            assertEquals(TestVaccineMother.VACCINE_NAME_RABIES, vaccine.vaccineName());
            assertEquals(TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR, vaccine.nextDueDate());
            assertNotNull(vaccine.createdAt());
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

            assertNotEquals(vaccine1.id(), vaccine2.id());
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

            var vaccine = Vaccine.reconstitute(
                    id,
                    TestPetIdMother.EXISTING_PET_ID,
                    TestCommonMother.EARLY_DATE,
                    TestVaccineMother.VACCINE_NAME_RABIES,
                    TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR,
                    createdAt
            );

            assertEquals(id, vaccine.id());
            assertEquals(TestPetIdMother.EXISTING_PET_ID, vaccine.petId());
            assertEquals(TestCommonMother.EARLY_DATE, vaccine.vaccinationDate());
            assertEquals(TestVaccineMother.VACCINE_NAME_RABIES, vaccine.vaccineName());
            assertEquals(TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR, vaccine.nextDueDate());
            assertEquals(createdAt, vaccine.createdAt());
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
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullPetId() {
            var id = VaccineId.generate();
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Vaccine.reconstitute(
                            id,
                            null,
                            TestCommonMother.EARLY_DATE,
                            TestVaccineMother.VACCINE_NAME_RABIES,
                            TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullVaccinationDate() {
            var id = VaccineId.generate();
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Vaccine.reconstitute(
                            id,
                            TestPetIdMother.EXISTING_PET_ID,
                            null,
                            TestVaccineMother.VACCINE_NAME_RABIES,
                            TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullVaccineName() {
            var id = VaccineId.generate();
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Vaccine.reconstitute(
                            id,
                            TestPetIdMother.EXISTING_PET_ID,
                            TestCommonMother.EARLY_DATE,
                            null,
                            TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullNextDueDate() {
            var id = VaccineId.generate();
            var now = Instant.now();

            assertThrows(
                    NullPointerException.class,
                    () -> Vaccine.reconstitute(
                            id,
                            TestPetIdMother.EXISTING_PET_ID,
                            TestCommonMother.EARLY_DATE,
                            TestVaccineMother.VACCINE_NAME_RABIES,
                            null,
                            now
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullCreatedAt() {
            var id = VaccineId.generate();

            assertThrows(
                    NullPointerException.class,
                    () -> Vaccine.reconstitute(
                            id,
                            TestPetIdMother.EXISTING_PET_ID,
                            TestCommonMother.EARLY_DATE,
                            TestVaccineMother.VACCINE_NAME_RABIES,
                            TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR,
                            null
                    )
            );
        }

    }

    @Nested
    class Reschedule {

        @Test
        void shouldReturnNewVaccineWithRescheduledNextDueDate() {
            var vaccine = VaccineTestBuilder.aVaccine().build();
            var newNextDueDate = TestCommonMother.FUTURE_DATE;

            var rescheduled = vaccine.rescheduled(newNextDueDate);

            assertEquals(newNextDueDate, rescheduled.nextDueDate().value());
        }

        @Test
        void shouldKeepIdentityAndOtherFieldsWhenRescheduling() {
            var vaccine = VaccineTestBuilder.aVaccine().build();

            var rescheduled = vaccine.rescheduled(TestCommonMother.FUTURE_DATE);

            assertEquals(vaccine.id(), rescheduled.id());
            assertEquals(vaccine.petId(), rescheduled.petId());
            assertEquals(vaccine.vaccinationDate(), rescheduled.vaccinationDate());
            assertEquals(vaccine.vaccineName(), rescheduled.vaccineName());
            assertEquals(vaccine.createdAt(), rescheduled.createdAt());
        }

        @Test
        void shouldPublishVaccineRescheduledEvent() {
            var vaccine = VaccineTestBuilder.aVaccine().build();

            var rescheduled = vaccine.rescheduled(TestCommonMother.FUTURE_DATE);
            var events = rescheduled.pullEvents();

            assertEquals(1, events.size());
            assertInstanceOf(VaccineRescheduled.class, events.getFirst());
        }

        @Test
        void shouldThrowWhenReschedulingWithNullNextDueDate() {
            var vaccine = VaccineTestBuilder.aVaccine().build();

            assertThrows(
                    NullPointerException.class,
                    () -> vaccine.rescheduled(null)
            );
        }

        @Test
        void shouldThrowWhenReschedulingToDateEqualToVaccinationDate() {
            var vaccine = VaccineTestBuilder.aVaccine()
                    .withVaccinationDate(TestCommonMother.EARLY_DATE)
                    .build();

            assertThrows(
                    IllegalArgumentException.class,
                    () -> vaccine.rescheduled(TestCommonMother.EARLY_DATE)
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
                    () -> vaccine.rescheduled(invalidNextDueDate)
            );
        }

    }

    @Nested
    class DaysRemaining {

        @Test
        void shouldReturnDaysRemainingUntilNextDueDate() {
            var vaccine = VaccineTestBuilder.aVaccine()
                    .withVaccinationDate(TestVaccineMother.VACCINE_VACCINATION_DATE)
                    .withNextDueDate(TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR)
                    .build();

            var today = TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR.value().minusDays(243);

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
                    .withVaccinationDate(TestVaccineMother.VACCINE_VACCINATION_DATE)
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
