package com.petmanagement.auth.infrastructure.adapter.in.http;

import com.petmanagement.auth.application.port.in.RequestAuthenticationUseCase;
import com.petmanagement.auth.application.port.in.VerifyAuthenticationUseCase;
import com.petmanagement.auth.domain.exception.*;
import com.petmanagement.auth.infrastructure.adapter.in.http.dto.request.RequestAuthenticationRequest;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.ResultActions;

import java.util.stream.Stream;

import static com.petmanagement.auth.support.AuthenticationTestBuilder.RequestAuthenticationCommandBuilder.aRequestAuthenticationCommand;
import static com.petmanagement.auth.support.AuthenticationTestBuilder.VerifyAuthenticationCommandBuilder.aVerifyAuthenticationCommand;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(AuthHttpMapper.class)
class AuthControllerTest extends ControllerTestSupport {

    @MockitoBean
    private RequestAuthenticationUseCase requestAuthenticationUseCase;

    @MockitoBean
    private VerifyAuthenticationUseCase verifyAuthenticationUseCase;

    @Nested
    class RequestAuthentication {

        @Test
        void shouldReturn202WithoutBody() throws Exception {
            // Given
            var command = aRequestAuthenticationCommand().build();
            var request = toRequest(command);

            // When/Then
            postRequest(request)
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
            postRequest(request)
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
            postRequest(request)
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value("OWNER_NOT_FOUND"));
        }

        // === Helpers ===

        private RequestAuthenticationRequest toRequest(
                RequestAuthenticationUseCase.RequestAuthenticationCommand command
        ) {
            return new RequestAuthenticationRequest(command.email().value());
        }

        private ResultActions postRequest(Object body) throws Exception {
            return mockMvc.perform(post(AuthController.AUTH + AuthController.REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonMapper.writeValueAsString(body)));
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

        @Test
        void shouldReturn200WithTokens() throws Exception {
            // Given
            var command = aVerifyAuthenticationCommand().build();
            var request = toRequest(command);

            given(verifyAuthenticationUseCase.execute(command))
                    .willReturn(new VerifyAuthenticationUseCase.VerifyAuthenticationResult("access-token", "refresh-token"));

            // When/Then
            postVerify(request)
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
            postVerify(request)
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
            postVerify(request)
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
            postVerify(request)
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
            postVerify(request)
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
            postVerify(request)
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
            postVerify(request)
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.error").value("INVALID_AUTHENTICATION_CODE"));
        }

        // === Helpers ===

        private VerifyAuthenticationRequest toRequest(
                VerifyAuthenticationUseCase.VerifyAuthenticationCommand command
        ) {
            return new VerifyAuthenticationRequest(command.email().value(), command.code().value());
        }

        private ResultActions postVerify(Object body) throws Exception {
            return mockMvc.perform(post(AuthController.AUTH + AuthController.VERIFY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonMapper.writeValueAsString(body)));
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

}
