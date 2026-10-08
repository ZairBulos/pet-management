package com.petmanagement.auth.infrastructure.adapter.in.http;

import com.petmanagement.auth.application.port.in.RefreshSessionUseCase;
import com.petmanagement.auth.application.port.in.RequestAuthenticationUseCase;
import com.petmanagement.auth.application.port.in.RevokeSessionUseCase;
import com.petmanagement.auth.application.port.in.VerifyAuthenticationUseCase;
import com.petmanagement.auth.domain.exception.*;
import com.petmanagement.auth.infrastructure.adapter.in.http.dto.request.RefreshSessionRequest;
import com.petmanagement.auth.infrastructure.adapter.in.http.dto.request.RequestAuthenticationRequest;
import com.petmanagement.auth.infrastructure.adapter.in.http.dto.request.RevokeSessionRequest;
import com.petmanagement.auth.infrastructure.adapter.in.http.dto.request.VerifyAuthenticationRequest;
import com.petmanagement.auth.infrastructure.adapter.in.http.mapper.AuthHttpMapper;
import com.petmanagement.auth.support.TestAuthenticationMother;
import com.petmanagement.support.ControllerTestSupport;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.stream.Stream;

import static com.petmanagement.auth.support.AuthenticationTestBuilder.RequestAuthenticationCommandBuilder.aRequestAuthenticationCommand;
import static com.petmanagement.auth.support.AuthenticationTestBuilder.VerifyAuthenticationCommandBuilder.aVerifyAuthenticationCommand;
import static com.petmanagement.auth.support.SessionTestBuilder.RefreshSessionCommandBuilder.aRefreshSessionCommand;
import static com.petmanagement.auth.support.SessionTestBuilder.RevokeSessionCommandBuilder.aRevokeSessionCommand;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(AuthHttpMapper.class)
class AuthControllerTest extends ControllerTestSupport {

    @MockitoBean
    private RequestAuthenticationUseCase requestAuthenticationUseCase;

    @MockitoBean
    private VerifyAuthenticationUseCase verifyAuthenticationUseCase;

    @MockitoBean
    private RefreshSessionUseCase refreshSessionUseCase;

    @MockitoBean
    private RevokeSessionUseCase revokeSessionUseCase;

    @Nested
    class RequestAuthentication {

        private static final String REQUEST = AuthController.AUTH + AuthController.REQUEST;

        @Test
        void shouldReturn202WithoutBody() throws Exception {
            // Given
            var command = aRequestAuthenticationCommand().build();
            var request = toRequest(command);

            // When/Then
            performPost(REQUEST, request)
                    .andExpect(status().isAccepted())
                    .andExpect(content().string(""));

            verify(requestAuthenticationUseCase).execute(command);
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("invalidRequests")
        void shouldReturn400WhenEmailIsInvalid(
                String description, RequestAuthenticationRequest request
        ) throws Exception {
            // When/Then
            performPost(REQUEST, request)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));

            verifyNoInteractions(requestAuthenticationUseCase);
        }

        @Test
        void shouldReturn404WhenOwnerDoesNotExist() throws Exception {
            // Given
            var request = toRequest(aRequestAuthenticationCommand().build());

            doThrow(new OwnerNotFoundException())
                    .when(requestAuthenticationUseCase).execute(any());

            // When/Then
            performPost(REQUEST, request)
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value("OWNER_NOT_FOUND"));
        }

        // === Helpers ===

        private RequestAuthenticationRequest toRequest(
                RequestAuthenticationUseCase.RequestAuthenticationCommand command
        ) {
            return new RequestAuthenticationRequest(command.email().value());
        }

        static Stream<Arguments> invalidRequests() {
            return Stream.of(
                    arguments("email is empty", new RequestAuthenticationRequest("")),
                    arguments("email is blank", new RequestAuthenticationRequest("   ")),
                    arguments("email has no @", new RequestAuthenticationRequest("not-an-email")),
                    arguments("email exceeds 254 chars", new RequestAuthenticationRequest("a".repeat(250) + "@x.com"))
            );
        }

    }

    @Nested
    class VerifyAuthentication {

        private static final String VERIFY = AuthController.AUTH + AuthController.VERIFY;

        @Test
        void shouldReturn200WithTokens() throws Exception {
            // Given
            var command = aVerifyAuthenticationCommand().build();
            var request = toRequest(command);

            given(verifyAuthenticationUseCase.execute(command))
                    .willReturn(new VerifyAuthenticationUseCase.VerifyAuthenticationResult("access-token", "refresh-token"));

            // When/Then
            performPost(VERIFY, request)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken").value("access-token"))
                    .andExpect(jsonPath("$.refreshToken").value("refresh-token"));

            verify(verifyAuthenticationUseCase).execute(command);
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("invalidRequests")
        void shouldReturn400WhenRequestIsInvalid(
                String description, VerifyAuthenticationRequest request, String field
        ) throws Exception {
            // When/Then
            performPost(VERIFY, request)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));

            verifyNoInteractions(verifyAuthenticationUseCase);
        }

        @Test
        void shouldReturn404WhenOwnerDoesNotExist() throws Exception {
            // Given
            var request = toRequest(aVerifyAuthenticationCommand().build());
            given(verifyAuthenticationUseCase.execute(any())).willThrow(new OwnerNotFoundException());

            // When/Then
            performPost(VERIFY, request)
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value("OWNER_NOT_FOUND"));
        }

        @Test
        void shouldReturn404WhenActiveAuthenticationDoesNotExist() throws Exception {
            // Given
            var request = toRequest(aVerifyAuthenticationCommand().build());
            given(verifyAuthenticationUseCase.execute(any()))
                    .willThrow(new ActiveAuthenticationNotFoundException());

            // When/Then
            performPost(VERIFY, request)
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value("ACTIVE_AUTHENTICATION_NOT_FOUND"));
        }

        @Test
        void shouldReturn409WhenAuthenticationIsAlreadyUsed() throws Exception {
            // Given
            var request = toRequest(aVerifyAuthenticationCommand().build());
            given(verifyAuthenticationUseCase.execute(any()))
                    .willThrow(new AuthenticationAlreadyUsedException());

            // When/Then
            performPost(VERIFY, request)
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.error").value("AUTHENTICATION_ALREADY_USED"));
        }

        @Test
        void shouldReturn410WhenAuthenticationIsExpired() throws Exception {
            // Given
            var request = toRequest(aVerifyAuthenticationCommand().build());
            given(verifyAuthenticationUseCase.execute(any()))
                    .willThrow(new AuthenticationExpiredException());

            // When/Then
            performPost(VERIFY, request)
                    .andExpect(status().isGone())
                    .andExpect(jsonPath("$.error").value("AUTHENTICATION_EXPIRED"));
        }

        @Test
        void shouldReturn401WhenAuthenticationCodeIsInvalid() throws Exception {
            // Given
            var request = toRequest(aVerifyAuthenticationCommand().build());
            given(verifyAuthenticationUseCase.execute(any()))
                    .willThrow(new InvalidAuthenticationCodeException());

            // When/Then
            performPost(VERIFY, request)
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.error").value("INVALID_AUTHENTICATION_CODE"));
        }

        // === Helpers ===

        private VerifyAuthenticationRequest toRequest(
                VerifyAuthenticationUseCase.VerifyAuthenticationCommand command
        ) {
            return new VerifyAuthenticationRequest(command.email().value(), command.code().value());
        }

        static Stream<Arguments> invalidRequests() {
            var email = TestAuthenticationMother.EMAIL_JOHN.value();
            var code = TestAuthenticationMother.DEFAULT_PLAIN_CODE.value();

            return Stream.of(
                    arguments("email is blank", new VerifyAuthenticationRequest("   ", code), "email"),
                    arguments("email has no @", new VerifyAuthenticationRequest("not-an-email", code), "email"),
                    arguments("code is blank", new VerifyAuthenticationRequest(email, "   "), "code"),
                    arguments("code has letters", new VerifyAuthenticationRequest(email, "12ab56"), "code"),
                    arguments("code has fewer than 6 digits", new VerifyAuthenticationRequest(email, "12345"), "code"),
                    arguments("code has more than 6 digits", new VerifyAuthenticationRequest(email, "1234567"), "code")
            );
        }

    }

    @Nested
    class RefreshSession {

        private static final String REFRESH = AuthController.AUTH + AuthController.REFRESH;

        @Test
        void shouldReturn200WithNewTokens() throws Exception {
            // Given
            var command = aRefreshSessionCommand().build();
            var request = toRequest(command);

            given(refreshSessionUseCase.execute(command))
                    .willReturn(new RefreshSessionUseCase.RefreshSessionResult("new-access-token", "new-refresh-token"));

            // When/Then
            performPost(REFRESH, request)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken").value("new-access-token"))
                    .andExpect(jsonPath("$.refreshToken").value("new-refresh-token"));

            verify(refreshSessionUseCase).execute(command);
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("invalidRequests")
        void shouldReturn400WhenRequestIsInvalid(
                String description, RefreshSessionRequest request
        ) throws Exception {
            // When/Then
            performPost(REFRESH, request)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));

            verifyNoInteractions(refreshSessionUseCase);
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("invalidSessions")
        void shouldReturn401WhenSessionIsNotValid(
                String description, RuntimeException exception
        ) throws Exception {
            // Given
            var request = toRequest(aRefreshSessionCommand().build());

            given(refreshSessionUseCase.execute(any()))
                    .willThrow(exception);

            // When/Then
            performPost(REFRESH, request)
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.error").value("INVALID_SESSION"));
        }

        // === Helpers ===

        private RefreshSessionRequest toRequest(RefreshSessionUseCase.RefreshSessionCommand command) {
            return new RefreshSessionRequest(command.refreshToken().value());
        }

        static Stream<Arguments> invalidRequests() {
            return Stream.of(
                    arguments("refresh token is null", new RefreshSessionRequest(null)),
                    arguments("refresh token is empty", new RefreshSessionRequest("")),
                    arguments("refresh token is blank", new RefreshSessionRequest("   "))
            );
        }

        static Stream<Arguments> invalidSessions() {
            return Stream.of(
                    arguments("session does not exist", new SessionNotFoundException()),
                    arguments("session is expired", new SessionExpiredException()),
                    arguments("session is revoked", new SessionRevokedException()),
                    arguments("session reuse is detected", new SessionReuseDetectedException())
            );
        }

    }

    @Nested
    class RevokeSession {

        private static final String REVOKE = AuthController.AUTH + AuthController.REVOKE;

        @Test
        void shouldReturn204WithoutBody() throws Exception {
            // Given
            var command = aRevokeSessionCommand().build();
            var request = toRequest(command);

            // When/Then
            performAuthenticatedPost(REVOKE, request)
                    .andExpect(status().isNoContent())
                    .andExpect(content().string(""));

            verify(revokeSessionUseCase).execute(command);
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("invalidRequests")
        void shouldReturn400WhenRequestIsInvalid(
                String description, RevokeSessionRequest request
        ) throws Exception {
            // When/Then
            performAuthenticatedPost(REVOKE, request)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));

            verifyNoInteractions(revokeSessionUseCase);
        }

        @Test
        void shouldReturn401WhenSessionDoesNotExist() throws Exception {
            // Given
            var request = toRequest(aRevokeSessionCommand().build());

            doThrow(new SessionNotFoundException())
                    .when(revokeSessionUseCase).execute(any());

            // When/Then
            performAuthenticatedPost(REVOKE, request)
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.error").value("INVALID_SESSION"));
        }

        @Test
        void shouldReturn401WhenNoAuthenticationIsProvided() throws Exception {
            // Given
            var request = toRequest(aRevokeSessionCommand().build());

            // When/Then
            performPost(REVOKE, request)
                    .andExpect(status().isUnauthorized());

            verifyNoInteractions(revokeSessionUseCase);
        }

        // === Helpers ===

        private RevokeSessionRequest toRequest(RevokeSessionUseCase.RevokeSessionCommand command) {
            return new RevokeSessionRequest(command.refreshToken().value());
        }

        static Stream<Arguments> invalidRequests() {
            return Stream.of(
                    arguments("refresh token is null", new RevokeSessionRequest(null)),
                    arguments("refresh token is empty", new RevokeSessionRequest("")),
                    arguments("refresh token is blank", new RevokeSessionRequest("   "))
            );
        }

    }

}
