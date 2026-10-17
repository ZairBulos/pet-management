package com.petmanagement.owners.infrastructure.adapter.out.persistence.mapper;

import com.petmanagement.owners.support.OwnerJpaEntityTestBuilder;
import com.petmanagement.owners.support.OwnerTestBuilder;
import com.petmanagement.owners.support.TestOwnerMother;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OwnerMapperTest {

    private final OwnerMapper mapper = new OwnerMapper();

    @Nested
    class WhenMappingDomainToJpaEntity {

        @Test
        void shouldMapDomainToJpaEntity() {
            // Given
            var owner = OwnerTestBuilder.aOwner().build();

            // When
            var result = mapper.toJpaEntity(owner);

            // Then
            assertNotNull(result);
            assertEquals(owner.getId().value(), result.getId());
            assertEquals(owner.getName().value(), result.getName());
            assertEquals(owner.getEmail().value(), result.getEmail());
            assertEquals(owner.getPhone().value(), result.getPhoneNumber());
            assertEquals(owner.getCreatedAt(), result.getCreatedAt());
            assertEquals(owner.getUpdatedAt(), result.getUpdatedAt());
        }

    }

    @Nested
    class WhenMappingJpaEntityToDomain {

        @Test
        void shouldMapJpaEntityToDomain() {
            // Given
            var entity = OwnerJpaEntityTestBuilder.anOwnerJpaEntity().build();

            // When
            var result = mapper.toDomain(entity);

            // Then
            assertNotNull(result);
            assertEquals(TestOwnerMother.DEFAULT_OWNER_ID, result.getId());
            assertEquals(TestOwnerMother.OWNER_NAME_JOHN, result.getName());
            assertEquals(TestOwnerMother.EMAIL_JOHN, result.getEmail());
            assertEquals(TestOwnerMother.PHONE_JOHN, result.getPhone());
            assertNotNull(result.getCreatedAt());
            assertNotNull(result.getUpdatedAt());
        }

    }

}
