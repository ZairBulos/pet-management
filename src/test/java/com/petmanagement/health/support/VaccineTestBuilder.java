package com.petmanagement.health.support;

import com.petmanagement.health.application.port.in.CreateVaccineUseCase;
import com.petmanagement.health.application.port.in.RescheduleVaccineUseCase;
import com.petmanagement.health.application.port.in.UpdateVaccineUseCase;
import com.petmanagement.health.domain.model.aggregate.Vaccine;
import com.petmanagement.health.domain.model.valueobject.NextDueDate;
import com.petmanagement.health.domain.model.valueobject.PetId;
import com.petmanagement.health.domain.model.valueobject.VaccineId;
import com.petmanagement.health.domain.model.valueobject.VaccineName;

import java.time.LocalDate;

public final class VaccineTestBuilder {

    private PetId petId;
    private LocalDate vaccinationDate;
    private VaccineName vaccineName;
    private NextDueDate nextDueDate;

    private VaccineTestBuilder() {
        this.petId = TestPetIdMother.EXISTING_PET_ID;
        this.vaccinationDate = TestVaccineMother.VACCINE_VACCINATION_DATE;
        this.vaccineName = TestVaccineMother.VACCINE_NAME_RABIES;
        this.nextDueDate = TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR;
    }

    public static VaccineTestBuilder aVaccine() {
        return new VaccineTestBuilder();
    }

    public VaccineTestBuilder withPetId(PetId petId) {
        this.petId = petId;
        return this;
    }

    public VaccineTestBuilder withPetId(String petId) {
        this.petId = PetId.of(petId);
        return this;
    }

    public VaccineTestBuilder withVaccinationDate(LocalDate vaccinationDate) {
        this.vaccinationDate = vaccinationDate;
        return this;
    }

    public VaccineTestBuilder withVaccineName(VaccineName vaccineName) {
        this.vaccineName = vaccineName;
        return this;
    }

    public VaccineTestBuilder withVaccineName(String vaccineName) {
        this.vaccineName = new VaccineName(vaccineName);
        return this;
    }

    public VaccineTestBuilder withNextDueDate(NextDueDate nextDueDate) {
        this.nextDueDate = nextDueDate;
        return this;
    }

    public VaccineTestBuilder withNextDueDate(LocalDate nextDueDate) {
        this.nextDueDate = new NextDueDate(nextDueDate);
        return this;
    }

    public Vaccine build() {
        return Vaccine.create(petId, vaccinationDate, vaccineName, nextDueDate);
    }

    public static class CreateVaccineCommandBuilder {

        private PetId petId;
        private LocalDate vaccinationDate;
        private VaccineName vaccineName;
        private NextDueDate nextDueDate;

        private CreateVaccineCommandBuilder() {
            this.petId = TestPetIdMother.EXISTING_PET_ID;
            this.vaccinationDate = TestVaccineMother.VACCINE_VACCINATION_DATE;
            this.vaccineName = TestVaccineMother.VACCINE_NAME_RABIES;
            this.nextDueDate = TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR;
        }

        public static CreateVaccineCommandBuilder aCreateVaccineCommand() {
            return new CreateVaccineCommandBuilder();
        }

        public CreateVaccineCommandBuilder withPetId(PetId petId) {
            this.petId = petId;
            return this;
        }

        public CreateVaccineCommandBuilder withPetId(String petId) {
            this.petId = PetId.of(petId);
            return this;
        }

        public CreateVaccineCommandBuilder withVaccinationDate(LocalDate vaccinationDate) {
            this.vaccinationDate = vaccinationDate;
            return this;
        }

        public CreateVaccineCommandBuilder withVaccineName(VaccineName vaccineName) {
            this.vaccineName = vaccineName;
            return this;
        }

        public CreateVaccineCommandBuilder withVaccineName(String vaccineName) {
            this.vaccineName = new VaccineName(vaccineName);
            return this;
        }

        public CreateVaccineCommandBuilder withNextDueDate(NextDueDate nextDueDate) {
            this.nextDueDate = nextDueDate;
            return this;
        }

        public CreateVaccineCommandBuilder withNextDueDate(LocalDate nextDueDate) {
            this.nextDueDate = new NextDueDate(nextDueDate);
            return this;
        }

        public CreateVaccineUseCase.CreateVaccineCommand build() {
            return new CreateVaccineUseCase.CreateVaccineCommand(petId, vaccinationDate, vaccineName, nextDueDate);
        }

    }

    public static class UpdateVaccineCommandBuilder {

        private VaccineId vaccineId;
        private LocalDate vaccinationDate;
        private VaccineName vaccineName;

        private UpdateVaccineCommandBuilder() {
            this.vaccineId = TestVaccineMother.DEFAULT_VACCINE_ID;
            this.vaccinationDate = TestCommonMother.UPDATE_DATE;
            this.vaccineName = TestVaccineMother.VACCINE_NAME_RABIES;
        }

        public static UpdateVaccineCommandBuilder aUpdateVaccineCommand() {
            return new UpdateVaccineCommandBuilder();
        }

        public UpdateVaccineCommandBuilder withId(VaccineId vaccineId) {
            this.vaccineId = vaccineId;
            return this;
        }

        public UpdateVaccineCommandBuilder withId(String vaccineId) {
            this.vaccineId = VaccineId.of(vaccineId);
            return this;
        }

        public UpdateVaccineCommandBuilder withVaccinationDate(LocalDate vaccinationDate) {
            this.vaccinationDate = vaccinationDate;
            return this;
        }

        public UpdateVaccineCommandBuilder withVaccineName(VaccineName vaccineName) {
            this.vaccineName = vaccineName;
            return this;
        }

        public UpdateVaccineCommandBuilder withVaccineName(String vaccineName) {
            this.vaccineName = new VaccineName(vaccineName);
            return this;
        }

        public UpdateVaccineUseCase.UpdateVaccineCommand build() {
            return new UpdateVaccineUseCase.UpdateVaccineCommand(vaccineId, vaccinationDate, vaccineName);
        }

    }

    public static class RescheduleVaccineCommandBuilder {

        private VaccineId vaccineId;
        private LocalDate nextDueDate;

        private RescheduleVaccineCommandBuilder() {
            this.vaccineId = TestVaccineMother.DEFAULT_VACCINE_ID;
            this.nextDueDate = TestVaccineMother.NEXT_DUE_DATE_ONE_YEAR.value();
        }

        public RescheduleVaccineCommandBuilder withId(VaccineId vaccineId) {
            this.vaccineId = vaccineId;
            return this;
        }

        public RescheduleVaccineCommandBuilder withId(String vaccineId) {
            this.vaccineId = VaccineId.of(vaccineId);
            return this;
        }

        public RescheduleVaccineCommandBuilder withNextDueDate(LocalDate nextDueDate) {
            this.nextDueDate = nextDueDate;
            return this;
        }

        public static RescheduleVaccineCommandBuilder aRescheduleVaccineCommand() {
            return new RescheduleVaccineCommandBuilder();
        }

        public RescheduleVaccineUseCase.RescheduleVaccineCommand build() {
            return new RescheduleVaccineUseCase.RescheduleVaccineCommand(vaccineId, nextDueDate);
        }

    }

}
