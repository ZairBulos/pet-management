package com.petmanagement.auth.domain.model.aggregate;

import com.petmanagement.auth.domain.event.AuthenticationCreated;
import com.petmanagement.auth.domain.exception.AuthenticationAlreadyUsedException;
import com.petmanagement.auth.domain.exception.AuthenticationExpiredException;
import com.petmanagement.auth.domain.exception.InvalidAuthenticationCodeException;
import com.petmanagement.auth.domain.model.valueobject.AuthenticationId;
import com.petmanagement.auth.support.AuthenticationTestBuilder;
import com.petmanagement.auth.support.TestAuthenticationMother;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationTest {

    @Nested
    class Creation {

        @Test
        void shouldCreateAuthentication() {
            var authentication = Authentication.create(
                    TestAuthenticationMother.EMAIL_JOHN,
                    TestAuthenticationMother.DEFAULT_HASHER
            );

            assertNotNull(authentication.getId());
            assertEquals(TestAuthenticationMother.EMAIL_JOHN, authentication.getEmail());
            assertNotNull(authentication.getHashedCode());
            assertNotNull(authentication.getExpiresAt());
            assertNull(authentication.getAuthenticatedAt());
            assertNotNull(authentication.getCreatedAt());
        }

        @Test
        void shouldPublishAuthenticationCreatedEvent() {
            var authentication = Authentication.create(
                    TestAuthenticationMother.EMAIL_JOHN,
                    TestAuthenticationMother.DEFAULT_HASHER
            );
            var events = authentication.pullEvents();

            assertEquals(1, events.size());
            assertInstanceOf(AuthenticationCreated.class, events.getFirst());
        }

        @Test
        void shouldGenerateUniqueIdForEachAuthentication() {
            var auth1 = Authentication.create(
                    TestAuthenticationMother.EMAIL_JOHN,
                    TestAuthenticationMother.DEFAULT_HASHER
            );
            var auth2 = Authentication.create(
                    TestAuthenticationMother.EMAIL_JANE,
                    TestAuthenticationMother.DEFAULT_HASHER
            );

            assertNotEquals(auth1.getId(), auth2.getId());
        }

        @Test
        void shouldThrowWhenCreatingWithNullEmail() {
            assertThrows(
                    NullPointerException.class,
                    () -> Authentication.create(null, TestAuthenticationMother.DEFAULT_HASHER)
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullHasher() {
            assertThrows(
                    NullPointerException.class,
                    () -> Authentication.create(TestAuthenticationMother.EMAIL_JOHN, null)
            );
        }

    }

    @Nested
    class Reconstitution {

        @Test
        void shouldReconstituteAuthentication() {
            var id = AuthenticationId.generate();
            var authenticatedAt = Instant.parse("2024-01-15T10:00:00Z");
            var createdAt = Instant.parse("2024-01-15T09:00:00Z");

            var authentication = Authentication.reconstitute(
                    id,
                    TestAuthenticationMother.EMAIL_JANE,
                    TestAuthenticationMother.ANOTHER_HASHED_CODE,
                    TestAuthenticationMother.FUTURE_EXPIRES_AT,
                    authenticatedAt,
                    createdAt
            );

            assertEquals(id, authentication.getId());
            assertEquals(TestAuthenticationMother.EMAIL_JANE, authentication.getEmail());
            assertEquals(TestAuthenticationMother.ANOTHER_HASHED_CODE, authentication.getHashedCode());
            assertEquals(TestAuthenticationMother.FUTURE_EXPIRES_AT.value(), authentication.getExpiresAt().value());
            assertEquals(authenticatedAt, authentication.getAuthenticatedAt());
            assertEquals(createdAt, authentication.getCreatedAt());
        }

        @Test
        void shouldThrowWhenReconstituteWithNullId() {
            assertThrows(
                    NullPointerException.class,
                    () -> Authentication.reconstitute(
                            null,
                            TestAuthenticationMother.EMAIL_JOHN,
                            TestAuthenticationMother.DEFAULT_HASHED_CODE,
                            TestAuthenticationMother.DEFAULT_EXPIRES_AT,
                            null,
                            Instant.now()
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullEmail() {
            assertThrows(
                    NullPointerException.class,
                    () -> Authentication.reconstitute(
                            TestAuthenticationMother.DEFAULT_AUTHENTICATION_ID,
                            null,
                            TestAuthenticationMother.DEFAULT_HASHED_CODE,
                            TestAuthenticationMother.DEFAULT_EXPIRES_AT,
                            null,
                            Instant.now()
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullHashedCode() {
            assertThrows(
                    NullPointerException.class,
                    () -> Authentication.reconstitute(
                            TestAuthenticationMother.DEFAULT_AUTHENTICATION_ID,
                            TestAuthenticationMother.EMAIL_JOHN,
                            null,
                            TestAuthenticationMother.DEFAULT_EXPIRES_AT,
                            null,
                            Instant.now()
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullExpiresAt() {
            assertThrows(
                    NullPointerException.class,
                    () -> Authentication.reconstitute(
                            TestAuthenticationMother.DEFAULT_AUTHENTICATION_ID,
                            TestAuthenticationMother.EMAIL_JOHN,
                            TestAuthenticationMother.DEFAULT_HASHED_CODE,
                            null,
                            null,
                            Instant.now()
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullCreatedAt() {
            assertThrows(
                    NullPointerException.class,
                    () -> Authentication.reconstitute(
                            TestAuthenticationMother.DEFAULT_AUTHENTICATION_ID,
                            TestAuthenticationMother.EMAIL_JOHN,
                            TestAuthenticationMother.DEFAULT_HASHED_CODE,
                            TestAuthenticationMother.DEFAULT_EXPIRES_AT,
                            null,
                            null
                    )
            );
        }

    }

    @Nested
    class Authenticate {

        @Test
        void shouldAuthenticateWithValidCode() {
            var authentication = AuthenticationTestBuilder.aAuthentication().build();
            var code = TestAuthenticationMother.DEFAULT_PLAIN_CODE;

            authentication.authenticate(code, TestAuthenticationMother.DEFAULT_VERIFIER);

            assertTrue(authentication.isAuthenticated());
            assertNotNull(authentication.getAuthenticatedAt());
        }

        @Test
        void shouldThrowWhenAuthenticateAlreadyAuthenticated() {
            var authentication = AuthenticationTestBuilder.aAuthentication()
                    .withAuthenticatedAt(Instant.now())
                    .build();
            var code = TestAuthenticationMother.DEFAULT_PLAIN_CODE;

            assertThrows(
                    AuthenticationAlreadyUsedException.class,
                    () -> authentication.authenticate(code, TestAuthenticationMother.DEFAULT_VERIFIER)
            );
        }

        @Test
        void shouldThrowWhenAuthenticateExpired() {
            var authentication = AuthenticationTestBuilder.aAuthentication()
                    .withExpiresAt(TestAuthenticationMother.EXPIRED_EXPIRES_AT)
                    .build();
            var code = TestAuthenticationMother.DEFAULT_PLAIN_CODE;

            assertThrows(
                    AuthenticationExpiredException.class,
                    () -> authentication.authenticate(code, TestAuthenticationMother.DEFAULT_VERIFIER)
            );
            assertFalse(authentication.isAuthenticated());
        }

        @Test
        void shouldThrowWhenAuthenticateWithInvalidCode() {
            var authentication = AuthenticationTestBuilder.aAuthentication().build();
            var invalidCode = TestAuthenticationMother.INVALID_PLAIN_CODE;

            assertThrows(
                    InvalidAuthenticationCodeException.class,
                    () -> authentication.authenticate(invalidCode, TestAuthenticationMother.DEFAULT_VERIFIER)
            );
            assertFalse(authentication.isAuthenticated());
        }

        @Test
        void shouldThrowWhenAuthenticateWithNullCode() {
            var authentication = AuthenticationTestBuilder.aAuthentication().build();

            assertThrows(
                    NullPointerException.class,
                    () -> authentication.authenticate(null, TestAuthenticationMother.DEFAULT_VERIFIER)
            );
        }

        @Test
        void shouldThrowWhenAuthenticateWithNullVerifier() {
            var authentication = AuthenticationTestBuilder.aAuthentication().build();
            var code = TestAuthenticationMother.DEFAULT_PLAIN_CODE;

            assertThrows(
                    NullPointerException.class,
                    () -> authentication.authenticate(code, null)
            );
        }

    }

}
