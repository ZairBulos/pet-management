package com.petmanagement.owners.application.service;

import com.petmanagement.owners.application.port.in.GetOwnerUseCase;
import com.petmanagement.owners.domain.exception.OwnerNotFoundException;
import com.petmanagement.owners.support.InMemoryOwnerRepository;
import com.petmanagement.owners.support.OwnerTestBuilder;
import com.petmanagement.owners.support.TestOwnerMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GetOwnerServiceTest {

    private InMemoryOwnerRepository repository;
    private GetOwnerService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryOwnerRepository();
        service = new GetOwnerService(repository);
    }

    @Nested
    class WhenRetrievingExistingOwner {

        @Test
        void shouldGetExistingOwner() {
            // Given
            var owner = OwnerTestBuilder.aOwner().build();
            repository.save(owner);

            var command = new GetOwnerUseCase.GetOwnerCommand(owner.getId());

            // When
            var result = service.execute(command);

            // Then
            assertNotNull(result);
            assertEquals(owner.getId(), result.getId());
            assertEquals(owner.getName(), result.getName());
        }

    }

    @Nested
    class WhenOwnerDoesNotExist {

        @Test
        void shouldThrowWhenOwnerDoesNotExist() {
            // Given
            var command = new GetOwnerUseCase.GetOwnerCommand(
                    TestOwnerMother.NON_EXISTING_OWNER_ID
            );

            // When/Then
            assertThrows(
                    OwnerNotFoundException.class,
                    () -> service.execute(command)
            );
        }

    }

}
