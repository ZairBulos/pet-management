package com.petmanagement.health.infrastructure.adapter.out.persistence.mapper;

import com.petmanagement.health.support.DewormingJpaEntityTestBuilder;
import com.petmanagement.health.support.DewormingTestBuilder;
import com.petmanagement.health.support.TestDewormingMother;
import com.petmanagement.health.support.TestPetIdMother;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DewormingMapperTest {

    private final DewormingMapper mapper = new DewormingMapper();

    @Nested
    class WhenMappingDomainToJpaEntity {

        @Test
        void shouldMapDomainToJpaEntity() {
            // Given
            var deworming = DewormingTestBuilder.aDeworming().build();

            // When
            var result = mapper.toJpaEntity(deworming);

            // Then
            assertNotNull(result);
            assertEquals(deworming.getId().value(), result.getId());
            assertEquals(deworming.getPetId().value(), result.getPetId());
            assertEquals(deworming.getDewormingDate(), result.getDewormingDate());
            assertEquals(deworming.getDrugName().value(), result.getDrugName());
            assertEquals(deworming.getDrugDose().value(), result.getDrugDose());
            assertEquals(deworming.getNextDueDate().value(), result.getNextDueDate());
        }

    }

    @Nested
    class WhenMappingJpaEntityToDomain {

        @Test
        void shouldMapJpaEntityToDomain() {
            // Given
            var entity = DewormingJpaEntityTestBuilder.aDewormingJpaEntity().build();

            // When
            var result = mapper.toDomain(entity);

            // Then
            assertNotNull(result);
            assertEquals(TestDewormingMother.DEFAULT_DEWORMING_ID, result.getId());
            assertEquals(TestPetIdMother.EXISTING_PET_ID, result.getPetId());
            assertEquals(TestDewormingMother.DEWORMING_DATE, result.getDewormingDate());
            assertEquals(TestDewormingMother.DRUG_NAME_DRONTAL, result.getDrugName());
            assertEquals(TestDewormingMother.DRUG_DOSE_TABLET, result.getDrugDose());
            assertEquals(TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS, result.getNextDueDate());
        }

    }

}
