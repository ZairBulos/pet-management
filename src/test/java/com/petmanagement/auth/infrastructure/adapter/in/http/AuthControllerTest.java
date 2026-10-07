package com.petmanagement.auth.infrastructure.adapter.in.http;

import com.petmanagement.auth.application.port.in.RequestAuthenticationUseCase;
import com.petmanagement.auth.domain.exception.OwnerNotFoundException;
import com.petmanagement.auth.infrastructure.adapter.in.http.dto.request.RequestAuthenticationRequest;
import com.petmanagement.auth.infrastructure.adapter.in.http.mapper.AuthHttpMapper;
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
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(AuthHttpMapper.class)
class AuthControllerTest extends ControllerTestSupport {

    @MockitoBean
    private RequestAuthenticationUseCase requestAuthenticationUseCase;

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

}
