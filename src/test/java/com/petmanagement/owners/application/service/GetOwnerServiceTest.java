package com.petmanagement.owners.application.service;

import com.petmanagement.owners.application.port.in.GetOwnerUseCase;
import com.petmanagement.owners.domain.exception.OwnerNotFoundException;
import com.petmanagement.owners.domain.model.aggregate.Owner;
import com.petmanagement.owners.domain.model.valueobject.Email;
import com.petmanagement.owners.domain.model.valueobject.OwnerId;
import com.petmanagement.owners.domain.model.valueobject.OwnerName;
import com.petmanagement.owners.domain.model.valueobject.PhoneNumber;
import com.petmanagement.owners.support.InMemoryOwnerRepository;
import org.junit.jupiter.api.BeforeEach;
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

    @Test
    void shouldGetExistingOwner() {
        // Given
        var owner = Owner.create(
                new OwnerName("John Doe"),
                new Email("john@example.com"),
                new PhoneNumber("(832) 631-7251")
        );
        repository.save(owner);

        var command = new GetOwnerUseCase.GetOwnerCommand(owner.getId());

        // When
        var result = service.execute(command);

        // Then
        assertNotNull(result);
        assertEquals(owner.getId(), result.getId());
    }

    @Test
    void shouldThrowWhenOwnerNotFound() {
        // Given
        var command = new GetOwnerUseCase.GetOwnerCommand(
                OwnerId.of("5182d167-6eb3-490f-b4e8-d4144ad5e73e")
        );

        // When/Then
        assertThrows(
                OwnerNotFoundException.class,
                () -> service.execute(command)
        );
    }

}
