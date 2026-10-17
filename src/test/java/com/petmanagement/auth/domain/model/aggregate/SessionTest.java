package com.petmanagement.auth.domain.model.aggregate;

import com.petmanagement.auth.domain.event.SessionCreated;
import com.petmanagement.auth.domain.event.SessionRevoked;
import com.petmanagement.auth.domain.exception.SessionExpiredException;
import com.petmanagement.auth.domain.exception.SessionReuseDetectedException;
import com.petmanagement.auth.domain.exception.SessionRevokedException;
import com.petmanagement.auth.domain.model.enums.SessionRevocationReason;
import com.petmanagement.auth.domain.model.valueobject.SessionId;
import com.petmanagement.auth.support.SessionTestBuilder;
import com.petmanagement.auth.support.TestSessionMother;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class SessionTest {

    @Nested
    class Creation {

        @Test
        void shouldCreateSession() {
            var session = Session.create(
                    TestSessionMother.EXISTING_OWNER_ID,
                    TestSessionMother.DEFAULT_PLAIN_REFRESH_TOKEN
            );

            assertNotNull(session.getId());
            assertEquals(TestSessionMother.EXISTING_OWNER_ID, session.getOwnerId());
            assertEquals(
                    TestSessionMother.DEFAULT_HASHED_REFRESH_TOKEN,
                    session.getHashedRefreshToken()
            );
            assertNotNull(session.getExpiresAt());
            assertNull(session.getRevokedAt());
            assertNull(session.getRevocationReason());
            assertNotNull(session.getCreatedAt());
        }

        @Test
        void shouldCreateActiveSession() {
            var session = Session.create(
                    TestSessionMother.EXISTING_OWNER_ID,
                    TestSessionMother.DEFAULT_PLAIN_REFRESH_TOKEN
            );

            assertTrue(session.isActive(Instant.now()));
            assertFalse(session.isRevoked());
            assertFalse(session.isExpired(Instant.now()));
        }

        @Test
        void shouldPublishSessionCreatedEvent() {
            var session = Session.create(
                    TestSessionMother.EXISTING_OWNER_ID,
                    TestSessionMother.DEFAULT_PLAIN_REFRESH_TOKEN
            );
            var events = session.pullEvents();

            assertEquals(1, events.size());
            assertInstanceOf(SessionCreated.class, events.getFirst());
        }

        @Test
        void shouldGenerateUniqueIdForEachSession() {
            var session1 = Session.create(
                    TestSessionMother.EXISTING_OWNER_ID,
                    TestSessionMother.DEFAULT_PLAIN_REFRESH_TOKEN
            );
            var session2 = Session.create(
                    TestSessionMother.EXISTING_OWNER_ID,
                    TestSessionMother.ANOTHER_PLAIN_REFRESH_TOKEN
            );

            assertNotEquals(session1.getId(), session2.getId());
        }

        @Test
        void shouldThrowWhenCreatingWithNullOwnerId() {
            assertThrows(
                    NullPointerException.class,
                    () -> Session.create(
                            null,
                            TestSessionMother.DEFAULT_PLAIN_REFRESH_TOKEN
                    )
            );
        }

        @Test
        void shouldThrowWhenCreatingWithNullRefreshToken() {
            assertThrows(
                    NullPointerException.class,
                    () -> Session.create(
                            TestSessionMother.EXISTING_OWNER_ID,
                            null
                    )
            );
        }

    }

    @Nested
    class Reconstitution {

        @Test
        void shouldReconstituteSession() {
            var id = SessionId.generate();
            var revokedAt = Instant.parse("2024-01-15T10:00:00Z");
            var createdAt = Instant.parse("2024-01-15T09:00:00Z");

            var session = Session.reconstitute(
                    id,
                    TestSessionMother.ANOTHER_OWNER_ID,
                    TestSessionMother.ANOTHER_HASHED_REFRESH_TOKEN,
                    TestSessionMother.FUTURE_EXPIRES_AT,
                    revokedAt,
                    TestSessionMother.LOGOUT_REASON,
                    createdAt
            );

            assertEquals(id, session.getId());
            assertEquals(TestSessionMother.ANOTHER_OWNER_ID, session.getOwnerId());
            assertEquals(TestSessionMother.ANOTHER_HASHED_REFRESH_TOKEN, session.getHashedRefreshToken());
            assertEquals(TestSessionMother.FUTURE_EXPIRES_AT.value(), session.getExpiresAt().value());
            assertEquals(revokedAt, session.getRevokedAt());
            assertEquals(TestSessionMother.LOGOUT_REASON, session.getRevocationReason());
            assertEquals(createdAt, session.getCreatedAt());
        }

        @Test
        void shouldThrowWhenReconstituteWithNullId() {
            assertThrows(
                    NullPointerException.class,
                    () -> Session.reconstitute(
                            null,
                            TestSessionMother.EXISTING_OWNER_ID,
                            TestSessionMother.DEFAULT_HASHED_REFRESH_TOKEN,
                            TestSessionMother.DEFAULT_EXPIRES_AT,
                            null,
                            null,
                            Instant.now()
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullOwnerId() {
            assertThrows(
                    NullPointerException.class,
                    () -> Session.reconstitute(
                            TestSessionMother.DEFAULT_SESSION_ID,
                            null,
                            TestSessionMother.DEFAULT_HASHED_REFRESH_TOKEN,
                            TestSessionMother.DEFAULT_EXPIRES_AT,
                            null,
                            null,
                            Instant.now()
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullHashedRefreshToken() {
            assertThrows(
                    NullPointerException.class,
                    () -> Session.reconstitute(
                            TestSessionMother.DEFAULT_SESSION_ID,
                            TestSessionMother.EXISTING_OWNER_ID,
                            null,
                            TestSessionMother.DEFAULT_EXPIRES_AT,
                            null,
                            null,
                            Instant.now()
                    )
            );
        }

        @Test
        void shouldThrowWhenReconstituteWithNullExpiresAt() {
            assertThrows(
                    NullPointerException.class,
                    () -> Session.reconstitute(
                            TestSessionMother.DEFAULT_SESSION_ID,
                            TestSessionMother.EXISTING_OWNER_ID,
                            TestSessionMother.DEFAULT_HASHED_REFRESH_TOKEN,
                            null,
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
                    () -> Session.reconstitute(
                            TestSessionMother.DEFAULT_SESSION_ID,
                            TestSessionMother.EXISTING_OWNER_ID,
                            TestSessionMother.DEFAULT_HASHED_REFRESH_TOKEN,
                            TestSessionMother.DEFAULT_EXPIRES_AT,
                            null,
                            null,
                            null
                    )
            );
        }

    }

    @Nested
    class Rotate {

        @Test
        void shouldRotateSession() {
            // Given
            var session = SessionTestBuilder.aSession().build();

            // When
            var newSession = session.rotate(TestSessionMother.ANOTHER_PLAIN_REFRESH_TOKEN);

            // Then
            assertNotNull(newSession);
            assertNotEquals(session.getId(), newSession.getId());
            assertEquals(session.getOwnerId(), newSession.getOwnerId());
            assertEquals(
                    TestSessionMother.ANOTHER_HASHED_REFRESH_TOKEN,
                    newSession.getHashedRefreshToken()
            );
            assertTrue(newSession.isActive(Instant.now()));
        }

        @Test
        void shouldRevokeCurrentSessionWithRotatedReason() {
            // Given
            var session = SessionTestBuilder.aSession().build();

            // When
            session.rotate(TestSessionMother.ANOTHER_PLAIN_REFRESH_TOKEN);

            // Then
            assertTrue(session.isRevoked());
            assertNotNull(session.getRevokedAt());
            assertEquals(SessionRevocationReason.ROTATED, session.getRevocationReason());
        }

        @Test
        void shouldKeepNewSessionUnrevoked() {
            // Given
            var session = SessionTestBuilder.aSession().build();

            // When
            var newSession = session.rotate(TestSessionMother.ANOTHER_PLAIN_REFRESH_TOKEN);

            // Then
            assertFalse(newSession.isRevoked());
            assertNull(newSession.getRevokedAt());
            assertNull(newSession.getRevocationReason());
        }

        @Test
        void shouldPublishEventsOnRotate() {
            // Given
            var session = SessionTestBuilder.aSession().build();

            // When
            var newSession = session.rotate(TestSessionMother.ANOTHER_PLAIN_REFRESH_TOKEN);

            // Then
            var oldEvents = session.pullEvents();
            var newEvents = newSession.pullEvents();

            assertEquals(1, oldEvents.size());
            assertInstanceOf(SessionRevoked.class, oldEvents.getFirst());
            assertEquals(1, newEvents.size());
            assertInstanceOf(SessionCreated.class, newEvents.getFirst());
        }

        @Test
        void shouldThrowReuseDetectedWhenSessionAlreadyRotated() {
            // Given
            var session = SessionTestBuilder.aSession()
                    .withRevokedAt(Instant.now())
                    .withRevocationReason(SessionRevocationReason.ROTATED)
                    .build();

            // When/Then
            assertThrows(
                    SessionReuseDetectedException.class,
                    () -> session.rotate(TestSessionMother.ANOTHER_PLAIN_REFRESH_TOKEN)
            );
        }

        @Test
        void shouldThrowRevokedWhenSessionRevokedByLogout() {
            // Given
            var session = SessionTestBuilder.aSession()
                    .withRevokedAt(Instant.now())
                    .withRevocationReason(SessionRevocationReason.LOGOUT)
                    .build();

            // When/Then
            assertThrows(
                    SessionRevokedException.class,
                    () -> session.rotate(TestSessionMother.ANOTHER_PLAIN_REFRESH_TOKEN)
            );
        }

        @Test
        void shouldThrowRevokedWhenSessionRevokedByReuseDetection() {
            // Given
            var session = SessionTestBuilder.aSession()
                    .withRevokedAt(Instant.now())
                    .withRevocationReason(SessionRevocationReason.REUSE_DETECTED)
                    .build();

            // When/Then
            assertThrows(
                    SessionRevokedException.class,
                    () -> session.rotate(TestSessionMother.ANOTHER_PLAIN_REFRESH_TOKEN)
            );
        }

        @Test
        void shouldThrowWhenRotateExpiredSession() {
            // Given
            var session = SessionTestBuilder.aSession()
                    .withExpiresAt(TestSessionMother.EXPIRED_EXPIRES_AT)
                    .build();

            // When/Then
            assertThrows(
                    SessionExpiredException.class,
                    () -> session.rotate(TestSessionMother.ANOTHER_PLAIN_REFRESH_TOKEN)
            );
            assertFalse(session.isRevoked());
        }

        @Test
        void shouldThrowWhenRotateWithNullRefreshToken() {
            // Given
            var session = SessionTestBuilder.aSession().build();

            // When/Then
            assertThrows(
                    NullPointerException.class,
                    () -> session.rotate(null)
            );
        }

    }

    @Nested
    class Revoke {

        @Test
        void shouldRevokeSession() {
            // Given
            var session = SessionTestBuilder.aSession().build();

            // When
            session.revoke(SessionRevocationReason.LOGOUT);

            // Then
            assertTrue(session.isRevoked());
            assertNotNull(session.getRevokedAt());
            assertEquals(SessionRevocationReason.LOGOUT, session.getRevocationReason());
        }

        @Test
        void shouldPublishSessionRevokedEvent() {
            // Given
            var session = SessionTestBuilder.aSession().build();

            // Then
            session.revoke(SessionRevocationReason.LOGOUT);
            var events = session.pullEvents();

            // Then
            assertEquals(1, events.size());
            assertInstanceOf(SessionRevoked.class, events.getFirst());
        }

        @Test
        void shouldBeIdempotentWhenAlreadyRevoked() {
            // Given
            var revokedAt = Instant.parse("2024-01-15T10:00:00Z");
            var session = SessionTestBuilder.aSession()
                    .withRevokedAt(revokedAt)
                    .withRevocationReason(SessionRevocationReason.LOGOUT)
                    .build();

            // Then
            session.revoke(SessionRevocationReason.REUSE_DETECTED);

            // Then
            assertEquals(revokedAt, session.getRevokedAt());
            assertEquals(SessionRevocationReason.LOGOUT, session.getRevocationReason());
            assertTrue(session.pullEvents().isEmpty());
        }

        @Test
        void shouldMakeSessionInactive() {
            // Given
            var session = SessionTestBuilder.aSession().build();

            // Then
            session.revoke(SessionRevocationReason.LOGOUT);

            // Then
            assertFalse(session.isActive(Instant.now()));
        }

    }

}
