package com.petmanagement.health.infrastructure.adapter.out.persistence;

import com.petmanagement.RepositoryTest;
import com.petmanagement.health.infrastructure.adapter.out.persistence.mapper.WeightRecordMapper;
import com.petmanagement.health.support.TestCommonMother;
import com.petmanagement.health.support.TestPetIdMother;
import com.petmanagement.health.support.TestWeightRecordMother;
import com.petmanagement.health.support.WeightRecordTestBuilder;
import com.petmanagement.shared.domain.model.PageRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RepositoryTest
@Import({
        WeightRecordPersistenceAdapter.class,
        WeightRecordMapper.class
})
class WeightRecordPersistenceAdapterIntegrationTest {

    @Autowired
    private WeightRecordPersistenceAdapter adapter;

    @Nested
    class WhenFindingWeightRecordById {

        @Test
        void shouldReturnWeightRecordWhenExists() {
            // Given
            var weightRecord = WeightRecordTestBuilder.aWeightRecord().build();
            adapter.save(weightRecord);

            // When
            var result = adapter.findById(weightRecord.getId());

            // Then
            assertTrue(result.isPresent());
        }

        @Test
        void shouldReturnEmptyWhenWeightRecordDoesNotExist() {
            // Given
            var nonExistentId = TestWeightRecordMother.NON_EXISTENT_RECORD_ID;

            // When
            var result = adapter.findById(nonExistentId);

            // Then
            assertTrue(result.isEmpty());
        }

    }

    @Nested
    class WhenFindingWeightRecordsByPetWithPagination {

        @Test
        void shouldReturnPaginatedWeightRecordsForPet() {
            // Given
            var petId = TestPetIdMother.EXISTING_PET_ID;

            var record1 = WeightRecordTestBuilder.aWeightRecord()
                    .withPetId(petId)
                    .withWeightDate(TestCommonMother.RECENT_DATE)
                    .build();
            var record2 = WeightRecordTestBuilder.aWeightRecord()
                    .withWeightDate(TestCommonMother.EARLY_DATE)
                    .withPetId(petId)
                    .build();
            adapter.save(record1);
            adapter.save(record2);

            var pageRequest = PageRequest.of(0, 10);

            // When
            var result = adapter.findByPetId(petId, pageRequest);

            // Then
            assertEquals(2, result.numberOfElements());
            assertEquals(1, result.totalPages());
            assertEquals(2, result.totalElements());
            assertTrue(result.isFirst());
            assertTrue(result.isLast());
        }

        @Test
        void shouldReturnEmptyPageWhenPetHasNoWeightRecords() {
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
        void shouldReturnRecordsOrderedByDateDescending() {
            // Given
            var petId = TestPetIdMother.EXISTING_PET_ID;

            var record1 = WeightRecordTestBuilder.aWeightRecord()
                    .withPetId(petId)
                    .withWeightDate(TestCommonMother.EARLY_DATE)
                    .build();
            var record2 = WeightRecordTestBuilder.aWeightRecord()
                    .withPetId(petId)
                    .withWeightDate(TestCommonMother.RECENT_DATE)
                    .build();
            adapter.save(record1);
            adapter.save(record2);

            var pageRequest = PageRequest.of(0, 10);

            // When
            var result = adapter.findByPetId(petId, pageRequest);

            // Then
            assertEquals(2, result.numberOfElements());
            assertEquals(TestCommonMother.RECENT_DATE, result.content().getFirst().getWeightDate());
            assertEquals(TestCommonMother.EARLY_DATE, result.content().getLast().getWeightDate());
        }

    }

    @Nested
    class WhenSavingWeightRecord {

        @Test
        void shouldPersistWeightRecordToDatabase() {
            // Given
            var weightRecord = WeightRecordTestBuilder.aWeightRecord().build();

            // When
            adapter.save(weightRecord);

            // Then
            var result = adapter.findById(weightRecord.getId());
            assertTrue(result.isPresent());
        }

        @Test
        void shouldUpdateWeightRecordWhenSavingExisting() {
            // Given
            var weightRecord = WeightRecordTestBuilder.aWeightRecord().build();
            adapter.save(weightRecord);

            weightRecord.update(
                    weightRecord.getWeightDate(),
                    TestWeightRecordMother.UPDATED_WEIGHT
            );

            // When
            adapter.save(weightRecord);

            // Then
            var result = adapter.findById(weightRecord.getId());
            assertTrue(result.isPresent());
            assertEquals(TestWeightRecordMother.UPDATED_WEIGHT, result.get().getWeight());
        }

    }

}
