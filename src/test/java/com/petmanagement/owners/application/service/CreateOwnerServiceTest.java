package com.petmanagement.owners.application.service;

import com.petmanagement.owners.application.port.in.CreateOwnerUseCase;
import com.petmanagement.owners.domain.exception.OwnerAlreadyExistsException;
import com.petmanagement.owners.domain.model.valueobject.Email;
import com.petmanagement.owners.domain.model.valueobject.OwnerName;
import com.petmanagement.owners.domain.model.valueobject.PhoneNumber;
import com.petmanagement.owners.support.InMemoryOwnerRepository;
import org.junit.jupiter.api.BeforeEach;
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

    @Test
    void shouldCreateOwner() {
        // Given
        var command = new CreateOwnerUseCase.CreateOwnerCommand(
                new OwnerName("John Doe"),
                new Email("j.doe@example.com"),
                new PhoneNumber("(832) 631-7251")
        );

        // When
        var ownerId = service.execute(command);

        // Then
        assertNotNull(ownerId);
    }

    @Test
    void shouldThrowWhenOwnerAlreadyExists() {
        // Given
        var command = new CreateOwnerUseCase.CreateOwnerCommand(
                new OwnerName("John Doe"),
                new Email("j.doe@example.com"),
                new PhoneNumber("(832) 631-7251")
        );
        service.execute(command);

        // When/Then
        assertThrows(
                OwnerAlreadyExistsException.class,
                () -> service.execute(command)
        );
    }

}
