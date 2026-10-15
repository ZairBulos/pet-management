package com.petmanagement.health.infrastructure.adapter.in.http;

import com.petmanagement.health.application.port.in.CreateVaccineUseCase;
import com.petmanagement.health.application.port.in.GetVaccineHistoryUseCase;
import com.petmanagement.health.application.port.in.RescheduleVaccineUseCase;
import com.petmanagement.health.domain.exception.PetNotFoundException;
import com.petmanagement.health.domain.exception.VaccineNotFoundException;
import com.petmanagement.health.domain.model.valueobject.NextDueDate;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.health.domain.model.valueobject.VaccineId;
import com.petmanagement.health.infrastructure.adapter.in.http.dto.request.CreateVaccineRequest;
import com.petmanagement.health.infrastructure.adapter.in.http.dto.request.RescheduleVaccineRequest;
import com.petmanagement.health.infrastructure.adapter.in.http.mapper.VaccineHttpMapper;
import com.petmanagement.health.support.TestCommonMother;
import com.petmanagement.health.support.TestPetIdMother;
import com.petmanagement.health.support.TestVaccineMother;
import com.petmanagement.shared.domain.model.PageRequest;
import com.petmanagement.shared.domain.model.PageResponse;
import com.petmanagement.support.ControllerTestSupport;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static com.petmanagement.health.support.VaccineTestBuilder.CreateVaccineCommandBuilder.aCreateVaccineCommand;
import static com.petmanagement.health.support.VaccineTestBuilder.aVaccine;
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

    @MockitoBean
    private GetVaccineHistoryUseCase getVaccineHistoryUseCase;

    @MockitoBean
    private RescheduleVaccineUseCase rescheduleVaccineUseCase;

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

    @Nested
    class History {

        private static final PetId PET_ID = TestPetIdMother.EXISTING_PET_ID;

        @Test
        void shouldReturn200WithVaccineHistory() throws Exception {
            // Given
            var vaccine = aVaccine().withPetId(PET_ID).build();
            var query = new GetVaccineHistoryUseCase.GetVaccineHistoryQuery(PET_ID, PageRequest.of(0, 10));

            given(getVaccineHistoryUseCase.execute(query))
                    .willReturn(PageResponse.of(List.of(vaccine), 0, 10, 1));

            // When/Then
            performAuthenticatedGet(VACCINES + "?petId=" + PET_ID.value() + "&page=0&size=10")
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content.length()").value(1))
                    .andExpect(jsonPath("$.content[0].id").value(vaccine.id().value().toString()))
                    .andExpect(jsonPath("$.content[0].vaccinationDate").value(vaccine.vaccinationDate().toString()))
                    .andExpect(jsonPath("$.content[0].vaccineName").value(vaccine.vaccineName().value()))
                    .andExpect(jsonPath("$.content[0].nextDueDate").value(vaccine.nextDueDate().value().toString()))
                    .andExpect(jsonPath("$.page").value(0))
                    .andExpect(jsonPath("$.size").value(10))
                    .andExpect(jsonPath("$.totalElements").value(1))
                    .andExpect(jsonPath("$.totalPages").value(1));

            verify(getVaccineHistoryUseCase).execute(query);
        }

        @Test
        void shouldUseDefaultPaginationWhenNotProvided() throws Exception {
            // Given
            var query = new GetVaccineHistoryUseCase.GetVaccineHistoryQuery(PET_ID, PageRequest.of(0, 10));

            given(getVaccineHistoryUseCase.execute(query))
                    .willReturn(PageResponse.empty(0, 10));

            // When/Then
            performAuthenticatedGet(VACCINES + "?petId=" + PET_ID.value())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.length()").value(0))
                    .andExpect(jsonPath("$.totalElements").value(0))
                    .andExpect(jsonPath("$.totalPages").value(0));

            verify(getVaccineHistoryUseCase).execute(query);
        }

        @Test
        void shouldUseProvidedPaginationParams() throws Exception {
            // Given
            var query = new GetVaccineHistoryUseCase.GetVaccineHistoryQuery(PET_ID, PageRequest.of(2, 5));

            given(getVaccineHistoryUseCase.execute(query))
                    .willReturn(PageResponse.of(List.of(), 2, 5, 11));

            // When/Then
            performAuthenticatedGet(VACCINES + "?petId=" + PET_ID.value() + "&page=2&size=5")
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.page").value(2))
                    .andExpect(jsonPath("$.size").value(5))
                    .andExpect(jsonPath("$.totalPages").value(3));

            verify(getVaccineHistoryUseCase).execute(query);
        }

        @Test
        void shouldReturn400WhenPetIdIsMissing() throws Exception {
            // When/Then
            performAuthenticatedGet(VACCINES)
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(getVaccineHistoryUseCase);
        }

        @Test
        void shouldReturn400WhenPetIdIsNotAUuid() throws Exception {
            // When/Then
            performAuthenticatedGet(VACCINES + "?petId=not-a-uuid")
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(getVaccineHistoryUseCase);
        }

        @ParameterizedTest(name = "page={0}, size={1}")
        @CsvSource({"-1, 10", "0, 0", "0, -5"})
        void shouldReturn400WhenPaginationIsInvalid(int page, int size) throws Exception {
            // When/Then
            performAuthenticatedGet(VACCINES + "?petId=" + PET_ID.value() + "&page=" + page + "&size=" + size)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));

            verifyNoInteractions(getVaccineHistoryUseCase);
        }

        @Test
        void shouldReturn404WhenPetDoesNotExist() throws Exception {
            // Given
            given(getVaccineHistoryUseCase.execute(any()))
                    .willThrow(new PetNotFoundException());

            // When/Then
            performAuthenticatedGet(VACCINES + "?petId=" + PET_ID.value())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value("PET_NOT_FOUND"));
        }

        @Test
        void shouldReturn401WhenNoAuthenticationIsProvided() throws Exception {
            // When/Then
            performGet(VACCINES + "?petId=" + PET_ID.value())
                    .andExpect(status().isUnauthorized());

            verifyNoInteractions(getVaccineHistoryUseCase);
        }

    }

    @Nested
    class Reschedule {

        private static final LocalDate NEW_DUE_DATE = LocalDate.now().plusYears(1);

        @Test
        void shouldReturn200WithRescheduledVaccine() throws Exception {
            // Given
            var vaccine = aVaccine().build();
            var command = new RescheduleVaccineUseCase.RescheduleVaccineCommand(vaccine.id(), NEW_DUE_DATE);
            var rescheduled = aVaccine().withNextDueDate(new NextDueDate(NEW_DUE_DATE)).build();

            given(rescheduleVaccineUseCase.execute(command))
                    .willReturn(rescheduled);

            // When/Then
            performAuthenticatedPatch(VACCINES, toRequest(command))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(rescheduled.id().value().toString()))
                    .andExpect(jsonPath("$.vaccineName").value(rescheduled.vaccineName().value()))
                    .andExpect(jsonPath("$.nextDueDate").value(NEW_DUE_DATE.toString()));

            verify(rescheduleVaccineUseCase).execute(command);
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("invalidRequests")
        void shouldReturn400WhenFieldIsInvalid(
                String description, RescheduleVaccineRequest request, String field
        ) throws Exception {
            // When/Then
            performAuthenticatedPatch(VACCINES, request)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));

            verifyNoInteractions(rescheduleVaccineUseCase);
        }

        @Test
        void shouldReturn400WhenNextDueDateIsNotAfterVaccinationDate() throws Exception {
            // Given
            var request = new RescheduleVaccineRequest(VaccineId.generate().value(), NEW_DUE_DATE);

            given(rescheduleVaccineUseCase.execute(any()))
                    .willThrow(new IllegalArgumentException("Next due date must be after base date"));

            // When/Then
            performAuthenticatedPatch(VACCINES, request)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));
        }

        @Test
        void shouldReturn404WhenVaccineDoesNotExist() throws Exception {
            // Given
            var request = new RescheduleVaccineRequest(
                    TestVaccineMother.NON_EXISTENT_VACCINE_ID.value(),
                    NEW_DUE_DATE
            );

            given(rescheduleVaccineUseCase.execute(any()))
                    .willThrow(new VaccineNotFoundException());

            // When/Then
            performAuthenticatedPatch(VACCINES, request)
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value("VACCINE_NOT_FOUND"));
        }

        @Test
        void shouldReturn401WhenNoAuthenticationIsProvided() throws Exception {
            // Given
            var request = new RescheduleVaccineRequest(VaccineId.generate().value(), NEW_DUE_DATE);

            // When/Then
            performPatch(VACCINES, request)
                    .andExpect(status().isUnauthorized());

            verifyNoInteractions(rescheduleVaccineUseCase);
        }

        // === Helpers ===

        private RescheduleVaccineRequest toRequest(RescheduleVaccineUseCase.RescheduleVaccineCommand command) {
            return new RescheduleVaccineRequest(command.vaccineId().value(), command.nextDueDate());
        }

        static Stream<Arguments> invalidRequests() {
            var vaccineId = TestVaccineMother.DEFAULT_VACCINE_ID.value();

            return Stream.of(
                    arguments("vaccine id is null", new RescheduleVaccineRequest(null, NEW_DUE_DATE), "vaccineId"),
                    arguments("next due date is null", new RescheduleVaccineRequest(vaccineId, null), "nextDueDate"),
                    arguments("next due date is in the past", new RescheduleVaccineRequest(vaccineId, LocalDate.now().minusDays(1)), "nextDueDate")
            );
        }

    }

}
