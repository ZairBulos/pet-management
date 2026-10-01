package com.petmanagement.auth.infrastructure.adapter.out.persistence;

import com.petmanagement.RepositoryTest;
import com.petmanagement.auth.infrastructure.adapter.out.persistence.mapper.SessionMapper;
import com.petmanagement.auth.support.SessionTestBuilder;
import com.petmanagement.auth.support.TestSessionMother;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@RepositoryTest
@Import({
        SessionPersistenceAdapter.class,
        SessionMapper.class,
})
class SessionPersistenceAdapterIntegrationTest {

    @Autowired
    private SessionPersistenceAdapter adapter;

    @Nested
    class WhenSavingSession {

        @Test
        void shouldPersistSessionToDatabase() {
            // Given
            var session = SessionTestBuilder.aSession().build();

            // When/Then
            assertDoesNotThrow(() -> adapter.save(session));
        }

        @Test
        void shouldUpdateSessionWhenSavingExisting() {
            // Given
            var session = SessionTestBuilder.aSession().build();
            adapter.save(session);

            session.revoke(TestSessionMother.LOGOUT_REASON);

            // When/Then
            assertDoesNotThrow(() -> adapter.save(session));
        }

    }

}
