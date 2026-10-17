package com.petmanagement.health.support;

import com.petmanagement.health.application.port.in.CreateWeightRecordUseCase;
import com.petmanagement.health.domain.model.aggregate.WeightRecord;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.health.domain.model.valueobject.Weight;

import java.time.LocalDate;

public final class WeightRecordTestBuilder {

    private PetId petId;
    private LocalDate weightDate;
    private Weight weight;

    private WeightRecordTestBuilder() {
        this.petId = TestPetIdMother.EXISTING_PET_ID;
        this.weightDate = TestCommonMother.EARLY_DATE;
        this.weight = TestWeightRecordMother.MEDIUM_WEIGHT;
    }

    public static WeightRecordTestBuilder aWeightRecord() {
        return new WeightRecordTestBuilder();
    }

    public WeightRecordTestBuilder withPetId(PetId petId) {
        this.petId = petId;
        return this;
    }

    public WeightRecordTestBuilder withPetId(String petId) {
        this.petId = PetId.of(petId);
        return this;
    }

    public WeightRecordTestBuilder withWeightDate(LocalDate weightDate) {
        this.weightDate = weightDate;
        return this;
    }

    public WeightRecordTestBuilder withWeight(Weight weight) {
        this.weight = weight;
        return this;
    }

    public WeightRecordTestBuilder withWeight(double weight) {
        this.weight = Weight.of(weight);
        return this;
    }

    public WeightRecord build() {
        return WeightRecord.create(petId, weightDate, weight);
    }

    public static class CreateWeightRecordCommandBuilder {

        private PetId petId;
        private LocalDate weightDate;
        private Weight weight;

        private CreateWeightRecordCommandBuilder() {
            this.petId = TestPetIdMother.EXISTING_PET_ID;
            this.weightDate = TestCommonMother.EARLY_DATE;
            this.weight = TestWeightRecordMother.MEDIUM_WEIGHT;
        }

        public static CreateWeightRecordCommandBuilder aCreateWeightRecordCommand() {
            return new CreateWeightRecordCommandBuilder();
        }

        public CreateWeightRecordCommandBuilder withPetId(PetId petId) {
            this.petId = petId;
            return this;
        }

        public CreateWeightRecordCommandBuilder withPetId(String petId) {
            this.petId = PetId.of(petId);
            return this;
        }

        public CreateWeightRecordCommandBuilder withWeightDate(LocalDate weightDate) {
            this.weightDate = weightDate;
            return this;
        }

        public CreateWeightRecordCommandBuilder withWeight(Weight weight) {
            this.weight = weight;
            return this;
        }

        public CreateWeightRecordCommandBuilder withWeight(double weight) {
            this.weight = Weight.of(weight);
            return this;
        }

        public CreateWeightRecordUseCase.CreateWeightRecordCommand build() {
            return new CreateWeightRecordUseCase.CreateWeightRecordCommand(petId, weightDate, weight);
        }

    }

}
