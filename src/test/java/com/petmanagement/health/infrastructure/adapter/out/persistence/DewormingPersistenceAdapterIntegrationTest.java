package com.petmanagement.health.infrastructure.adapter.out.persistence;

import com.petmanagement.RepositoryTest;
import com.petmanagement.health.infrastructure.adapter.out.persistence.mapper.DewormingMapper;
import com.petmanagement.health.support.DewormingTestBuilder;
import com.petmanagement.health.support.TestCommonMother;
import com.petmanagement.health.support.TestDewormingMother;
import com.petmanagement.health.support.TestPetIdMother;
import com.petmanagement.shared.domain.model.PageRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RepositoryTest
@Import({
        DewormingPersistenceAdapter.class,
        DewormingMapper.class
})
class DewormingPersistenceAdapterIntegrationTest {

    @Autowired
    private DewormingPersistenceAdapter adapter;

    @Nested
    class WhenFindingDewormingById {

        @Test
        void shouldReturnDewormingWhenExists() {
            // Given
            var deworming = DewormingTestBuilder.aDeworming().build();
            adapter.save(deworming);

            // When
            var result = adapter.findById(deworming.getId());

            // Then
            assertTrue(result.isPresent());
            assertEquals(deworming.getId(), result.get().getId());
        }

        @Test
        void shouldReturnEmptyWhenDewormingDoesNotExist() {
            // Given
            var nonExistentId = TestDewormingMother.NON_EXISTENT_DEWORMING_ID;

            // When
            var result = adapter.findById(nonExistentId);

            // Then
            assertTrue(result.isEmpty());
        }

    }

    @Nested
    class WhenFindingDewormingsByPetWithPagination {

        @Test
        void shouldReturnPaginatedDewormingsForPet() {
            // Given
            var petId = TestPetIdMother.EXISTING_PET_ID;

            var deworming1 = DewormingTestBuilder.aDeworming()
                    .withPetId(petId)
                    .withDrugName(TestDewormingMother.DRUG_NAME_DRONTAL)
                    .build();
            var deworming2 = DewormingTestBuilder.aDeworming()
                    .withPetId(petId)
                    .withDrugName(TestDewormingMother.DRUG_NAME_ADVOCATE)
                    .build();
            adapter.save(deworming1);
            adapter.save(deworming2);

            var pageRequest = PageRequest.of(0, 10);

            // When
            var result = adapter.findByPetId(petId, pageRequest);

            // Then
            assertEquals(2, result.numberOfElements());
            assertEquals(1, result.totalPages());
            assertEquals(2, result.totalElements());
        }

        @Test
        void shouldReturnEmptyPageWhenPetHasNoDewormings() {
            // Given
            var nonExistentPetId = TestPetIdMother.ANOTHER_PET_ID;
            var pageRequest = PageRequest.of(0, 10);

            // When
            var result = adapter.findByPetId(nonExistentPetId, pageRequest);

            // Then
            assertTrue(result.isEmpty());
            assertEquals(0, result.totalElements());
        }

        @Test
        void shouldReturnDewormingsOrderedByDateDescending() {
            // Given
            var petId = TestPetIdMother.EXISTING_PET_ID;

            var deworming1 = DewormingTestBuilder.aDeworming()
                    .withPetId(petId)
                    .withDrugName(TestDewormingMother.DRUG_NAME_DRONTAL.value())
                    .withDewormingDate(TestCommonMother.RECENT_DATE)
                    .build();
            var deworming2 = DewormingTestBuilder.aDeworming()
                    .withPetId(petId)
                    .withDrugName(TestDewormingMother.DRUG_NAME_ADVOCATE.value())
                    .withDewormingDate(TestCommonMother.EARLY_DATE)
                    .build();
            adapter.save(deworming1);
            adapter.save(deworming2);

            var pageRequest = PageRequest.of(0, 10);

            // When
            var result = adapter.findByPetId(petId, pageRequest);

            // Then
            assertEquals(2, result.numberOfElements());
            assertEquals(TestCommonMother.RECENT_DATE, result.content().getFirst().getDewormingDate());
            assertEquals(TestCommonMother.EARLY_DATE, result.content().getLast().getDewormingDate());
        }

    }

    @Nested
    class WhenSavingDeworming {

        @Test
        void shouldPersistDewormingToDatabase() {
            // Given
            var deworming = DewormingTestBuilder.aDeworming().build();

            // When
            adapter.save(deworming);

            // Then
            var result = adapter.findById(deworming.getId());
            assertTrue(result.isPresent());
        }

        @Test
        void shouldUpdateDewormingWhenSavingExisting() {
            // Given
            var deworming = DewormingTestBuilder.aDeworming().build();
            adapter.save(deworming);

            deworming.update(
                    deworming.getDewormingDate(),
                    TestDewormingMother.DRUG_NAME_ADVANTAGE,
                    TestDewormingMother.DRUG_DOSE_SPOT_ON
            );

            // When
            adapter.save(deworming);

            // Then
            var result = adapter.findById(deworming.getId());
            assertTrue(result.isPresent());
            assertEquals(TestDewormingMother.DRUG_NAME_ADVANTAGE, result.get().getDrugName());
            assertEquals(TestDewormingMother.DRUG_DOSE_SPOT_ON, result.get().getDrugDose());
        }

    }

}
