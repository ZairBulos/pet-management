package com.petmanagement.health.support;

import com.petmanagement.health.application.port.in.CreateDewormingUseCase;
import com.petmanagement.health.application.port.in.RescheduleDewormingUseCase;
import com.petmanagement.health.application.port.in.UpdateDewormingUseCase;
import com.petmanagement.health.domain.model.aggregate.Deworming;
import com.petmanagement.health.domain.model.valueobject.*;

import java.time.LocalDate;

public final class DewormingTestBuilder {

    private PetId petId;
    private LocalDate dewormingDate;
    private DrugName drugName;
    private DrugDose drugDose;
    private NextDueDate nextDueDate;

    private DewormingTestBuilder() {
        this.petId = TestPetIdMother.EXISTING_PET_ID;
        this.dewormingDate = TestDewormingMother.DEWORMING_DATE;
        this.drugName = TestDewormingMother.DRUG_NAME_DRONTAL;
        this.drugDose = TestDewormingMother.DRUG_DOSE_TABLET;
        this.nextDueDate = TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS;
    }

    public static DewormingTestBuilder aDeworming() {
        return new DewormingTestBuilder();
    }

    public DewormingTestBuilder withPetId(PetId petId) {
        this.petId = petId;
        return this;
    }

    public DewormingTestBuilder withPetId(String petId) {
        this.petId = PetId.of(petId);
        return this;
    }

    public DewormingTestBuilder withDewormingDate(LocalDate dewormingDate) {
        this.dewormingDate = dewormingDate;
        return this;
    }

    public DewormingTestBuilder withDrugName(DrugName drugName) {
        this.drugName = drugName;
        return this;
    }

    public DewormingTestBuilder withDrugName(String drugName) {
        this.drugName = new DrugName(drugName);
        return this;
    }

    public DewormingTestBuilder withDrugDose(DrugDose drugDose) {
        this.drugDose = drugDose;
        return this;
    }

    public DewormingTestBuilder withDrugDose(String drugDose) {
        this.drugDose = new DrugDose(drugDose);
        return this;
    }

    public DewormingTestBuilder withNextDueDate(NextDueDate nextDueDate) {
        this.nextDueDate = nextDueDate;
        return this;
    }

    public DewormingTestBuilder withNextDueDate(LocalDate nextDueDate) {
        this.nextDueDate = new NextDueDate(nextDueDate);
        return this;
    }

    public Deworming build() {
        return Deworming.create(
                petId,
                dewormingDate,
                drugName, drugDose,
                nextDueDate
        );
    }

    public static class CreateDewormingCommandBuilder {

        private PetId petId;
        private LocalDate dewormingDate;
        private DrugName drugName;
        private DrugDose drugDose;
        private NextDueDate nextDueDate;

        private CreateDewormingCommandBuilder() {
            this.petId = TestPetIdMother.EXISTING_PET_ID;
            this.dewormingDate = TestDewormingMother.DEWORMING_DATE;
            this.drugName = TestDewormingMother.DRUG_NAME_DRONTAL;
            this.drugDose = TestDewormingMother.DRUG_DOSE_TABLET;
            this.nextDueDate = TestDewormingMother.NEXT_DUE_DATE_THREE_MONTHS;
        }

        public static CreateDewormingCommandBuilder aCreateDewormingCommand() {
            return new CreateDewormingCommandBuilder();
        }

        public CreateDewormingCommandBuilder withPetId(PetId petId) {
            this.petId = petId;
            return this;
        }

        public CreateDewormingCommandBuilder withPetId(String petId) {
            this.petId = PetId.of(petId);
            return this;
        }

        public CreateDewormingCommandBuilder withDewormingDate(LocalDate dewormingDate) {
            this.dewormingDate = dewormingDate;
            return this;
        }

        public CreateDewormingCommandBuilder withDrugName(DrugName drugName) {
            this.drugName = drugName;
            return this;
        }

        public CreateDewormingCommandBuilder withDrugName(String drugName) {
            this.drugName = new DrugName(drugName);
            return this;
        }

        public CreateDewormingCommandBuilder withDrugDose(DrugDose drugDose) {
            this.drugDose = drugDose;
            return this;
        }

        public CreateDewormingCommandBuilder withDrugDose(String drugDose) {
            this.drugDose = new DrugDose(drugDose);
            return this;
        }

        public CreateDewormingCommandBuilder withNextDueDate(NextDueDate nextDueDate) {
            this.nextDueDate = nextDueDate;
            return this;
        }

        public CreateDewormingCommandBuilder withNextDueDate(LocalDate nextDueDate) {
            this.nextDueDate = new NextDueDate(nextDueDate);
            return this;
        }

        public CreateDewormingUseCase.CreateDewormingCommand build() {
            return new CreateDewormingUseCase.CreateDewormingCommand(
                    petId,
                    dewormingDate,
                    drugName,
                    drugDose,
                    nextDueDate
            );
        }

    }

    public static class UpdateDewormingCommandBuilder {

        private DewormingId dewormingId;
        private LocalDate dewormingDate;
        private DrugName drugName;
        private DrugDose drugDose;

        private UpdateDewormingCommandBuilder() {
            this.dewormingId = TestDewormingMother.DEFAULT_DEWORMING_ID;
            this.dewormingDate = TestCommonMother.UPDATE_DATE;
            this.drugName = TestDewormingMother.DRUG_NAME_ADVOCATE;
            this.drugDose = TestDewormingMother.DRUG_DOSE_LIQUID;
        }

        public static UpdateDewormingCommandBuilder aUpdateDewormingCommand() {
            return new UpdateDewormingCommandBuilder();
        }

        public UpdateDewormingCommandBuilder withId(DewormingId dewormingId) {
            this.dewormingId = dewormingId;
            return this;
        }

        public UpdateDewormingCommandBuilder withId(String dewormingId) {
            this.dewormingId = DewormingId.of(dewormingId);
            return this;
        }

        public UpdateDewormingCommandBuilder withDewormingDate(LocalDate dewormingDate) {
            this.dewormingDate = dewormingDate;
            return this;
        }

        public UpdateDewormingCommandBuilder withDrugName(DrugName drugName) {
            this.drugName = drugName;
            return this;
        }

        public UpdateDewormingCommandBuilder withDrugName(String drugName) {
            this.drugName = new DrugName(drugName);
            return this;
        }

        public UpdateDewormingCommandBuilder withDrugDose(DrugDose drugDose) {
            this.drugDose = drugDose;
            return this;
        }

        public UpdateDewormingCommandBuilder withDrugDose(String drugDose) {
            this.drugDose = new DrugDose(drugDose);
            return this;
        }

        public UpdateDewormingUseCase.UpdateDewormingCommand build() {
            return new UpdateDewormingUseCase.UpdateDewormingCommand(
                    dewormingId,
                    dewormingDate,
                    drugName,
                    drugDose
            );
        }

    }

    public static class RescheduleDewormingCommandBuilder {

        private DewormingId dewormingId;
        private LocalDate nextDueDate;

        private RescheduleDewormingCommandBuilder() {
            this.dewormingId = TestDewormingMother.DEFAULT_DEWORMING_ID;
            this.nextDueDate = TestDewormingMother.NEXT_DUE_DATE_SIX_MONTHS.value();
        }

        public static RescheduleDewormingCommandBuilder aRescheduleDewormingCommand() {
            return new RescheduleDewormingCommandBuilder();
        }

        public RescheduleDewormingCommandBuilder withId(DewormingId dewormingId) {
            this.dewormingId = dewormingId;
            return this;
        }

        public RescheduleDewormingCommandBuilder withId(String dewormingId) {
            this.dewormingId = DewormingId.of(dewormingId);
            return this;
        }

        public RescheduleDewormingCommandBuilder withNextDueDate(LocalDate nextDueDate) {
            this.nextDueDate = nextDueDate;
            return this;
        }

        public RescheduleDewormingUseCase.RescheduleDewormingCommand build() {
            return new RescheduleDewormingUseCase.RescheduleDewormingCommand(
                    dewormingId,
                    nextDueDate
            );
        }

    }

}
