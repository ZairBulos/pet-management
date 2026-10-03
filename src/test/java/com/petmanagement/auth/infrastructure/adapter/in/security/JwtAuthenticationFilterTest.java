package com.petmanagement.auth.infrastructure.adapter.in.security;

import com.petmanagement.auth.support.StubTokenProvider;
import com.petmanagement.auth.support.TestSessionMother;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;

class JwtAuthenticationFilterTest {

    private JwtAuthenticationFilter filter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockFilterChain chain;

    @BeforeEach
    void setUp() {
        filter = new JwtAuthenticationFilter(new StubTokenProvider());

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        chain = new MockFilterChain();
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Nested
    class WhenTokenIsValid {

        @Test
        void shouldAuthenticateWithUuidAsPrincipal() throws Exception {
            // Given
            request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + StubTokenProvider.DEFAULT_ACCESS_TOKEN);

            // When
            filter.doFilter(request, response, chain);

            // Then
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            assertNotNull(authentication);
            assertEquals(TestSessionMother.EXISTING_OWNER_ID.value(), authentication.getPrincipal());
        }

        @Test
        void shouldContinueFilterChain() throws Exception {
            // Given
            request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + StubTokenProvider.DEFAULT_ACCESS_TOKEN);

            // When
            filter.doFilter(request, response, chain);

            // Then
            assertNotNull(chain.getRequest());
        }

    }

    @Nested
    class WhenTokenIsInvalid {

        @Test
        void shouldNotAuthenticate() throws Exception {
            // Given
            request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer invalid-token");

            // When
            filter.doFilter(request, response, chain);

            // Then
            assertNull(SecurityContextHolder.getContext().getAuthentication());
        }

        @Test
        void shouldContinueFilterChain() throws Exception {
            // Given
            request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer invalid-token");

            // When
            filter.doFilter(request, response, chain);

            // Then
            assertNotNull(chain.getRequest());
        }

        @Test
        void shouldNotAuthenticateWhenBearerHasNoToken() throws Exception {
            // Given
            request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer ");

            // When
            filter.doFilter(request, response, chain);

            // Then
            assertNull(SecurityContextHolder.getContext().getAuthentication());
            assertNotNull(chain.getRequest());
        }

    }

    @Nested
    class WhenAuthorizationHeaderIsMissingOrNotBearer {

        @Test
        void shouldNotAuthenticateWhenHeaderIsMissing() throws Exception {
            // When
            filter.doFilter(request, response, chain);

            // Then
            assertNull(SecurityContextHolder.getContext().getAuthentication());
            assertNotNull(chain.getRequest());
        }

        @Test
        void shouldNotAuthenticateWhenSchemeIsNotBearer() throws Exception {
            // Given
            request.addHeader(HttpHeaders.AUTHORIZATION, "Basic " + StubTokenProvider.DEFAULT_ACCESS_TOKEN);

            // When
            filter.doFilter(request, response, chain);

            // Then
            assertNull(SecurityContextHolder.getContext().getAuthentication());
            assertNotNull(chain.getRequest());
        }

        @Test
        void shouldNotAuthenticateWhenSchemeIsLowercase() throws Exception {
            // Given
            request.addHeader(HttpHeaders.AUTHORIZATION, "bearer " + StubTokenProvider.DEFAULT_ACCESS_TOKEN);

            // When
            filter.doFilter(request, response, chain);

            // Then
            assertNull(SecurityContextHolder.getContext().getAuthentication());
            assertNotNull(chain.getRequest());
        }

    }

}
