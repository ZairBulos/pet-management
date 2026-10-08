package com.petmanagement.owners.infrastructure.adapter.in.http;

import com.petmanagement.owners.application.port.in.CreateOwnerUseCase;
import com.petmanagement.owners.application.port.in.GetOwnerUseCase;
import com.petmanagement.owners.application.port.in.UpdateOwnerUseCase;
import com.petmanagement.owners.domain.exception.OwnerAlreadyExistsException;
import com.petmanagement.owners.domain.exception.OwnerNotFoundException;
import com.petmanagement.owners.domain.model.valueobject.OwnerId;
import com.petmanagement.owners.infrastructure.adapter.in.http.dto.request.CreateOwnerRequest;
import com.petmanagement.owners.infrastructure.adapter.in.http.dto.request.UpdateOwnerRequest;
import com.petmanagement.owners.infrastructure.adapter.in.http.mapper.OwnerHttpMapper;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.stream.Stream;

import static com.petmanagement.owners.support.OwnerTestBuilder.CreateOwnerCommandBuilder.aCreateOwnerCommand;
import static com.petmanagement.owners.support.OwnerTestBuilder.UpdateOwnerCommandBuilder.aUpdateOwnerCommand;
import static com.petmanagement.owners.support.OwnerTestBuilder.aOwner;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OwnerController.class)
@Import(OwnerHttpMapper.class)
class OwnerControllerTest extends ControllerTestSupport {

    @MockitoBean
    private CreateOwnerUseCase createOwnerUseCase;

    @MockitoBean
    private GetOwnerUseCase getOwnerUseCase;

    @MockitoBean
    private UpdateOwnerUseCase updateOwnerUseCase;

    private static final String OWNERS = OwnerController.OWNERS;
    private static final String ME = OwnerController.OWNERS + OwnerController.ME;

    private static final OwnerId AUTHENTICATED_OWNER_ID = TestOwnerMother.DEFAULT_OWNER_ID;

    @Nested
    class Create {

        @Test
        void shouldReturn201WithLocation() throws Exception {
            // Given
            var command = aCreateOwnerCommand().build();
            var request = toRequest(command);

            given(createOwnerUseCase.execute(command))
                    .willReturn(TestOwnerMother.DEFAULT_OWNER_ID);

            // When/Then
            performPost(OWNERS, request)
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
            performPost(OWNERS, request)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));

            verifyNoInteractions(createOwnerUseCase);
        }

        @Test
        void shouldReturn409WhenOwnerAlreadyExists() throws Exception {
            // Given
            var command = aCreateOwnerCommand().build();
            var request = toRequest(command);

            given(createOwnerUseCase.execute(command))
                    .willThrow(new OwnerAlreadyExistsException());

            // When/Then
            performPost(OWNERS, request)
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
            var command = new GetOwnerUseCase.GetOwnerCommand(TestOwnerMother.DEFAULT_OWNER_ID);
            var owner = aOwner().build();

            given(getOwnerUseCase.execute(command))
                    .willReturn(owner);

            // When/Then
            performAuthenticatedGet(ME)
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.name").value(TestOwnerMother.OWNER_NAME_JOHN.value()))
                    .andExpect(jsonPath("$.email").value(TestOwnerMother.EMAIL_JOHN.value()))
                    .andExpect(jsonPath("$.phone").value(TestOwnerMother.PHONE_JOHN.value()));

            verify(getOwnerUseCase).execute(command);
        }

        @Test
        void shouldReturn404WhenOwnerDoesNotExist() throws Exception {
            // Given
            given(getOwnerUseCase.execute(any()))
                    .willThrow(new OwnerNotFoundException());

            // When/Then
            performAuthenticatedGet(ME)
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value("OWNER_NOT_FOUND"));
        }

        @Test
        void shouldReturn401WhenNoAuthenticationIsProvided() throws Exception {
            // When/Then
            performGet(ME)
                    .andExpect(status().isUnauthorized());

            verifyNoInteractions(getOwnerUseCase);
        }

    }

    @Nested
    class Update {

        @Test
        void shouldReturn200WithUpdatedOwnerData() throws Exception {
            // Given
            var command = aUpdateOwnerCommand().withId(AUTHENTICATED_OWNER_ID).build();
            var updated = aOwner()
                    .withName(TestOwnerMother.OWNER_NAME_ROBERT)
                    .withEmail(TestOwnerMother.EMAIL_ROBERT)
                    .withPhoneNumber(TestOwnerMother.PHONE_ROBERT)
                    .build();

            given(updateOwnerUseCase.execute(command))
                    .willReturn(updated);

            // When/Then
            performAuthenticatedPut(ME, toRequest(command))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.name").value(TestOwnerMother.OWNER_NAME_ROBERT.value()))
                    .andExpect(jsonPath("$.email").value(TestOwnerMother.EMAIL_ROBERT.value()))
                    .andExpect(jsonPath("$.phone").value(TestOwnerMother.PHONE_ROBERT.value()));

            verify(updateOwnerUseCase).execute(command);
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("invalidRequests")
        void shouldReturn400WhenFieldIsInvalid(
                String description, UpdateOwnerRequest request, String field
        ) throws Exception {
            // When/Then
            performAuthenticatedPut(ME, request)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));

            verifyNoInteractions(createOwnerUseCase);
        }

        @Test
        void shouldReturn404WhenOwnerDoesNotExist() throws Exception {
            // Given
            var request = toRequest(aUpdateOwnerCommand().build());

            given(updateOwnerUseCase.execute(any()))
                    .willThrow(new OwnerNotFoundException());

            // When/Then
            performAuthenticatedPut(ME, request)
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value("OWNER_NOT_FOUND"));
        }

        @Test
        void shouldReturn409WhenEmailBelongsToAnotherOwner() throws Exception {
            // Given
            var request = toRequest(aUpdateOwnerCommand()
                    .withEmail(TestOwnerMother.EMAIL_JOHN)
                    .build());

            given(updateOwnerUseCase.execute(any()))
                    .willThrow(new OwnerAlreadyExistsException());

            // When/Then
            performAuthenticatedPut(ME, request)
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.error").value("OWNER_ALREADY_EXISTS"));
        }

        @Test
        void shouldReturn401WhenNoAuthenticationIsProvided() throws Exception {
            // Given
            var request = toRequest(aUpdateOwnerCommand().build());

            // When/Then
            performPut(ME, request)
                    .andExpect(status().isUnauthorized());

            verifyNoInteractions(updateOwnerUseCase);
        }

        // === Helpers ===

        private UpdateOwnerRequest toRequest(UpdateOwnerUseCase.UpdateOwnerCommand command) {
            return new UpdateOwnerRequest(
                    command.name().value(),
                    command.email().value(),
                    command.phone().value()
            );
        }

        static Stream<Arguments> invalidRequests() {
            var name = TestOwnerMother.OWNER_NAME_ROBERT.value();
            var email = TestOwnerMother.EMAIL_ROBERT.value();
            var phone = TestOwnerMother.PHONE_ROBERT.value();

            return Stream.of(
                    // name
                    arguments("name is empty", new UpdateOwnerRequest("", email, phone), "name"),
                    arguments("name is blank", new UpdateOwnerRequest("   ", email, phone), "name"),
                    arguments("name has 1 char after trim", new UpdateOwnerRequest("  A  ", email, phone), "name"),
                    arguments("name exceeds 150 chars", new UpdateOwnerRequest("a".repeat(151), email, phone), "name"),

                    // email
                    arguments("email is blank", new UpdateOwnerRequest(name, "   ", phone), "email"),
                    arguments("email has no @", new UpdateOwnerRequest(name, "not-an-email", phone), "email"),
                    arguments("email exceeds 254 chars", new UpdateOwnerRequest(name, "a".repeat(250) + "@x.com", phone), "email"),

                    // phone
                    arguments("phone is blank", new UpdateOwnerRequest(name, email, "   "), "phone"),
                    arguments("phone has letters", new UpdateOwnerRequest(name, email, "abc"), "phone"),
                    arguments("phone has fewer than 7 digits", new UpdateOwnerRequest(name, email, "123456"), "phone"),
                    arguments("phone has more than 15 digits", new UpdateOwnerRequest(name, email, "1234567890123456"), "phone")
            );
        }

    }

}
