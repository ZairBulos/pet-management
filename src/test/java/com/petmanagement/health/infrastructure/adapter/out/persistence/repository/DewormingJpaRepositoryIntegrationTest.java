package com.petmanagement.health.infrastructure.adapter.out.persistence.repository;

import com.petmanagement.RepositoryTest;
import com.petmanagement.health.support.DewormingJpaEntityTestBuilder;
import com.petmanagement.health.support.TestCommonMother;
import com.petmanagement.health.support.TestDewormingMother;
import com.petmanagement.health.support.TestPetIdMother;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RepositoryTest
class DewormingJpaRepositoryIntegrationTest {

    @Autowired
    private DewormingJpaRepository repository;

    @Nested
    class WhenFindingDewormingsByPetWithPagination {

        @Test
        void shouldReturnPaginatedDewormingsForPet() {
            // Given
            var petId = TestPetIdMother.EXISTING_PET_ID.value();

            var deworming1 = DewormingJpaEntityTestBuilder.aDewormingJpaEntity()
                    .withPetId(petId)
                    .withId(TestDewormingMother.DEFAULT_DEWORMING_ID.value())
                    .withDrugName(TestDewormingMother.DRUG_NAME_DRONTAL.value())
                    .build();
            var deworming2 = DewormingJpaEntityTestBuilder.aDewormingJpaEntity()
                    .withPetId(petId)
                    .withId(TestDewormingMother.ANOTHER_DEWORMING_ID.value())
                    .withDrugName(TestDewormingMother.DRUG_NAME_ADVOCATE.value())
                    .build();
            repository.saveAll(List.of(deworming1, deworming2));

            var pageable = PageRequest.of(0, 10);

            // When
            var result = repository.findByPetId(petId, pageable);

            // Then
            assertEquals(2, result.getNumberOfElements());
            assertEquals(1, result.getTotalPages());
            assertEquals(2, result.getTotalElements());
        }

        @Test
        void shouldReturnEmptyPageWhenPetHasNoDewormings() {
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
        void shouldReturnDewormingsOrderedByDateDescending() {
            // Given
            var petId = TestPetIdMother.EXISTING_PET_ID.value();

            var deworming1 = DewormingJpaEntityTestBuilder.aDewormingJpaEntity()
                    .withPetId(petId)
                    .withId(TestDewormingMother.DEFAULT_DEWORMING_ID.value())
                    .withDrugName(TestDewormingMother.DRUG_NAME_DRONTAL.value())
                    .withDewormingDate(TestCommonMother.RECENT_DATE)
                    .build();
            var deworming2 = DewormingJpaEntityTestBuilder.aDewormingJpaEntity()
                    .withPetId(petId)
                    .withId(TestDewormingMother.ANOTHER_DEWORMING_ID.value())
                    .withDrugName(TestDewormingMother.DRUG_NAME_ADVOCATE.value())
                    .withDewormingDate(TestCommonMother.EARLY_DATE)
                    .build();
            repository.saveAll(List.of(deworming1, deworming2));

            var pageable = PageRequest.of(0, 10);

            // When
            var result = repository.findByPetId(petId, pageable);

            // Then
            assertEquals(2, result.getNumberOfElements());
            assertEquals(TestCommonMother.RECENT_DATE, result.getContent().getFirst().getDewormingDate());
            assertEquals(TestCommonMother.EARLY_DATE, result.getContent().getLast().getDewormingDate());
        }

    }

}
