package com.petmanagement.health.application.service;

import com.petmanagement.health.application.port.in.GetDewormingHistoryUseCase;
import com.petmanagement.health.domain.exception.PetNotFoundException;
import com.petmanagement.health.support.DewormingTestBuilder;
import com.petmanagement.health.support.InMemoryDewormingRepository;
import com.petmanagement.health.support.TestCommonMother;
import com.petmanagement.health.support.TestPetIdMother;
import com.petmanagement.pets.api.PetApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetDewormingHistoryServiceTest {

    private InMemoryDewormingRepository repository;
    private PetApi petApi;
    private GetDewormingHistoryService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryDewormingRepository();
        petApi = mock(PetApi.class);

        service = new GetDewormingHistoryService(repository, petApi);
    }

    @Nested
    class WhenRetrievingDewormingHistory {

        @Test
        void shouldReturnDewormingHistoryForPet() {
            // Given
            var petId = TestPetIdMother.EXISTING_PET_ID;

            var olderDeworming = DewormingTestBuilder.aDeworming()
                    .withPetId(petId)
                    .withDewormingDate(TestCommonMother.EARLY_DATE)
                    .build();

            var newerDeworming = DewormingTestBuilder.aDeworming()
                    .withPetId(petId)
                    .withDewormingDate(TestCommonMother.MIDDLE_DATE)
                    .build();

            repository.saveAll(olderDeworming, newerDeworming);

            var query = new GetDewormingHistoryUseCase.GetDewormingHistoryQuery(
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

            assertEquals(TestCommonMother.MIDDLE_DATE, result.content().getFirst().getDewormingDate());
            assertEquals(TestCommonMother.EARLY_DATE, result.content().getLast().getDewormingDate());
        }

    }

    @Nested
    class WhenPetHasNoRecords {

        @Test
        void shouldReturnEmptyPageWhenPetHasNoRecords() {
            // Given
            var petId = TestPetIdMother.ANOTHER_PET_ID;
            var query = new GetDewormingHistoryUseCase.GetDewormingHistoryQuery(
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
            var query = new GetDewormingHistoryUseCase.GetDewormingHistoryQuery(
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
