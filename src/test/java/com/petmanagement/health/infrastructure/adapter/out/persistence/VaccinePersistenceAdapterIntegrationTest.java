package com.petmanagement.health.infrastructure.adapter.out.persistence;

import com.petmanagement.RepositoryTest;
import com.petmanagement.health.infrastructure.adapter.out.persistence.mapper.VaccineMapper;
import com.petmanagement.health.support.TestCommonMother;
import com.petmanagement.health.support.TestPetIdMother;
import com.petmanagement.health.support.TestVaccineMother;
import com.petmanagement.health.support.VaccineTestBuilder;
import com.petmanagement.shared.domain.model.PageRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RepositoryTest
@Import({
        VaccinePersistenceAdapter.class,
        VaccineMapper.class
})
class VaccinePersistenceAdapterIntegrationTest {

    @Autowired
    private VaccinePersistenceAdapter adapter;

    @Nested
    class WhenFindingVaccineById {

        @Test
        void shouldReturnVaccineWhenExists() {
            // Given
            var vaccine = VaccineTestBuilder.aVaccine().build();
            adapter.save(vaccine);

            // When
            var result = adapter.findById(vaccine.getId());

            // Then
            assertTrue(result.isPresent());
        }

        @Test
        void shouldReturnEmptyWhenVaccineDoesNotExist() {
            // Given
            var nonExistentId = TestVaccineMother.NON_EXISTENT_VACCINE_ID;

            // When
            var result = adapter.findById(nonExistentId);

            // Then
            assertTrue(result.isEmpty());
        }

    }

    @Nested
    class WhenFindingVaccinesByPetWithPagination {

        @Test
        void shouldReturnPaginatedVaccinesForPet() {
            // Given
            var petId = TestPetIdMother.EXISTING_PET_ID;

            var vaccine1 = VaccineTestBuilder.aVaccine()
                    .withPetId(petId)
                    .withVaccineName(TestVaccineMother.VACCINE_NAME_RABIES)
                    .build();
            var vaccine2 = VaccineTestBuilder.aVaccine()
                    .withPetId(petId)
                    .withVaccineName(TestVaccineMother.VACCINE_NAME_DISTEMPER)
                    .build();
            adapter.save(vaccine1);
            adapter.save(vaccine2);

            var pageRequest = PageRequest.of(0, 10);

            // When
            var result = adapter.findByPetId(petId, pageRequest);

            // Then
            assertEquals(2, result.numberOfElements());
            assertEquals(1, result.totalPages());
            assertEquals(2, result.totalElements());
        }

        @Test
        void shouldReturnEmptyPageWhenPetHasNoVaccines() {
            // Given
            var nonExistentPetId = TestPetIdMother.NON_EXISTENT_PET_ID;
            var pageRequest = PageRequest.of(0, 10);

            // When
            var result = adapter.findByPetId(nonExistentPetId, pageRequest);

            // Then
            assertTrue(result.isEmpty());
            assertEquals(0, result.totalElements());
        }

        @Test
        void shouldReturnVaccinesOrderedByDateDescending() {
            // Given
            var petId = TestPetIdMother.EXISTING_PET_ID;

            var vaccine1 = VaccineTestBuilder.aVaccine()
                    .withPetId(petId)
                    .withVaccineName(TestVaccineMother.VACCINE_NAME_RABIES.value())
                    .withVaccinationDate(TestCommonMother.RECENT_DATE)
                    .build();
            var vaccine2 = VaccineTestBuilder.aVaccine()
                    .withPetId(petId)
                    .withVaccineName(TestVaccineMother.VACCINE_NAME_DISTEMPER.value())
                    .withVaccinationDate(TestCommonMother.EARLY_DATE)
                    .build();
            adapter.save(vaccine1);
            adapter.save(vaccine2);

            var pageRequest = PageRequest.of(0, 10);

            // When
            var result = adapter.findByPetId(petId, pageRequest);

            // Then
            assertEquals(2, result.numberOfElements());
            assertEquals(TestCommonMother.RECENT_DATE, result.content().getFirst().getVaccinationDate());
            assertEquals(TestCommonMother.EARLY_DATE, result.content().getLast().getVaccinationDate());
        }

    }

    @Nested
    class WhenSavingVaccine {

        @Test
        void shouldPersistVaccineToDatabase() {
            // Given
            var vaccine = VaccineTestBuilder.aVaccine().build();

            // When
            adapter.save(vaccine);

            // Then
            var result = adapter.findById(vaccine.getId());
            assertTrue(result.isPresent());
        }

        @Test
        void shouldUpdateVaccineWhenSavingExisting() {
            // Given
            var vaccine = VaccineTestBuilder.aVaccine().build();
            adapter.save(vaccine);

            vaccine.update(
                    vaccine.getVaccinationDate(),
                    TestVaccineMother.VACCINE_NAME_DISTEMPER
            );

            // When
            adapter.save(vaccine);

            // Then
            var result = adapter.findById(vaccine.getId());
            assertTrue(result.isPresent());
            assertEquals(TestVaccineMother.VACCINE_NAME_DISTEMPER, result.get().getVaccineName());
        }

    }

}
