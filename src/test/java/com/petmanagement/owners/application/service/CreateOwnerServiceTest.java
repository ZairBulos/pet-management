package com.petmanagement.owners.application.service;

import com.petmanagement.owners.domain.exception.OwnerAlreadyExistsException;
import com.petmanagement.owners.support.InMemoryOwnerRepository;
import com.petmanagement.owners.support.OwnerTestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CreateOwnerServiceTest {

    private InMemoryOwnerRepository repository;
    private CreateOwnerService service;

    @BeforeEach
    void setUp() {
        this.repository = new InMemoryOwnerRepository();
        this.service = new CreateOwnerService(repository);
    }

    @Nested
    class WhenCreatingOwner {

        @Test
        void shouldCreateOwner() {
            // Given
            var command = OwnerTestBuilder.CreateOwnerCommandBuilder
                    .aCreateOwnerCommand()
                    .build();

            // When
            var result = service.execute(command);

            // Then
            assertNotNull(result);
        }

    }

    @Nested
    class WhenOwnerAlreadyExists {

        @Test
        void shouldThrowWhenOwnerAlreadyExists() {
            // Given
            var command = OwnerTestBuilder.CreateOwnerCommandBuilder
                    .aCreateOwnerCommand()
                    .build();
            service.execute(command);

            // When/Then
            assertThrows(
                    OwnerAlreadyExistsException.class,
                    () -> service.execute(command)
            );
        }

    }

}
