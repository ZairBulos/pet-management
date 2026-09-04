package com.petmanagement.owners.application.service;

import com.petmanagement.owners.application.port.in.UpdateOwnerUseCase;
import com.petmanagement.owners.domain.exception.OwnerAlreadyExistsException;
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

class UpdateOwnerServiceTest {

    private InMemoryOwnerRepository repository;
    private UpdateOwnerService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryOwnerRepository();
        service = new UpdateOwnerService(repository);
    }

    @Test
    void shouldUpdateOwner() {
        // Given
        var owner = Owner.create(
                new OwnerName("John Doe"),
                new Email("john@example.com"),
                new PhoneNumber("(678) 228-9897")
        );
        repository.save(owner);

        var command = new UpdateOwnerUseCase.UpdateOwnerCommand(
                owner.getId(),
                new OwnerName("Joel Yusuf"),
                new Email("joel@example.com"),
                new PhoneNumber("(919) 694-5891")
        );

        // When
        var result = service.execute(command);

        // Then
        assertNotNull(result);
        assertEquals(command.name(), result.getName());
        assertEquals(command.email(), result.getEmail());
        assertEquals(command.phone(), result.getPhone());
    }

    @Test
    void shouldThrowWhenOwnerNotFound() {
        // Given
        var command = new UpdateOwnerUseCase.UpdateOwnerCommand(
                OwnerId.of("381ea6ca-4e46-40e1-a926-b83afed1b40b"),
                new OwnerName("Norma Leon"),
                new Email("norma@example.com"),
                new PhoneNumber("(980) 958-2622")
        );

        // When/Then
        assertThrows(
                OwnerNotFoundException.class,
                () -> service.execute(command)
        );
    }

    @Test
    void shouldThrowWhenEmailIsInUse() {
        // Given
        var owner1 = Owner.create(
                new OwnerName("John Doe"),
                new Email("j.doe@example.com"),
                new PhoneNumber("(908) 718-9460")
        );
        var owner2 = Owner.create(
                new OwnerName("Jane Doe"),
                new Email("jane.doe@example.com"),
                new PhoneNumber("(980) 567-3844")
        );
        repository.save(owner1);
        repository.save(owner2);

        var command = new UpdateOwnerUseCase.UpdateOwnerCommand(
                owner2.getId(),
                new OwnerName("Jane Doe"),
                new Email("j.doe@example.com"),
                new PhoneNumber("(980) 567-3844")
        );

        // When/Then
        assertThrows(
                OwnerAlreadyExistsException.class,
                () -> service.execute(command)
        );
    }

}
