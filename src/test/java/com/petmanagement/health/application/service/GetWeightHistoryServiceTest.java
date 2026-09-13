package com.petmanagement.health.application.service;

import com.petmanagement.health.application.port.in.GetWeightHistoryUseCase;
import com.petmanagement.health.domain.exception.PetNotFoundException;
import com.petmanagement.health.support.InMemoryWeightRecordRepository;
import com.petmanagement.health.support.TestCommonMother;
import com.petmanagement.health.support.TestPetIdMother;
import com.petmanagement.health.support.WeightRecordTestBuilder;
import com.petmanagement.pets.api.PetApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetWeightHistoryServiceTest {

    private InMemoryWeightRecordRepository repository;
    private PetApi petApi;
    private GetWeightHistoryService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryWeightRecordRepository();
        petApi = mock(PetApi.class);

        service = new GetWeightHistoryService(repository, petApi);
    }

    @Nested
    class WhenRetrievingWeightHistory {

        @Test
        void shouldReturnWeightHistoryForPet() {
            // Given
            var petId = TestPetIdMother.EXISTING_PET_ID;

            var olderRecord = WeightRecordTestBuilder.aWeightRecord()
                    .withPetId(petId)
                    .withWeightDate(TestCommonMother.EARLY_DATE)
                    .build();

            var newerRecord = WeightRecordTestBuilder.aWeightRecord()
                    .withPetId(petId)
                    .withWeightDate(TestCommonMother.MIDDLE_DATE)
                    .build();

            repository.saveAll(olderRecord, newerRecord);

            var query = new GetWeightHistoryUseCase.GetWeightHistoryQuery(
                    petId, TestCommonMother.DEFAULT_PAGE_REQUEST
            );

            when(petApi.existsById(petId.value()))
                    .thenReturn(true);

            // When
            var result = service.execute(query);

            // Then
            assertEquals(2, result.content().size());
            assertEquals(2, result.totalElements());
            assertEquals(1, result.totalPages());
            assertEquals(0, result.page());
            assertEquals(10, result.size());

            assertEquals(TestCommonMother.MIDDLE_DATE, result.content().getFirst().getWeightDate());
            assertEquals(TestCommonMother.EARLY_DATE, result.content().getLast().getWeightDate());
        }

    }

    @Nested
    class WhenPetHasNoRecords {

        @Test
        void shouldReturnEmptyPageWhenPetHasNoRecords() {
            // Given
            var petId = TestPetIdMother.ANOTHER_PET_ID;
            var query = new GetWeightHistoryUseCase.GetWeightHistoryQuery(
                    petId, TestCommonMother.DEFAULT_PAGE_REQUEST
            );

            when(petApi.existsById(petId.value()))
                    .thenReturn(true);

            // When
            var result = service.execute(query);

            // Then
            assertTrue(result.isEmpty());
            assertEquals(0, result.totalElements());
            assertEquals(0, result.totalPages());
            assertEquals(0, result.numberOfElements());
        }

    }

    @Nested
    class WhenPetDoesNotExist {

        @Test
        void shouldThrowWhenPetDoesNotExist() {
            // Given
            var petId = TestPetIdMother.NON_EXISTENT_PET_ID;
            var query = new GetWeightHistoryUseCase.GetWeightHistoryQuery(
                    petId, TestCommonMother.DEFAULT_PAGE_REQUEST
            );

            when(petApi.existsById(petId.value()))
                    .thenReturn(false);

            // When/Then
            assertThrows(
                    PetNotFoundException.class,
                    () -> service.execute(query)
            );
        }

    }

}
