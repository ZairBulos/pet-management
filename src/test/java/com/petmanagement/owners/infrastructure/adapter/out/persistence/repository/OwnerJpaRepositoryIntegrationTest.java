package com.petmanagement.owners.infrastructure.adapter.out.persistence.repository;

import com.petmanagement.RepositoryTest;
import com.petmanagement.owners.support.OwnerJpaEntityTestBuilder;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RepositoryTest
class OwnerJpaRepositoryIntegrationTest {

    @Autowired
    private OwnerJpaRepository repository;

    @Nested
    class WhenSearchingByEmail {

        @Test
        void shouldFindOwnerByEmail() {
            // Given
            var entity = OwnerJpaEntityTestBuilder.anOwnerJpaEntity().build();
            repository.save(entity);

            // When
            var result = repository.findByEmail(entity.getEmail());

            // Then
            assertTrue(result.isPresent());
        }

        @Test
        void shouldNotFindOwnerByNonExistentEmail() {
            // Given
            var email = "notfound@example.com";

            // When
            var result = repository.findByEmail(email);

            // Then
            assertTrue(result.isEmpty());
        }

    }

}
