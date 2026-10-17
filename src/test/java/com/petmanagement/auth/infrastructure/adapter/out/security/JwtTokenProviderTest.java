package com.petmanagement.auth.infrastructure.adapter.out.security;

import com.petmanagement.auth.infrastructure.config.JwtProperties;
import com.petmanagement.auth.support.TestSessionMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtTokenProviderTest {

    private static final String SECRET = "UVzp+I3t0PMOLjar7IhpTCCOflxyx4cTNBxNOu5Xry4=";
    private static final String ISSUER = "pet-management-test";
    private static final String AUDIENCE = "pet-management-test-api";
    private static final Duration ACCESS_TOKEN_TTL = Duration.ofMinutes(15);

    private static final Instant NOW = Instant.parse("2026-10-01T12:00:00Z");

    private JwtProperties properties;
    private JwtTokenProvider provider;

    @BeforeEach
    void setUp() {
        properties = new JwtProperties(SECRET, ISSUER, AUDIENCE, ACCESS_TOKEN_TTL);

        provider = new JwtTokenProvider(properties, Clock.fixed(NOW, ZoneOffset.UTC));
    }

    @Nested
    class WhenGeneratingTokens {

        @Test
        void shouldGenerateAuthTokens() {
            // Given
            var ownerId = TestSessionMother.EXISTING_OWNER_ID;

            // When
            var tokens = provider.generate(ownerId);
            System.out.println(tokens);

            // Then
            assertThat(tokens).isNotNull();
            assertThat(tokens.accessToken()).isNotNull();
            assertThat(tokens.refreshToken()).isNotNull();
        }

        @Test
        void shouldGenerateAccessToken() {
            // Given
            var ownerId = TestSessionMother.EXISTING_OWNER_ID;

            // When
            var accessToken = provider.generate(ownerId).accessToken();

            // Then
            assertThat(accessToken)
                    .isNotBlank()
                    .containsPattern("^[^.]+\\.[^.]+\\.[^.]+$");
        }

        @Test
        void shouldGenerateRefreshToken() {
            // Given
            var ownerId = TestSessionMother.EXISTING_OWNER_ID;

            // When
            var refreshToken = provider.generate(ownerId).refreshToken();

            // Then
            assertThat(refreshToken).isNotBlank();
        }

        @Test
        void shouldThrowWhenOwnerIdIsNull() {
            // When/Then
            assertThatThrownBy(() -> provider.generate(null))
                    .isInstanceOf(NullPointerException.class);
        }

    }

    @Nested
    class WhenVerifyingAccessToken {

        @Test
        void shouldReturnOwnerId() {
            // Given
            var tokens = provider.generate(TestSessionMother.EXISTING_OWNER_ID);

            // When
            var result = provider.verifyAccessToken(tokens.accessToken());

            // Then
            assertThat(result)
                    .isPresent()
                    .contains(TestSessionMother.EXISTING_OWNER_ID);
        }

        @Test
        void shouldReturnEmptyWhenTokenIsExpired() {
            // Given
            var tokens = provider.generate(TestSessionMother.EXISTING_OWNER_ID);
            var laterClock = Clock.fixed(NOW.plus(ACCESS_TOKEN_TTL).plusSeconds(1), ZoneOffset.UTC);
            var laterProvider = new JwtTokenProvider(properties, laterClock);

            // When
            var result = laterProvider.verifyAccessToken(tokens.accessToken());

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        void shouldReturnEmptyWhenTokenIsNotYetValid() {
            // Given
            var tokens = provider.generate(TestSessionMother.EXISTING_OWNER_ID);
            var earlierClock = Clock.fixed(NOW.minusSeconds(60), ZoneOffset.UTC);
            var earlierProvider = new JwtTokenProvider(properties, earlierClock);

            // When
            var result = earlierProvider.verifyAccessToken(tokens.accessToken());

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        void shouldReturnEmptyWhenSignatureIsInvalid() {
            // Given
            var otherProperties = new JwtProperties(
                    "another-secret-with-at-least-32-characters", ISSUER, AUDIENCE, ACCESS_TOKEN_TTL
            );
            var otherProvider = new JwtTokenProvider(otherProperties, Clock.fixed(NOW, ZoneOffset.UTC));
            var tokens = otherProvider.generate(TestSessionMother.EXISTING_OWNER_ID);

            // When
            var result = provider.verifyAccessToken(tokens.accessToken());

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        void shouldReturnEmptyWhenIssuerIsDifferent() {
            // Given
            var otherProperties = new JwtProperties(SECRET, "another-issuer", AUDIENCE, ACCESS_TOKEN_TTL);
            var otherProvider = new JwtTokenProvider(otherProperties, Clock.fixed(NOW, ZoneOffset.UTC));
            var tokens = otherProvider.generate(TestSessionMother.EXISTING_OWNER_ID);

            // When
            var result = provider.verifyAccessToken(tokens.accessToken());

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        void shouldReturnEmptyWhenAudienceIsDifferent() {
            // Given
            var otherProperties = new JwtProperties(SECRET, ISSUER, "another-audience", ACCESS_TOKEN_TTL);
            var otherProvider = new JwtTokenProvider(otherProperties, Clock.fixed(NOW, ZoneOffset.UTC));
            var tokens = otherProvider.generate(TestSessionMother.EXISTING_OWNER_ID);

            // When
            var result = provider.verifyAccessToken(tokens.accessToken());

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        void shouldReturnEmptyWhenTokenIsTampered() {
            // Given
            var accessToken = provider.generate(TestSessionMother.EXISTING_OWNER_ID).accessToken();
            var segments = accessToken.split("\\.");
            var forgedPayload = Base64.getUrlEncoder().withoutPadding()
                    .encodeToString("{\"sub\":\"forged\"}".getBytes(StandardCharsets.UTF_8));
            var tampered = segments[0] + "." + forgedPayload + "." + segments[2];

            // When
            var result = provider.verifyAccessToken(tampered);

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        void shouldReturnEmptyWhenTokenIsMalformed() {
            // When
            var result = provider.verifyAccessToken("not-a-jwt");

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        void shouldReturnEmptyWhenTokenIsNull() {
            // When
            var result = provider.verifyAccessToken(null);

            // Then
            assertThat(result).isEmpty();
        }

    }

}
