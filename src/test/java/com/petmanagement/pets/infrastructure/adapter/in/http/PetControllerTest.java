package com.petmanagement.pets.infrastructure.adapter.in.http;

import com.petmanagement.pets.application.port.in.CreatePetUseCase;
import com.petmanagement.pets.domain.model.valueobject.Breed;
import com.petmanagement.pets.domain.model.valueobject.OwnerId;
import com.petmanagement.pets.infrastructure.adapter.in.http.dto.request.CreatePetRequest;
import com.petmanagement.pets.infrastructure.adapter.in.http.mapper.PetHttpMapper;
import com.petmanagement.pets.support.TestOwnerIdMother;
import com.petmanagement.pets.support.TestPetMother;
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

import static com.petmanagement.pets.support.PetTestBuilder.CreatePetCommandBuilder.aCreatePetCommand;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PetController.class)
@Import(PetHttpMapper.class)
class PetControllerTest extends ControllerTestSupport {

    @MockitoBean
    private CreatePetUseCase createPetUseCase;

    private static final String PETS = PetController.PETS;

    private static final OwnerId AUTHENTICATED_OWNER_ID = TestOwnerIdMother.EXISTING_OWNER_ID;

    @Nested
    class Create {

        @Test
        void shouldReturn201WithLocation() throws Exception {
            // Given
            var command = aCreatePetCommand().withOwnerId(AUTHENTICATED_OWNER_ID).build();
            var request = toRequest(command);

            given(createPetUseCase.execute(command))
                    .willReturn(TestPetMother.DEFAULT_PET_ID);

            // When/Then
            performAuthenticatedPost(PETS, request)
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", PETS + "/" + TestPetMother.DEFAULT_PET_ID.value()))
                    .andExpect(content().string(""));

            verify(createPetUseCase).execute(command);
        }

        @Test
        void shouldReturn201WhenBreedIsUnknown() throws Exception {
            // Given
            var command = aCreatePetCommand()
                    .withOwnerId(AUTHENTICATED_OWNER_ID)
                    .withBreed(Breed.unknown())
                    .build();

            given(createPetUseCase.execute(command))
                    .willReturn(TestPetMother.DEFAULT_PET_ID);

            // When/Then
            performAuthenticatedPost(PETS, toRequest(command))
                    .andExpect(status().isCreated());

            verify(createPetUseCase).execute(command);
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("invalidRequests")
        void shouldReturn400WhenFieldIsInvalid(
                String description, CreatePetRequest request, String field
        ) throws Exception {
            // When/Then
            performAuthenticatedPost(PETS, request)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));

            verifyNoInteractions(createPetUseCase);
        }

        @Test
        void shouldReturn401WhenNoAuthenticationIsProvided() throws Exception {
            // Given
            var request = toRequest(aCreatePetCommand().build());

            // When/Then
            performPost(PETS, request)
                    .andExpect(status().isUnauthorized());

            verifyNoInteractions(createPetUseCase);
        }

        // === Helpers ===

        private CreatePetRequest toRequest(CreatePetUseCase.CreatePetCommand command) {
            return new CreatePetRequest(
                    command.name().value(),
                    command.species().value(),
                    command.breed().value(),
                    command.coat().value(),
                    command.sex(),
                    command.birthDate()
            );
        }

        static Stream<Arguments> invalidRequests() {
            var name = TestPetMother.PET_NAME_BELLA.value();
            var species = TestPetMother.SPECIES_CAT.value();
            var breed = TestPetMother.BREED_SIAMESE.value();
            var coat = TestPetMother.COAT_ORANGE_WHITE.value();
            var sex = TestPetMother.SEX_FEMALE;
            var birthDate = TestPetMother.BIRTH_DATE_STANDARD;

            return Stream.of(
                    // name
                    arguments("name is blank", new CreatePetRequest("   ", species, breed, coat, sex, birthDate), "name"),
                    arguments("name has 1 char after trim", new CreatePetRequest("  A  ", species, breed, coat, sex, birthDate), "name"),
                    arguments("name exceeds 100 chars", new CreatePetRequest("a".repeat(101), species, breed, coat, sex, birthDate), "name"),

                    // species
                    arguments("species is null", new CreatePetRequest(name, null, breed, coat, sex, birthDate), "species"),
                    arguments("species is blank", new CreatePetRequest(name, "   ", breed, coat, sex, birthDate), "species"),
                    arguments("species exceeds 60 chars", new CreatePetRequest(name, "a".repeat(61), breed, coat, sex, birthDate), "species"),

                    // breed
                    arguments("breed exceeds 100 chars", new CreatePetRequest(name, species, "a".repeat(101), coat, sex, birthDate), "breed"),

                    // coat
                    arguments("coat is null", new CreatePetRequest(name, species, breed, null, sex, birthDate), "coat"),
                    arguments("coat is blank", new CreatePetRequest(name, species, breed, "   ", sex, birthDate), "coat"),
                    arguments("coat exceeds 100 chars", new CreatePetRequest(name, species, breed, "a".repeat(101), sex, birthDate), "coat"),

                    // sex
                    arguments("sex is null", new CreatePetRequest(name, species, breed, coat, null, birthDate), "sex"),

                    // birthDate
                    arguments("birth date is null", new CreatePetRequest(name, species, breed, coat, sex, null), "birthDate"),
                    arguments("birth date is in the future", new CreatePetRequest(name, species, breed, coat, sex, LocalDate.now().plusDays(1)), "birthDate")
            );
        }

    }

}
