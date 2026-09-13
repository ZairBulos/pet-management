package com.petmanagement.health.application.service;

import com.petmanagement.health.application.port.in.GetVaccineHistoryUseCase;
import com.petmanagement.health.domain.exception.PetNotFoundException;
import com.petmanagement.health.support.*;
import com.petmanagement.pets.api.PetApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetVaccineHistoryServiceTest {

    private InMemoryVaccineRepository repository;
    private PetApi petApi;
    private GetVaccineHistoryService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryVaccineRepository();
        petApi = mock(PetApi.class);

        service = new GetVaccineHistoryService(repository, petApi);
    }

    @Nested
    class WhenRetrievingVaccineHistory {

        @Test
        void shouldReturnVaccineHistoryForPet() {
            // Given
            var petId = TestPetIdMother.EXISTING_PET_ID;

            var olderVaccine = VaccineTestBuilder.aVaccine()
                    .withPetId(petId)
                    .withVaccinationDate(TestCommonMother.EARLY_DATE)
                    .build();

            var newerVaccine = VaccineTestBuilder.aVaccine()
                    .withPetId(petId)
                    .withVaccinationDate(TestCommonMother.MIDDLE_DATE)
                    .build();

            repository.saveAll(olderVaccine, newerVaccine);

            var query = new GetVaccineHistoryUseCase.GetVaccineHistoryQuery(
                    petId, TestCommonMother.DEFAULT_PAGE_REQUEST
            );

            when(petApi.existsById(query.petId().value()))
                    .thenReturn(true);

            // When
            var result = service.execute(query);

            // Then
            assertEquals(2, result.content().size());
            assertEquals(2, result.totalElements());
            assertEquals(1, result.totalPages());
            assertEquals(0, result.page());
            assertEquals(10, result.size());

            assertEquals(TestCommonMother.MIDDLE_DATE, result.content().getFirst().getVaccinationDate());
            assertEquals(TestCommonMother.EARLY_DATE, result.content().getLast().getVaccinationDate());
        }

    }

    @Nested
    class WhenPetHasNoRecords {

        @Test
        void shouldReturnEmptyPageWhenPetHasNoRecords() {
            // Given
            var petId = TestPetIdMother.ANOTHER_PET_ID;
            var query = new GetVaccineHistoryUseCase.GetVaccineHistoryQuery(
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
            var query = new GetVaccineHistoryUseCase.GetVaccineHistoryQuery(
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
