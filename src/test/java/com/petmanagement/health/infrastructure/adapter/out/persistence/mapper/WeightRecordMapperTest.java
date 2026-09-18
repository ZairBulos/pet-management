package com.petmanagement.health.infrastructure.adapter.out.persistence.mapper;

import com.petmanagement.health.support.*;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class WeightRecordMapperTest {

    private final WeightRecordMapper mapper = new WeightRecordMapper();

    @Nested
    class WhenMappingDomainToJpaEntity {

        @Test
        void shouldMapDomainToJpaEntity() {
            // Given
            var weightRecord = WeightRecordTestBuilder.aWeightRecord().build();

            // When
            var result = mapper.toJpaEntity(weightRecord);

            // Then
            assertNotNull(result);
            assertEquals(weightRecord.getId().value(), result.getId());
            assertEquals(weightRecord.getPetId().value(), result.getPetId());
            assertEquals(weightRecord.getWeightDate(), result.getWeightDate());
            assertEquals(weightRecord.getWeight().value(), result.getWeight());
            assertEquals(weightRecord.getCreatedAt(), result.getCreatedAt());
            assertEquals(weightRecord.getUpdatedAt(), result.getUpdatedAt());
        }

    }

    @Nested
    class WhenMappingJpaEntityToDomain {

        @Test
        void shouldMapJpaEntityToDomain() {
            // Given
            var entity = WeightRecordJpaEntityTestBuilder.aWeightRecordJpaEntity().build();

            // When
            var result = mapper.toDomain(entity);

            // Then
            assertNotNull(result);
            assertEquals(TestWeightRecordMother.DEFAULT_WEIGHT_RECORD_ID, result.getId());
            assertEquals(TestPetIdMother.EXISTING_PET_ID, result.getPetId());
            assertEquals(TestCommonMother.EARLY_DATE, result.getWeightDate());
            assertEquals(TestWeightRecordMother.MEDIUM_WEIGHT, result.getWeight());
        }

    }

}
