package com.petmanagement.health.infrastructure.adapter.out.persistence.repository;

import com.petmanagement.RepositoryTest;
import com.petmanagement.health.support.TestCommonMother;
import com.petmanagement.health.support.TestPetIdMother;
import com.petmanagement.health.support.TestVaccineMother;
import com.petmanagement.health.support.VaccineJpaEntityTestBuilder;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RepositoryTest
class VaccineJpaRepositoryIntegrationTest {

    @Autowired
    private VaccineJpaRepository repository;

    @Nested
    class WhenFindingVaccinesByPetWithPagination {

        @Test
        void shouldReturnPaginatedVaccinesForPet() {
            // Given
            var petId = TestPetIdMother.EXISTING_PET_ID.value();

            var vaccine1 = VaccineJpaEntityTestBuilder.aVaccineJpaEntity()
                    .withPetId(petId)
                    .withId(TestVaccineMother.DEFAULT_VACCINE_ID.value())
                    .withVaccineName(TestVaccineMother.VACCINE_NAME_RABIES.value())
                    .build();
            var vaccine2 = VaccineJpaEntityTestBuilder.aVaccineJpaEntity()
                    .withPetId(petId)
                    .withId(TestVaccineMother.ANOTHER_VACCINE_ID.value())
                    .withVaccineName(TestVaccineMother.VACCINE_NAME_DISTEMPER.value())
                    .build();
            repository.saveAll(List.of(vaccine1, vaccine2));

            var pageable = PageRequest.of(0, 10);

            // When
            var result = repository.findByPetId(petId, pageable);

            // Then
            assertEquals(2, result.getNumberOfElements());
            assertEquals(1, result.getTotalPages());
            assertEquals(2, result.getTotalElements());
        }

        @Test
        void shouldReturnEmptyPageWhenPetHasNoVaccines() {
            // Given
            var nonExistentPetId = TestPetIdMother.NON_EXISTENT_PET_ID.value();

            var pageable = PageRequest.of(0, 10);

            // When
            var result = repository.findByPetId(nonExistentPetId, pageable);

            // Then
            assertTrue(result.isEmpty());
            assertEquals(0, result.getTotalElements());
        }

        @Test
        void shouldReturnVaccinesOrderedByDateDescending() {
            // Given
            var petId = TestPetIdMother.EXISTING_PET_ID.value();

            var vaccine1 = VaccineJpaEntityTestBuilder.aVaccineJpaEntity()
                    .withPetId(petId)
                    .withId(TestVaccineMother.DEFAULT_VACCINE_ID.value())
                    .withVaccineName(TestVaccineMother.VACCINE_NAME_RABIES.value())
                    .withVaccinationDate(TestCommonMother.RECENT_DATE)
                    .build();
            var vaccine2 = VaccineJpaEntityTestBuilder.aVaccineJpaEntity()
                    .withPetId(petId)
                    .withId(TestVaccineMother.ANOTHER_VACCINE_ID.value())
                    .withVaccineName(TestVaccineMother.VACCINE_NAME_DISTEMPER.value())
                    .withVaccinationDate(TestCommonMother.EARLY_DATE)
                    .build();
            repository.saveAll(List.of(vaccine1, vaccine2));

            var pageable = PageRequest.of(0, 10);

            // When
            var result = repository.findByPetId(petId, pageable);

            // Then
            assertEquals(2, result.getNumberOfElements());
            assertEquals(TestCommonMother.RECENT_DATE, result.getContent().getFirst().getVaccinationDate());
            assertEquals(TestCommonMother.EARLY_DATE, result.getContent().getLast().getVaccinationDate());
        }

    }

}
