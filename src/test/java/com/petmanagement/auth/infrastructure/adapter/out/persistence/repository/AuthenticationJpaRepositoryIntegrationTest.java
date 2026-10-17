package com.petmanagement.auth.infrastructure.adapter.out.persistence.repository;

import com.petmanagement.support.RepositoryTest;
import com.petmanagement.auth.support.AuthenticationJpaEntityTestBuilder;
import com.petmanagement.auth.support.TestAuthenticationMother;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RepositoryTest
class AuthenticationJpaRepositoryIntegrationTest {

    @Autowired
    private AuthenticationJpaRepository repository;

    @Nested
    class WhenFindingActiveByEmail {

        @Test
        void shouldReturnActiveAuthenticationForEmail() {
            // Given
            var entity = AuthenticationJpaEntityTestBuilder.aAuthenticationJpaEntity().build();
            repository.save(entity);

            // When
            var result = repository.findActiveByEmail(entity.getEmail(), Instant.now());

            // Then
            assertTrue(result.isPresent());
            assertEquals(entity.getId(), result.get().getId());
        }

        @Test
        void shouldReturnEmptyWhenEmailHasNoAuthentications() {
            // Given
            var email = TestAuthenticationMother.EMAIL_MARIA.value();

            // When
            var result = repository.findActiveByEmail(email, Instant.now());

            // Then
            assertTrue(result.isEmpty());
        }

        @Test
        void shouldReturnEmptyWhenAuthenticationIsAlreadyUsed() {
            // Given
            var entity = AuthenticationJpaEntityTestBuilder.aAuthenticationJpaEntity()
                    .withAuthenticatedAt(Instant.now())
                    .build();
            repository.save(entity);

            // When
            var result = repository.findActiveByEmail(entity.getEmail(), Instant.now());

            // Then
            assertTrue(result.isEmpty());
        }

        @Test
        void shouldReturnEmptyWhenAuthenticationIsExpired() {
            // Given
            var entity = AuthenticationJpaEntityTestBuilder.aAuthenticationJpaEntity()
                    .withExpiresAt(TestAuthenticationMother.EXPIRED_EXPIRES_AT.value())
                    .build();
            repository.save(entity);

            // When
            var result = repository.findActiveByEmail(entity.getEmail(), Instant.now());

            // Then
            assertTrue(result.isEmpty());
        }

        @Test
        void shouldIgnoreUsedAuthenticationAndReturnActiveOne() {
            // Given
            var used = AuthenticationJpaEntityTestBuilder.aAuthenticationJpaEntity()
                    .withAuthenticatedAt(Instant.now())
                    .build();
            var active = AuthenticationJpaEntityTestBuilder.aAuthenticationJpaEntity()
                    .withId(TestAuthenticationMother.ANOTHER_AUTHENTICATION_ID.value())
                    .build();
            repository.saveAll(List.of(used, active));

            // When
            var result = repository.findActiveByEmail(active.getEmail(), Instant.now());

            // Then
            assertTrue(result.isPresent());
            assertEquals(active.getId(), result.get().getId());
        }

    }

    @Nested
    class WhenDisablingActiveAuthentication {

        @Test
        void shouldMarkActiveAuthenticationAsUsed() {
            // Given
            var entity = AuthenticationJpaEntityTestBuilder.aAuthenticationJpaEntity().build();
            repository.save(entity);

            // When
            repository.disableActiveAuthentication(entity.getEmail(), Instant.now());

            // Then
            var result = repository.findById(entity.getId());
            assertTrue(result.isPresent());
            assertNotNull(result.get().getAuthenticatedAt());
        }

        @Test
        void shouldMakeAuthenticationNoLongerActive() {
            // Given
            var entity = AuthenticationJpaEntityTestBuilder.aAuthenticationJpaEntity().build();
            repository.save(entity);

            // When
            repository.disableActiveAuthentication(entity.getEmail(), Instant.now());

            // Then
            var result = repository.findActiveByEmail(entity.getEmail(), Instant.now());
            assertTrue(result.isEmpty());
        }

        @Test
        void shouldNotModifyAlreadyUsedAuthentication() {
            // Given
            var usedAt = Instant.parse("2024-01-15T10:00:00Z");
            var used = AuthenticationJpaEntityTestBuilder.aAuthenticationJpaEntity()
                    .withAuthenticatedAt(usedAt)
                    .build();
            repository.save(used);

            // When
            repository.disableActiveAuthentication(used.getEmail(), Instant.now());

            // Then
            var result = repository.findById(used.getId());
            assertTrue(result.isPresent());
            assertEquals(usedAt, result.get().getAuthenticatedAt());
        }

    }

}
