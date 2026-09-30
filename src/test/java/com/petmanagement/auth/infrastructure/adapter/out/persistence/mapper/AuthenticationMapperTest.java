package com.petmanagement.auth.infrastructure.adapter.out.persistence.mapper;

import com.petmanagement.auth.support.AuthenticationJpaEntityTestBuilder;
import com.petmanagement.auth.support.AuthenticationTestBuilder;
import com.petmanagement.auth.support.TestAuthenticationMother;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationMapperTest {

    private final AuthenticationMapper mapper = new AuthenticationMapper();

    @Nested
    class WhenMappingDomainToJpaEntity {

        @Test
        void shouldMapDomainToJpaEntity() {
            // Given
            var authentication = AuthenticationTestBuilder.aAuthentication().build();

            // When
            var result = mapper.toJpaEntity(authentication);

            // Then
            assertNotNull(result);
            assertEquals(authentication.getId().value(), result.getId());
            assertEquals(authentication.getEmail().value(), result.getEmail());
            assertEquals(authentication.getHashedCode().value(), result.getHashedCode());
            assertEquals(authentication.getExpiresAt().value(), result.getExpiresAt());
            assertNull(result.getAuthenticatedAt());
            assertEquals(authentication.getCreatedAt(), result.getCreatedAt());
        }

    }

    @Nested
    class WhenMappingJpaEntityToDomain {

        @Test
        void shouldMapJpaEntityToDomain() {
            // Given
            var entity = AuthenticationJpaEntityTestBuilder.aAuthenticationJpaEntity().build();

            // When
            var result = mapper.toDomain(entity);

            // Then
            assertNotNull(result);
            assertEquals(TestAuthenticationMother.DEFAULT_AUTHENTICATION_ID, result.getId());
            assertEquals(TestAuthenticationMother.EMAIL_JOHN, result.getEmail());
            assertEquals(TestAuthenticationMother.DEFAULT_HASHED_CODE, result.getHashedCode());
            assertEquals(TestAuthenticationMother.DEFAULT_EXPIRES_AT, result.getExpiresAt());
            assertNull(result.getAuthenticatedAt());
            assertEquals(entity.getCreatedAt(), result.getCreatedAt());
        }

    }

}
