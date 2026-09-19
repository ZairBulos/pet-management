package com.petmanagement.health.infrastructure.adapter.out.persistence.mapper;

import com.petmanagement.health.support.TestPetIdMother;
import com.petmanagement.health.support.TestVaccineMother;
import com.petmanagement.health.support.VaccineJpaEntityTestBuilder;
import com.petmanagement.health.support.VaccineTestBuilder;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class VaccineMapperTest {

    private final VaccineMapper mapper = new VaccineMapper();

    @Nested
    class WhenMappingDomainToJpaEntity {

        @Test
        void shouldMapDomainToJpaEntity() {
            // Given
            var vaccine = VaccineTestBuilder.aVaccine().build();

            // When
            var result = mapper.toJpaEntity(vaccine);

            // Then
            assertNotNull(result);
            assertEquals(vaccine.getId().value(), result.getId());
            assertEquals(vaccine.getPetId().value(), result.getPetId());
            assertEquals(vaccine.getVaccinationDate(), result.getVaccinationDate());
            assertEquals(vaccine.getVaccineName().value(), result.getVaccineName());
            assertEquals(vaccine.getNextDueDate().value(), result.getNextDueDate());
        }

    }

    @Nested
    class WhenMappingJpaEntityToDomain {

        @Test
        void shouldMapJpaEntityToDomain() {
            // Given
            var entity = VaccineJpaEntityTestBuilder.aVaccineJpaEntity().build();

            // When
            var result = mapper.toDomain(entity);

            // Then
            assertNotNull(result);
            assertEquals(TestVaccineMother.DEFAULT_VACCINE_ID, result.getId());
            assertEquals(TestPetIdMother.EXISTING_PET_ID, result.getPetId());
            assertEquals(TestVaccineMother.VACCINE_VACCINATION_DATE, result.getVaccinationDate());
            assertEquals(TestVaccineMother.VACCINE_NAME_RABIES, result.getVaccineName());
            assertEquals(TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR, result.getNextDueDate());
        }

    }

}
