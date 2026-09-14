package com.petmanagement.owners.application.service;

import com.petmanagement.owners.domain.exception.OwnerAlreadyExistsException;
import com.petmanagement.owners.domain.exception.OwnerNotFoundException;
import com.petmanagement.owners.support.InMemoryOwnerRepository;
import com.petmanagement.owners.support.OwnerTestBuilder;
import com.petmanagement.owners.support.TestOwnerMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdateOwnerServiceTest {

    private InMemoryOwnerRepository repository;
    private UpdateOwnerService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryOwnerRepository();
        service = new UpdateOwnerService(repository);
    }

    @Nested
    class WhenUpdatingOwner {

        @Test
        void shouldUpdateOwner() {
            // Given
            var owner = OwnerTestBuilder.aOwner().build();
            repository.save(owner);

            var command = OwnerTestBuilder.UpdateOwnerCommandBuilder
                    .aUpdateOwnerCommand()
                    .withId(owner.getId())
                    .build();

            // When
            var result = service.execute(command);

            // Then
            assertNotNull(result);
            assertEquals(command.name(), result.getName());
            assertEquals(command.email(), result.getEmail());
            assertEquals(command.phone(), result.getPhone());
        }

    }

    @Nested
    class WhenOwnerDoesNotExist {

        @Test
        void shouldThrowWhenOwnerDoesNotExist() {
            // Given
            var command = OwnerTestBuilder.UpdateOwnerCommandBuilder
                    .aUpdateOwnerCommand()
                    .withId(TestOwnerMother.NON_EXISTING_OWNER_ID)
                    .build();

            // When/Then
            assertThrows(
                    OwnerNotFoundException.class,
                    () -> service.execute(command)
            );
        }

    }

    @Nested
    class WhenEmailIsInUse {

        @Test
        void shouldThrowWhenEmailIsInUse() {
            // Given
            var owner1 = OwnerTestBuilder.aOwner().build();
            var owner2 = OwnerTestBuilder.aOwner()
                    .withName(TestOwnerMother.OWNER_NAME_MARIA)
                    .withEmail(TestOwnerMother.EMAIL_MARIA)
                    .build();

            repository.saveAll(owner1, owner2);
            repository.save(owner2);

            var command = OwnerTestBuilder.UpdateOwnerCommandBuilder
                    .aUpdateOwnerCommand()
                    .withId(owner2.getId())
                    .withEmail(owner1.getEmail())
                    .build();

            // When/Then
            assertThrows(
                    OwnerAlreadyExistsException.class,
                    () -> service.execute(command)
            );
        }

    }

}
