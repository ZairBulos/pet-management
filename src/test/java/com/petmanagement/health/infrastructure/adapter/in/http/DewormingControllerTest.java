package com.petmanagement.health.infrastructure.adapter.in.http;

import com.petmanagement.health.application.port.in.CreateDewormingUseCase;
import com.petmanagement.health.application.port.in.GetDewormingHistoryUseCase;
import com.petmanagement.health.domain.exception.PetNotFoundException;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.health.infrastructure.adapter.in.http.dto.request.CreateDewormingRequest;
import com.petmanagement.health.infrastructure.adapter.in.http.mapper.DewormingHttpMapper;
import com.petmanagement.health.support.TestCommonMother;
import com.petmanagement.health.support.TestDewormingMother;
import com.petmanagement.health.support.TestPetIdMother;
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

import java.util.List;
import java.util.stream.Stream;

import static com.petmanagement.health.support.DewormingTestBuilder.CreateDewormingCommandBuilder.aCreateDewormingCommand;
import static com.petmanagement.health.support.DewormingTestBuilder.aDeworming;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DewormingController.class)
@Import(DewormingHttpMapper.class)
class DewormingControllerTest extends ControllerTestSupport {

    @MockitoBean
    private CreateDewormingUseCase createDewormingUseCase;

    @MockitoBean
    private GetDewormingHistoryUseCase getDewormingHistoryUseCase;

    private static final String DEWORMINGS = DewormingController.DEWORMINGS;

    @Nested
    class Create {

        @Test
        void shouldReturn201WithLocation() throws Exception {
            // Given
            var command = aCreateDewormingCommand().build();
            var request = toRequest(command);

            given(createDewormingUseCase.execute(command))
                    .willReturn(TestDewormingMother.DEFAULT_DEWORMING_ID);

            // When/Then
            performAuthenticatedPost(DEWORMINGS, request)
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", DEWORMINGS));

            verify(createDewormingUseCase).execute(command);
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("invalidRequests")
        void shouldReturn400WhenFieldIsInvalid(
                String description, CreateDewormingRequest request, String field
        ) throws Exception {
            // When/Then
            performAuthenticatedPost(DEWORMINGS, request)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));

            verifyNoInteractions(createDewormingUseCase);
        }

        @Test
        void shouldReturn404WhenPetDoesNotExist() throws Exception {
            // Given
            var request = toRequest(aCreateDewormingCommand().build());

            given(createDewormingUseCase.execute(any()))
                    .willThrow(new PetNotFoundException());

            // When/Then
            performAuthenticatedPost(DEWORMINGS, request)
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value("PET_NOT_FOUND"));
        }

        @Test
        void shouldReturn401WhenNoAuthenticationIsProvided() throws Exception {
            // Given
            var request = toRequest(aCreateDewormingCommand().build());

            // When/Then
            performPost(DEWORMINGS, request)
                    .andExpect(status().isUnauthorized());

            verifyNoInteractions(createDewormingUseCase);
        }

        // === Helpers ===

        private CreateDewormingRequest toRequest(CreateDewormingUseCase.CreateDewormingCommand command) {
            return new CreateDewormingRequest(
                    command.petId().value(),
                    command.dewormingDate(),
                    command.drugName().value(),
                    command.drugDose().value(),
                    command.nextDueDate().value()
            );
        }

        static Stream<Arguments> invalidRequests() {
            var petId = TestPetIdMother.EXISTING_PET_ID.value();
            var date = TestDewormingMother.DEWORMING_DATE;
            var drug = TestDewormingMother.DRUG_NAME_DRONTAL.value();
            var dose = TestDewormingMother.DRUG_DOSE_TABLET.value();
            var dueDate = TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS.value();

            return Stream.of(
                    // petId
                    arguments("pet id is null", new CreateDewormingRequest(null, date, drug, dose, dueDate), "petId"),

                    // dewormingDate
                    arguments("deworming date is null", new CreateDewormingRequest(petId, null, drug, dose, dueDate), "dewormingDate"),
                    arguments("deworming date is in the future", new CreateDewormingRequest(petId, TestCommonMother.FUTURE_DATE, drug, dose, dueDate), "dewormingDate"),

                    // drugName
                    arguments("drug name is null", new CreateDewormingRequest(petId, date, null, dose, dueDate), "drugName"),
                    arguments("drug name is empty", new CreateDewormingRequest(petId, date, "", dose, dueDate), "drugName"),
                    arguments("drug name is blank", new CreateDewormingRequest(petId, date, "   ", dose, dueDate), "drugName"),
                    arguments("drug name exceeds 100 chars", new CreateDewormingRequest(petId, date, "a".repeat(101), dose, dueDate), "drugName"),

                    // drugDose
                    arguments("drug dose is null", new CreateDewormingRequest(petId, date, drug, null, dueDate), "drugDose"),
                    arguments("drug dose is empty", new CreateDewormingRequest(petId, date, drug, "", dueDate), "drugDose"),
                    arguments("drug dose is blank", new CreateDewormingRequest(petId, date, drug, "   ", dueDate), "drugDose"),
                    arguments("drug dose exceeds 100 chars", new CreateDewormingRequest(petId, date, drug, "a".repeat(101), dueDate), "drugDose"),

                    // nextDueDate
                    arguments("next due date is null", new CreateDewormingRequest(petId, date, drug, dose, null), "nextDueDate"),
                    arguments("next due date is in the past", new CreateDewormingRequest(petId, date, drug, dose, TestCommonMother.EARLY_DATE), "nextDueDate")
            );
        }

    }

    @Nested
    class History {

        private static final PetId PET_ID = TestPetIdMother.EXISTING_PET_ID;

        @Test
        void shouldReturn200WithDewormingHistory() throws Exception {
            // Given
            var deworming = aDeworming().withPetId(PET_ID).build();
            var query = new GetDewormingHistoryUseCase.GetDewormingHistoryQuery(PET_ID, PageRequest.of(0, 10));

            given(getDewormingHistoryUseCase.execute(query))
                    .willReturn(PageResponse.of(List.of(deworming), 0, 10, 1));

            // When/Then
            performAuthenticatedGet(DEWORMINGS + "?petId=" + PET_ID.value() + "&page=0&size=10")
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content.length()").value(1))
                    .andExpect(jsonPath("$.content[0].id").value(deworming.id().value().toString()))
                    .andExpect(jsonPath("$.content[0].dewormingDate").value(deworming.dewormingDate().toString()))
                    .andExpect(jsonPath("$.content[0].drugName").value(deworming.drugName().value()))
                    .andExpect(jsonPath("$.content[0].drugDose").value(deworming.drugDose().value()))
                    .andExpect(jsonPath("$.content[0].nextDueDate").value(deworming.nextDueDate().value().toString()))
                    .andExpect(jsonPath("$.page").value(0))
                    .andExpect(jsonPath("$.size").value(10))
                    .andExpect(jsonPath("$.totalElements").value(1))
                    .andExpect(jsonPath("$.totalPages").value(1));

            verify(getDewormingHistoryUseCase).execute(query);
        }

        @Test
        void shouldUseDefaultPaginationWhenNotProvided() throws Exception {
            // Given
            var query = new GetDewormingHistoryUseCase.GetDewormingHistoryQuery(PET_ID, PageRequest.of(0, 10));

            given(getDewormingHistoryUseCase.execute(query))
                    .willReturn(PageResponse.empty(0, 10));

            // When/Then
            performAuthenticatedGet(DEWORMINGS + "?petId=" + PET_ID.value())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.length()").value(0))
                    .andExpect(jsonPath("$.totalElements").value(0))
                    .andExpect(jsonPath("$.totalPages").value(0));

            verify(getDewormingHistoryUseCase).execute(query);
        }

        @Test
        void shouldUseProvidedPaginationParams() throws Exception {
            // Given
            var query = new GetDewormingHistoryUseCase.GetDewormingHistoryQuery(PET_ID, PageRequest.of(2, 5));

            given(getDewormingHistoryUseCase.execute(query))
                    .willReturn(PageResponse.of(List.of(), 2, 5, 11));

            // When/Then
            performAuthenticatedGet(DEWORMINGS + "?petId=" + PET_ID.value() + "&page=2&size=5")
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.page").value(2))
                    .andExpect(jsonPath("$.size").value(5))
                    .andExpect(jsonPath("$.totalPages").value(3));

            verify(getDewormingHistoryUseCase).execute(query);
        }

        @Test
        void shouldReturn400WhenPetIdIsMissing() throws Exception {
            // When/Then
            performAuthenticatedGet(DEWORMINGS)
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(getDewormingHistoryUseCase);
        }

        @Test
        void shouldReturn400WhenPetIdIsNotAUuid() throws Exception {
            // When/Then
            performAuthenticatedGet(DEWORMINGS + "?petId=not-a-uuid")
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(getDewormingHistoryUseCase);
        }

        @ParameterizedTest(name = "page={0}, size={1}")
        @CsvSource({"-1, 10", "0, 0", "0, -5"})
        void shouldReturn400WhenPaginationIsInvalid(int page, int size) throws Exception {
            // When/Then
            performAuthenticatedGet(DEWORMINGS + "?petId=" + PET_ID.value() + "&page=" + page + "&size=" + size)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));

            verifyNoInteractions(getDewormingHistoryUseCase);
        }

        @Test
        void shouldReturn404WhenPetDoesNotExist() throws Exception {
            // Given
            given(getDewormingHistoryUseCase.execute(any()))
                    .willThrow(new PetNotFoundException());

            // When/Then
            performAuthenticatedGet(DEWORMINGS + "?petId=" + PET_ID.value())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value("PET_NOT_FOUND"));
        }

        @Test
        void shouldReturn401WhenNoAuthenticationIsProvided() throws Exception {
            // When/Then
            performGet(DEWORMINGS + "?petId=" + PET_ID.value())
                    .andExpect(status().isUnauthorized());

            verifyNoInteractions(getDewormingHistoryUseCase);
        }

    }

}
