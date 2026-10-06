package com.petmanagement.auth.infrastructure.adapter.out.persistence;

import com.petmanagement.support.RepositoryTest;
import com.petmanagement.auth.infrastructure.adapter.out.persistence.mapper.AuthenticationMapper;
import com.petmanagement.auth.support.AuthenticationTestBuilder;
import com.petmanagement.auth.support.TestAuthenticationMother;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@RepositoryTest
@Import({
        AuthenticationPersistenceAdapter.class,
        AuthenticationMapper.class,
})
public class AuthenticationPersistenceAdapterIntegrationTest {

    @Autowired
    private AuthenticationPersistenceAdapter adapter;

    @Nested
    class WhenFindingActiveAuthenticationByEmail {

        @Test
        void shouldReturnActiveAuthenticationWhenExists() {
            // Given
            var authentication = AuthenticationTestBuilder.aAuthentication().build();
            adapter.save(authentication);

            // When
            var result = adapter.findActiveByEmail(authentication.getEmail());

            // Then
            assertTrue(result.isPresent());
            assertEquals(authentication.getId(), result.get().getId());
        }

        @Test
        void shouldReturnEmptyWhenThereIsNoActiveAuthentication() {
            // Given
            var email = TestAuthenticationMother.EMAIL_MARIA;

            // When
            var result = adapter.findActiveByEmail(email);

            // Then
            assertTrue(result.isEmpty());
        }

        @Test
        void shouldReturnEmptyWhenAuthenticationIsAlreadyUsed() {
            // Given
            var authentication = AuthenticationTestBuilder.aAuthentication()
                    .withAuthenticatedAt(Instant.now())
                    .build();
            adapter.save(authentication);

            // When
            var result = adapter.findActiveByEmail(authentication.getEmail());

            // Then
            assertTrue(result.isEmpty());
        }

        @Test
        void shouldReturnEmptyWhenAuthenticationIsExpired() {
            // Given
            var authentication = AuthenticationTestBuilder.aAuthentication()
                    .withExpiresAt(TestAuthenticationMother.EXPIRED_EXPIRES_AT)
                    .build();
            adapter.save(authentication);

            // When
            var result = adapter.findActiveByEmail(authentication.getEmail());

            // Then
            assertTrue(result.isEmpty());
        }

    }

    @Nested
    class WhenSavingAuthentication {

        @Test
        void shouldPersistAuthenticationToDatabase() {
            // Given
            var authentication = AuthenticationTestBuilder.aAuthentication().build();

            // When
            adapter.save(authentication);

            // Then
            var result = adapter.findActiveByEmail(authentication.getEmail());
            assertTrue(result.isPresent());
            assertEquals(authentication.getHashedCode(), result.get().getHashedCode());
        }

        @Test
        void shouldUpdateAuthenticationWhenSavingExisting() {
            // Given
            var authentication = AuthenticationTestBuilder.aAuthentication().build();
            adapter.save(authentication);

            authentication.authenticate(
                    TestAuthenticationMother.DEFAULT_PLAIN_CODE,
                    TestAuthenticationMother.DEFAULT_VERIFIER
            );

            // When
            adapter.save(authentication);

            // Then
            var result = adapter.findActiveByEmail(authentication.getEmail());
            assertTrue(result.isEmpty());
        }

    }

    @Nested
    class WhenReplacingActiveAuthentication {

        @Test
        void shouldSaveNewAuthenticationWhenThereIsNoActiveOne() {
            // Given
            var authentication = AuthenticationTestBuilder.aAuthentication().build();

            // When
            adapter.replaceActiveAuthentication(authentication);

            // Then
            var result = adapter.findActiveByEmail(authentication.getEmail());
            assertTrue(result.isPresent());
            assertEquals(authentication.getId(), result.get().getId());
        }

        @Test
        void shouldReplacePreviousActiveAuthenticationWithNewOne() {
            // Given
            var oldAuthentication = AuthenticationTestBuilder.aAuthentication().build();
            adapter.save(oldAuthentication);

            var newAuthentication = AuthenticationTestBuilder.aAuthentication()
                    .withId(TestAuthenticationMother.ANOTHER_AUTHENTICATION_ID)
                    .build();

            // When
            adapter.replaceActiveAuthentication(newAuthentication);

            // Then
            var result = adapter.findActiveByEmail(newAuthentication.getEmail());
            assertTrue(result.isPresent());
            assertEquals(newAuthentication.getId(), result.get().getId());
            assertNotEquals(oldAuthentication.getId(), result.get().getId());
        }

        @Test
        void shouldNotAffectActiveAuthenticationOfAnotherEmail() {
            // Given
            var other = AuthenticationTestBuilder.aAuthentication()
                    .withEmail(TestAuthenticationMother.EMAIL_JANE)
                    .build();
            adapter.save(other);

            var newAuthentication = AuthenticationTestBuilder.aAuthentication()
                    .withId(TestAuthenticationMother.ANOTHER_AUTHENTICATION_ID)
                    .withEmail(TestAuthenticationMother.EMAIL_JOHN)
                    .build();

            // When
            adapter.replaceActiveAuthentication(newAuthentication);

            // Then
            var result = adapter.findActiveByEmail(other.getEmail());
            assertTrue(result.isPresent());
            assertEquals(other.getId(), result.get().getId());
        }

    }

}
