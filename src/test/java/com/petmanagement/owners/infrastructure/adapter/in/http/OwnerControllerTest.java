package com.petmanagement.owners.infrastructure.adapter.in.http;

import com.petmanagement.owners.application.port.in.CreateOwnerUseCase;
import com.petmanagement.owners.application.port.in.GetOwnerUseCase;
import com.petmanagement.owners.domain.exception.OwnerAlreadyExistsException;
import com.petmanagement.owners.domain.exception.OwnerNotFoundException;
import com.petmanagement.owners.domain.model.valueobject.OwnerId;
import com.petmanagement.owners.infrastructure.adapter.in.http.dto.request.CreateOwnerRequest;
import com.petmanagement.owners.infrastructure.adapter.in.http.mapper.OwnerHttpMapper;
import com.petmanagement.owners.support.OwnerTestBuilder;
import com.petmanagement.owners.support.TestOwnerMother;
import com.petmanagement.support.ControllerTestSupport;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OwnerController.class)
@Import(OwnerHttpMapper.class)
class OwnerControllerTest extends ControllerTestSupport {

    @MockitoBean
    private CreateOwnerUseCase createOwnerUseCase;
    @MockitoBean
    private GetOwnerUseCase getOwnerUseCase;

    private RequestPostProcessor authenticatedAs(OwnerId ownerId) {
        return authentication(
                new UsernamePasswordAuthenticationToken(ownerId.value(), null, Collections.emptyList())
        );
    }

    @Nested
    class Create {

        @Test
        void shouldReturn201WithLocation() throws Exception {
            // Given
            var command = OwnerTestBuilder.CreateOwnerCommandBuilder.aCreateOwnerCommand().build();
            var request = toRequest(command);

            given(createOwnerUseCase.execute(command))
                    .willReturn(TestOwnerMother.DEFAULT_OWNER_ID);

            // When/Then
            postOwner(request)
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", OwnerController.OWNERS + OwnerController.ME));

            verify(createOwnerUseCase).execute(command);
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("invalidRequests")
        void shouldReturn400WhenFieldIsInvalid(
                String description, CreateOwnerRequest request, String field
        ) throws Exception {
            // When/Then
            postOwner(request)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));

            verifyNoInteractions(createOwnerUseCase);
        }

        @Test
        void shouldReturn409WhenOwnerAlreadyExists() throws Exception {
            // Given
            var command = OwnerTestBuilder.CreateOwnerCommandBuilder.aCreateOwnerCommand().build();
            var request = toRequest(command);

            given(createOwnerUseCase.execute(command))
                    .willThrow(new OwnerAlreadyExistsException());

            // When/Then
            postOwner(request)
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.error").value("OWNER_ALREADY_EXISTS"));
        }

        // === Helpers ===

        private CreateOwnerRequest toRequest(CreateOwnerUseCase.CreateOwnerCommand command) {
            return new CreateOwnerRequest(
                    command.name().value(),
                    command.email().value(),
                    command.phone().value()
            );
        }

        private ResultActions postOwner(Object body) throws Exception {
            return mockMvc.perform(post(OwnerController.OWNERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonMapper.writeValueAsString(body)));
        }

        static Stream<Arguments> invalidRequests() {
            var name = TestOwnerMother.OWNER_NAME_JOHN.value();
            var email = TestOwnerMother.EMAIL_JOHN.value();
            var phone = TestOwnerMother.PHONE_JOHN.value();

            return Stream.of(
                    // name
                    arguments("name is empty", new CreateOwnerRequest("", email, phone), "name"),
                    arguments("name is blank", new CreateOwnerRequest("   ", email, phone), "name"),
                    arguments("name has 1 char after trim", new CreateOwnerRequest("  A  ", email, phone), "name"),
                    arguments("name exceeds 150 chars", new CreateOwnerRequest("a".repeat(151), email, phone), "name"),

                    // email
                    arguments("email is blank", new CreateOwnerRequest(name, "   ", phone), "email"),
                    arguments("email has no @", new CreateOwnerRequest(name, "not-an-email", phone), "email"),
                    arguments("email exceeds 254 chars", new CreateOwnerRequest(name, "a".repeat(250) + "@x.com", phone), "email"),

                    // phone
                    arguments("phone is blank", new CreateOwnerRequest(name, email, "   "), "phone"),
                    arguments("phone has letters", new CreateOwnerRequest(name, email, "abc"), "phone"),
                    arguments("phone has fewer than 7 digits", new CreateOwnerRequest(name, email, "123456"), "phone"),
                    arguments("phone has more than 15 digits", new CreateOwnerRequest(name, email, "1234567890123456"), "phone")
            );
        }

    }

    @Nested
    class Me {

        @Test
        void shouldReturn200WithOwnerData() throws Exception {
            // Given
            var ownerId = TestOwnerMother.DEFAULT_OWNER_ID;
            var owner = OwnerTestBuilder.aOwner().build();

            given(getOwnerUseCase.execute(new GetOwnerUseCase.GetOwnerCommand(ownerId)))
                    .willReturn(owner);

            // When/Then
            getMe(ownerId)
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.name").value(TestOwnerMother.OWNER_NAME_JOHN.value()))
                    .andExpect(jsonPath("$.email").value(TestOwnerMother.EMAIL_JOHN.value()))
                    .andExpect(jsonPath("$.phone").value(TestOwnerMother.PHONE_JOHN.value()));

            verify(getOwnerUseCase).execute(new GetOwnerUseCase.GetOwnerCommand(ownerId));
        }

        @Test
        void shouldReturn401_whenNoAuthenticationIsProvided() throws Exception {
            // When/Then
            getMeWithoutAuth()
                    .andExpect(status().isUnauthorized());

            verifyNoInteractions(getOwnerUseCase);
        }

        @Test
        void shouldReturn404_whenOwnerDoesNotExist() throws Exception {
            // Given
            var ownerId = TestOwnerMother.NON_EXISTING_OWNER_ID;

            given(getOwnerUseCase.execute(any()))
                    .willThrow(new OwnerNotFoundException());

            // When/Then
            getMe(ownerId)
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value("OWNER_NOT_FOUND"));
        }

        // === Helpers ===

        private ResultActions getMe(OwnerId ownerId) throws Exception {
            return mockMvc.perform(get(OwnerController.OWNERS + OwnerController.ME)
                    .with(authenticatedAs(ownerId)));
        }

        private ResultActions getMeWithoutAuth() throws Exception {
            return mockMvc.perform(get(OwnerController.OWNERS + OwnerController.ME));
        }

    }

}
