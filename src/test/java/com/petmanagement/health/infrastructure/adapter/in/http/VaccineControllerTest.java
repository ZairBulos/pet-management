package com.petmanagement.health.infrastructure.adapter.in.http;

import com.petmanagement.health.application.port.in.CreateVaccineUseCase;
import com.petmanagement.health.domain.exception.PetNotFoundException;
import com.petmanagement.health.domain.model.valueobject.VaccineId;
import com.petmanagement.health.infrastructure.adapter.in.http.dto.request.CreateVaccineRequest;
import com.petmanagement.health.infrastructure.adapter.in.http.mapper.VaccineHttpMapper;
import com.petmanagement.health.support.TestCommonMother;
import com.petmanagement.health.support.TestPetIdMother;
import com.petmanagement.health.support.TestVaccineMother;
import com.petmanagement.support.ControllerTestSupport;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.stream.Stream;

import static com.petmanagement.health.support.VaccineTestBuilder.CreateVaccineCommandBuilder.aCreateVaccineCommand;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = VaccineController.class)
@Import(VaccineHttpMapper.class)
class VaccineControllerTest extends ControllerTestSupport {

    @MockitoBean
    private CreateVaccineUseCase createVaccineUseCase;

    private static final String VACCINES = VaccineController.VACCINES;

    @Nested
    class Create {

        @Test
        void shouldReturn201WithLocation() throws Exception {
            // Given
            var command = aCreateVaccineCommand().build();
            var request = toRequest(command);

            given(createVaccineUseCase.execute(command))
                    .willReturn(VaccineId.generate());

            // When/Then
            performAuthenticatedPost(VACCINES, request)
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", VACCINES));

            verify(createVaccineUseCase).execute(command);
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("invalidRequests")
        void shouldReturn400WhenFieldIsInvalid(
                String description, CreateVaccineRequest request, String field
        ) throws Exception {
            // When/Then
            performAuthenticatedPost(VACCINES, request)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));

            verifyNoInteractions(createVaccineUseCase);
        }

        @Test
        void shouldReturn404WhenPetDoesNotExist() throws Exception {
            // Given
            var request = toRequest(aCreateVaccineCommand().build());

            given(createVaccineUseCase.execute(any()))
                    .willThrow(new PetNotFoundException());

            // When/Then
            performAuthenticatedPost(VACCINES, request)
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value("PET_NOT_FOUND"));
        }

        @Test
        void shouldReturn401WhenNoAuthenticationIsProvided() throws Exception {
            // Given
            var request = toRequest(aCreateVaccineCommand().build());

            // When/Then
            performPost(VACCINES, request)
                    .andExpect(status().isUnauthorized());

            verifyNoInteractions(createVaccineUseCase);
        }

        // === Helpers ===

        private CreateVaccineRequest toRequest(CreateVaccineUseCase.CreateVaccineCommand command) {
            return new CreateVaccineRequest(
                    command.petId().value(),
                    command.vaccinationDate(),
                    command.vaccineName().value(),
                    command.nextDueDate().value()
            );
        }

        static Stream<Arguments> invalidRequests() {
            var petId = TestPetIdMother.EXISTING_PET_ID.value();
            var date = TestCommonMother.EARLY_DATE;
            var name = TestVaccineMother.VACCINE_NAME_RABIES.value();
            var dueDate = TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR.value();

            return Stream.of(
                    // petId
                    arguments("pet id is null", new CreateVaccineRequest(null, date, name, dueDate), "petId"),

                    // vaccinationDate
                    arguments("vaccination date is null", new CreateVaccineRequest(petId, null, name, dueDate), "vaccinationDate"),
                    arguments("vaccination date is in the future", new CreateVaccineRequest(petId, LocalDate.now().plusDays(1), name, dueDate), "vaccinationDate"),

                    // vaccineName
                    arguments("vaccine name is null", new CreateVaccineRequest(petId, date, null, dueDate), "vaccineName"),
                    arguments("vaccine name is empty", new CreateVaccineRequest(petId, date, "", dueDate), "vaccineName"),
                    arguments("vaccine name is blank", new CreateVaccineRequest(petId, date, "   ", dueDate), "vaccineName"),
                    arguments("vaccine name exceeds 100 chars", new CreateVaccineRequest(petId, date, "a".repeat(101), dueDate), "vaccineName"),

                    // nextDueDate
                    arguments("next due date is null", new CreateVaccineRequest(petId, date, name, null), "nextDueDate"),
                    arguments("next due date is in the past", new CreateVaccineRequest(petId, date, name, LocalDate.now().minusDays(1)), "nextDueDate")
            );
        }

    }

}
