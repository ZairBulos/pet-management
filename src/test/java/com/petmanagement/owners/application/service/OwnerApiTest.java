package com.petmanagement.owners.application.service;

import com.petmanagement.owners.application.port.out.OwnerRepositoryPort;
import com.petmanagement.owners.support.InMemoryOwnerRepository;
import com.petmanagement.owners.support.TestOwnerMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.petmanagement.owners.support.OwnerTestBuilder.aOwner;
import static org.junit.jupiter.api.Assertions.*;

class OwnerApiTest {

    private OwnerRepositoryPort repository;
    private OwnerApiService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryOwnerRepository();
        service = new OwnerApiService(repository);
    }

    @Nested
    class WhenCheckingOwnerExistence {

        @Test
        void shouldReturnTrueWhenOwnerExists() {
            // Given
            var owner = aOwner().build();
            repository.save(owner);

            var ownerEmail = owner.getEmail().value();

            // When
            var result = service.existsByEmail(ownerEmail);

            // Then
            assertTrue(result);
        }

        @Test
        void shouldReturnFalseWhenOwnerDoesNotExist() {
            // Given
            var ownerEmail = TestOwnerMother.EMAIL_ROBERT.value();

            // When
            var result = service.existsByEmail(ownerEmail);

            // Then
            assertFalse(result);
        }

    }

    @Nested
    class WhenGettingOwnerIdByEmail {

        @Test
        void shouldReturnOwnerIdWhenOwnerExists() {
            // Given
            var owner = aOwner().build();
            repository.save(owner);

            var ownerEmail = owner.getEmail().value();

            // When
            var result = service.getOwnerIdByEmail(ownerEmail);

            // Then
            assertTrue(result.isPresent());
            assertEquals(owner.getId().value(), result.get());
        }

        @Test
        void shouldReturnEmptyOptionalWhenOwnerDoesNotExist() {
            // Given
            var ownerEmail = TestOwnerMother.EMAIL_ROBERT.value();

            // When
            var result = service.getOwnerIdByEmail(ownerEmail);

            // Then
            assertTrue(result.isEmpty());
        }

    }

    @Nested
    class WhenGettingContactById {

        @Test
        void shouldReturnContactWhenOwnerExists() {
            // Given
            var owner = aOwner().build();
            repository.save(owner);

            var ownerId = owner.getId().value();

            // When
            var result = service.getContactById(ownerId);

            // Then
            assertTrue(result.isPresent());

            var contact = result.get();
            assertEquals(owner.getName().value(), contact.name());
            assertEquals(owner.getEmail().value(), contact.email());
            assertEquals(owner.getPhone().value(), contact.phone());
        }

        @Test
        void shouldReturnEmptyOptionalWhenOwnerDoesNotExist() {
            // Given
            var ownerId = TestOwnerMother.NON_EXISTING_OWNER_ID.value();

            // When
            var result = service.getContactById(ownerId);

            // Then
            assertTrue(result.isEmpty());
        }

    }

}
