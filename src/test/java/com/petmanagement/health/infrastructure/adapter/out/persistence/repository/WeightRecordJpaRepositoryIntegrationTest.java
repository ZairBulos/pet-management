package com.petmanagement.health.infrastructure.adapter.out.persistence.repository;

import com.petmanagement.RepositoryTest;
import com.petmanagement.health.support.TestCommonMother;
import com.petmanagement.health.support.TestPetIdMother;
import com.petmanagement.health.support.TestWeightRecordMother;
import com.petmanagement.health.support.WeightRecordJpaEntityTestBuilder;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RepositoryTest
class WeightRecordJpaRepositoryIntegrationTest {

    @Autowired
    private WeightRecordJpaRepository repository;

    @Nested
    class WhenFindingWeightRecordsByPetWithPagination {

        @Test
        void shouldReturnPaginatedWeightRecordsForPet() {
            // Given
            var petId = TestPetIdMother.EXISTING_PET_ID.value();

            var record1 = WeightRecordJpaEntityTestBuilder.aWeightRecordJpaEntity()
                    .withPetId(petId)
                    .build();
            var record2 = WeightRecordJpaEntityTestBuilder.aWeightRecordJpaEntity()
                    .withId(TestWeightRecordMother.ANOTHER_WEIGHT_RECORD_ID.value())
                    .withPetId(petId)
                    .build();
            repository.saveAll(List.of(record1, record2));

            var pageable = PageRequest.of(0, 10);

            // When
            var result = repository.findByPetId(petId, pageable);

            // Then
            assertEquals(2, result.getNumberOfElements());
            assertEquals(1, result.getTotalPages());
            assertEquals(2, result.getTotalElements());
        }

        @Test
        void shouldReturnEmptyPageWhenPetHasNoWeightRecords() {
            // Given
            var nonExistentPetId = TestPetIdMother.ANOTHER_PET_ID.value();

            var pageable = PageRequest.of(0, 10);

            // When
            var result = repository.findByPetId(nonExistentPetId, pageable);

            // Then
            assertTrue(result.isEmpty());
            assertEquals(0, result.getTotalElements());
        }

        @Test
        void shouldReturnRecordsOrderedByDateDescending() {
            // Given
            var petId = TestPetIdMother.EXISTING_PET_ID.value();

            var record1 = WeightRecordJpaEntityTestBuilder.aWeightRecordJpaEntity()
                    .withWeightDate(TestCommonMother.RECENT_DATE)
                    .withPetId(petId)
                    .build();
            var record2 = WeightRecordJpaEntityTestBuilder.aWeightRecordJpaEntity()
                    .withId(TestWeightRecordMother.ANOTHER_WEIGHT_RECORD_ID.value())
                    .withWeightDate(TestCommonMother.EARLY_DATE)
                    .withPetId(petId)
                    .build();
            repository.save(record1);
            repository.save(record2);

            var pageable = PageRequest.of(0, 10);

            // When
            var result = repository.findByPetId(petId, pageable);

            // Then
            assertEquals(2, result.getNumberOfElements());
            assertEquals(TestCommonMother.RECENT_DATE, result.getContent().getFirst().getWeightDate());
            assertEquals(TestCommonMother.EARLY_DATE, result.getContent().getLast().getWeightDate());
        }

    }

}
