package com.petmanagement.health.infrastructure.adapter.in.http;

import com.petmanagement.health.application.port.in.CreateWeightRecordUseCase;
import com.petmanagement.health.application.port.in.GetWeightHistoryUseCase;
import com.petmanagement.health.domain.exception.PetNotFoundException;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.health.infrastructure.adapter.in.http.dto.request.CreateWeightRecordRequest;
import com.petmanagement.health.infrastructure.adapter.in.http.mapper.WeightRecordHttpMapper;
import com.petmanagement.health.support.TestCommonMother;
import com.petmanagement.health.support.TestPetIdMother;
import com.petmanagement.health.support.TestWeightRecordMother;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static com.petmanagement.health.support.WeightRecordTestBuilder.CreateWeightRecordCommandBuilder.aCreateWeightRecordCommand;
import static com.petmanagement.health.support.WeightRecordTestBuilder.aWeightRecord;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = WeightRecordController.class)
@Import(WeightRecordHttpMapper.class)
class WeightRecordControllerTest extends ControllerTestSupport {

    @MockitoBean
    private CreateWeightRecordUseCase createWeightRecordUseCase;

    @MockitoBean
    private GetWeightHistoryUseCase getWeightHistoryUseCase;

    private static final String WEIGHT_RECORDS = WeightRecordController.WEIGHT_RECORDS;

    @Nested
    class Create {

        @Test
        void shouldReturn201WithLocation() throws Exception {
            // Given
            var command = aCreateWeightRecordCommand().build();
            var request = toRequest(command);

            given(createWeightRecordUseCase.execute(command))
                    .willReturn(TestWeightRecordMother.DEFAULT_WEIGHT_RECORD_ID);

            // When/Then
            performAuthenticatedPost(WEIGHT_RECORDS, request)
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", WEIGHT_RECORDS));

            verify(createWeightRecordUseCase).execute(command);
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("invalidRequests")
        void shouldReturn400WhenFieldIsInvalid(
                String description, CreateWeightRecordRequest request, String field
        ) throws Exception {
            // When/Then
            performAuthenticatedPost(WEIGHT_RECORDS, request)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));

            verifyNoInteractions(createWeightRecordUseCase);
        }

        @Test
        void shouldReturn404WhenPetDoesNotExist() throws Exception {
            // Given
            var request = toRequest(aCreateWeightRecordCommand().build());

            given(createWeightRecordUseCase.execute(any()))
                    .willThrow(new PetNotFoundException());

            // When/Then
            performAuthenticatedPost(WEIGHT_RECORDS, request)
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value("PET_NOT_FOUND"));
        }

        @Test
        void shouldReturn401WhenNoAuthenticationIsProvided() throws Exception {
            // Given
            var request = toRequest(aCreateWeightRecordCommand().build());

            // When/Then
            performPost(WEIGHT_RECORDS, request)
                    .andExpect(status().isUnauthorized());

            verifyNoInteractions(createWeightRecordUseCase);
        }

        // === Helpers ===

        private CreateWeightRecordRequest toRequest(CreateWeightRecordUseCase.CreateWeightRecordCommand command) {
            return new CreateWeightRecordRequest(
                    command.petId().value(),
                    command.weightDate(),
                    command.weight().value()
            );
        }

        static Stream<Arguments> invalidRequests() {
            var petId = TestPetIdMother.EXISTING_PET_ID.value();
            var date = TestCommonMother.EARLY_DATE;
            var weight = TestWeightRecordMother.MEDIUM_WEIGHT.value();

            return Stream.of(
                    // petId
                    arguments("pet id is null", new CreateWeightRecordRequest(null, date, weight), "petId"),

                    // weightDate
                    arguments("weight date is null", new CreateWeightRecordRequest(petId, null, weight), "weightDate"),
                    arguments("weight date is in the future", new CreateWeightRecordRequest(petId, LocalDate.now().plusDays(1), weight), "weightDate"),

                    // weight
                    arguments("weight is null", new CreateWeightRecordRequest(petId, date, null), "weight"),
                    arguments("weight is zero", new CreateWeightRecordRequest(petId, date, BigDecimal.ZERO), "weight"),
                    arguments("weight is negative", new CreateWeightRecordRequest(petId, date, BigDecimal.valueOf(-1)), "weight"),
                    arguments("weight rounds to zero", new CreateWeightRecordRequest(petId, date, new BigDecimal("0.001")), "weight")
            );
        }

    }

    @Nested
    class History {

        private static final PetId PET_ID = TestPetIdMother.EXISTING_PET_ID;

        @Test
        void shouldReturn200WithWeightHistory() throws Exception {
            // Given
            var weightRecord = aWeightRecord().withPetId(PET_ID).build();
            var query = new GetWeightHistoryUseCase.GetWeightHistoryQuery(PET_ID, PageRequest.of(0, 10));

            given(getWeightHistoryUseCase.execute(query))
                    .willReturn(PageResponse.of(List.of(weightRecord), 0, 10, 1));

            // When/Then
            performAuthenticatedGet(WEIGHT_RECORDS + "?petId=" + PET_ID.value() + "&page=0&size=10")
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content.length()").value(1))
                    .andExpect(jsonPath("$.content[0].id").value(weightRecord.id().value().toString()))
                    .andExpect(jsonPath("$.content[0].weightDate").value(weightRecord.weightDate().toString()))
                    .andExpect(jsonPath("$.content[0].weight").value(weightRecord.weight().value().doubleValue()))
                    .andExpect(jsonPath("$.page").value(0))
                    .andExpect(jsonPath("$.size").value(10))
                    .andExpect(jsonPath("$.totalElements").value(1))
                    .andExpect(jsonPath("$.totalPages").value(1));

            verify(getWeightHistoryUseCase).execute(query);
        }

        @Test
        void shouldUseDefaultPaginationWhenNotProvided() throws Exception {
            // Given
            var query = new GetWeightHistoryUseCase.GetWeightHistoryQuery(PET_ID, PageRequest.of(0, 10));

            given(getWeightHistoryUseCase.execute(query))
                    .willReturn(PageResponse.empty(0, 10));

            // When/Then
            performAuthenticatedGet(WEIGHT_RECORDS + "?petId=" + PET_ID.value())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.length()").value(0))
                    .andExpect(jsonPath("$.totalElements").value(0))
                    .andExpect(jsonPath("$.totalPages").value(0));

            verify(getWeightHistoryUseCase).execute(query);
        }

        @Test
        void shouldUseProvidedPaginationParams() throws Exception {
            // Given
            var query = new GetWeightHistoryUseCase.GetWeightHistoryQuery(PET_ID, PageRequest.of(2, 5));

            given(getWeightHistoryUseCase.execute(query))
                    .willReturn(PageResponse.of(List.of(), 2, 5, 11));

            // When/Then
            performAuthenticatedGet(WEIGHT_RECORDS + "?petId=" + PET_ID.value() + "&page=2&size=5")
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.page").value(2))
                    .andExpect(jsonPath("$.size").value(5))
                    .andExpect(jsonPath("$.totalPages").value(3));

            verify(getWeightHistoryUseCase).execute(query);
        }

        @Test
        void shouldReturn400WhenPetIdIsMissing() throws Exception {
            // When/Then
            performAuthenticatedGet(WEIGHT_RECORDS)
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(getWeightHistoryUseCase);
        }

        @Test
        void shouldReturn400WhenPetIdIsNotAUuid() throws Exception {
            // When/Then
            performAuthenticatedGet(WEIGHT_RECORDS + "?petId=not-a-uuid")
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(getWeightHistoryUseCase);
        }

        @ParameterizedTest(name = "page={0}, size={1}")
        @CsvSource({"-1, 10", "0, 0", "0, -5"})
        void shouldReturn400WhenPaginationIsInvalid(int page, int size) throws Exception {
            // When/Then
            performAuthenticatedGet(WEIGHT_RECORDS + "?petId=" + PET_ID.value() + "&page=" + page + "&size=" + size)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));

            verifyNoInteractions(getWeightHistoryUseCase);
        }

        @Test
        void shouldReturn404WhenPetDoesNotExist() throws Exception {
            // Given
            given(getWeightHistoryUseCase.execute(any()))
                    .willThrow(new PetNotFoundException());

            // When/Then
            performAuthenticatedGet(WEIGHT_RECORDS + "?petId=" + PET_ID.value())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value("PET_NOT_FOUND"));
        }

        @Test
        void shouldReturn401WhenNoAuthenticationIsProvided() throws Exception {
            // When/Then
            performGet(WEIGHT_RECORDS + "?petId=" + PET_ID.value())
                    .andExpect(status().isUnauthorized());

            verifyNoInteractions(getWeightHistoryUseCase);
        }

    }

}
