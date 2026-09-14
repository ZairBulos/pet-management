package com.petmanagement.pets.support;

import com.petmanagement.pets.application.port.in.CreatePetUseCase;
import com.petmanagement.pets.application.port.in.UpdatePetUseCase;
import com.petmanagement.pets.domain.model.aggregate.Pet;
import com.petmanagement.pets.domain.model.enums.Sex;
import com.petmanagement.pets.domain.model.valueobject.*;

import java.time.LocalDate;

public final class PetTestBuilder {

    private OwnerId ownerId;
    private PetName petName;
    private Species species;
    private Breed breed;
    private Coat coat;
    private Sex sex;
    private LocalDate birthDate;

    private PetTestBuilder() {
        this.ownerId = TestOwnerIdMother.EXISTING_OWNER_ID;
        this.petName = TestPetMother.PET_NAME_BUDDY;
        this.species = TestPetMother.SPECIES_DOG;
        this.breed = TestPetMother.BREED_GOLDEN_RETRIEVER;
        this.coat = TestPetMother.COAT_GOLDEN;
        this.sex = TestPetMother.SEX_MALE;
        this.birthDate = TestPetMother.BIRTH_DATE_STANDARD;
    }

    public static PetTestBuilder aPet() {
        return new PetTestBuilder();
    }

    public PetTestBuilder withOwnerId(OwnerId ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public PetTestBuilder withOwnerId(String ownerId) {
        this.ownerId = OwnerId.of(ownerId);
        return this;
    }

    public PetTestBuilder withPetName(PetName petName) {
        this.petName = petName;
        return this;
    }

    public PetTestBuilder withPetName(String petName) {
        this.petName = new PetName(petName);
        return this;
    }

    public PetTestBuilder withSpecies(Species species) {
        this.species = species;
        return this;
    }

    public PetTestBuilder withSpecies(String species) {
        this.species = new Species(species);
        return this;
    }

    public PetTestBuilder withBreed(Breed breed) {
        this.breed = breed;
        return this;
    }

    public PetTestBuilder withBreed(String breed) {
        this.breed = new Breed(breed);
        return this;
    }

    public PetTestBuilder withBreedUnknown() {
        this.breed = Breed.unknown();
        return this;
    }

    public PetTestBuilder withCoat(Coat coat) {
        this.coat = coat;
        return this;
    }

    public PetTestBuilder withCoat(String coat) {
        this.coat = new Coat(coat);
        return this;
    }

    public PetTestBuilder withSex(Sex sex) {
        this.sex = sex;
        return this;
    }

    public PetTestBuilder withBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public Pet build() {
        return Pet.create(ownerId, petName, species, breed, coat, sex, birthDate);
    }

    public static class CreatePetCommandBuilder {

        private OwnerId ownerId;
        private PetName petName;
        private Species species;
        private Breed breed;
        private Coat coat;
        private Sex sex;
        private LocalDate birthDate;

        private CreatePetCommandBuilder() {
            this.ownerId = TestOwnerIdMother.EXISTING_OWNER_ID;
            this.petName = TestPetMother.PET_NAME_BELLA;
            this.species = TestPetMother.SPECIES_CAT;
            this.breed = TestPetMother.BREED_SIAMESE;
            this.coat = TestPetMother.COAT_ORANGE_WHITE;
            this.sex = TestPetMother.SEX_FEMALE;
            this.birthDate = TestPetMother.BIRTH_DATE_STANDARD;
        }

        public static CreatePetCommandBuilder aCreatePetCommand() {
            return new CreatePetCommandBuilder();
        }

        public CreatePetCommandBuilder withOwnerId(OwnerId ownerId) {
            this.ownerId = ownerId;
            return this;
        }

        public CreatePetCommandBuilder withOwnerId(String ownerId) {
            this.ownerId = OwnerId.of(ownerId);
            return this;
        }

        public CreatePetCommandBuilder withPetName(PetName petName) {
            this.petName = petName;
            return this;
        }

        public CreatePetCommandBuilder withPetName(String petName) {
            this.petName = new PetName(petName);
            return this;
        }

        public CreatePetCommandBuilder withSpecies(Species species) {
            this.species = species;
            return this;
        }

        public CreatePetCommandBuilder withSpecies(String species) {
            this.species = new Species(species);
            return this;
        }

        public CreatePetCommandBuilder withBreed(Breed breed) {
            this.breed = breed;
            return this;
        }

        public CreatePetCommandBuilder withBreed(String breed) {
            this.breed = new Breed(breed);
            return this;
        }

        public CreatePetCommandBuilder withCoat(Coat coat) {
            this.coat = coat;
            return this;
        }

        public CreatePetCommandBuilder withCoat(String coat) {
            this.coat = new Coat(coat);
            return this;
        }

        public CreatePetCommandBuilder withSex(Sex sex) {
            this.sex = sex;
            return this;
        }

        public CreatePetCommandBuilder withBirthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public CreatePetUseCase.CreatePetCommand build() {
            return new CreatePetUseCase.CreatePetCommand(ownerId, petName, species, breed, coat, sex, birthDate);
        }

    }

    public static class UpdatePetCommandBuilder {

        private PetId petId;
        private PetName petName;

        private UpdatePetCommandBuilder() {
            this.petId = TestPetMother.DEFAULT_PET_ID;
            this.petName = TestPetMother.PET_NAME_CHARLIE;
        }

        public static UpdatePetCommandBuilder aUpdatePetCommand() {
            return new UpdatePetCommandBuilder();
        }

        public UpdatePetCommandBuilder withId(PetId petId) {
            this.petId = petId;
            return this;
        }

        public UpdatePetCommandBuilder withId(String petId) {
            this.petId = PetId.of(petId);
            return this;
        }

        public UpdatePetCommandBuilder withPetName(PetName petName) {
            this.petName = petName;
            return this;
        }

        public UpdatePetCommandBuilder withPetName(String petName) {
            this.petName = new PetName(petName);
            return this;
        }

        public UpdatePetUseCase.UpdatePetCommand build() {
            return new UpdatePetUseCase.UpdatePetCommand(petId, petName);
        }

    }

}
