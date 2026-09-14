package com.petmanagement.owners.support;

import com.petmanagement.owners.application.port.in.CreateOwnerUseCase;
import com.petmanagement.owners.application.port.in.UpdateOwnerUseCase;
import com.petmanagement.owners.domain.model.aggregate.Owner;
import com.petmanagement.owners.domain.model.valueobject.Email;
import com.petmanagement.owners.domain.model.valueobject.OwnerId;
import com.petmanagement.owners.domain.model.valueobject.OwnerName;
import com.petmanagement.owners.domain.model.valueobject.PhoneNumber;

public final class OwnerTestBuilder {

    private OwnerName name;
    private Email email;
    private PhoneNumber phoneNumber;

    private OwnerTestBuilder() {
        this.name = TestOwnerMother.OWNER_NAME_JOHN;
        this.email = TestOwnerMother.EMAIL_JOHN;
        this.phoneNumber = TestOwnerMother.PHONE_JOHN;
    }

    public static OwnerTestBuilder aOwner() {
        return new OwnerTestBuilder();
    }

    public OwnerTestBuilder withName(OwnerName name) {
        this.name = name;
        return this;
    }

    public OwnerTestBuilder withName(String name) {
        this.name = new OwnerName(name);
        return this;
    }

    public OwnerTestBuilder withEmail(Email email) {
        this.email = email;
        return this;
    }

    public OwnerTestBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    public OwnerTestBuilder withPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public OwnerTestBuilder withPhoneNumber(String phoneNumber) {
        this.phoneNumber = new PhoneNumber(phoneNumber);
        return this;
    }

    public Owner build() {
        return Owner.create(name, email, phoneNumber);
    }

    public static class CreateOwnerCommandBuilder {

        private OwnerName name;
        private Email email;
        private PhoneNumber phoneNumber;

        private CreateOwnerCommandBuilder() {
            this.name = TestOwnerMother.OWNER_NAME_JANE;
            this.email = TestOwnerMother.EMAIL_JANE;
            this.phoneNumber = TestOwnerMother.PHONE_JANE;
        }

        public static CreateOwnerCommandBuilder aCreateOwnerCommand() {
            return new CreateOwnerCommandBuilder();
        }

        public CreateOwnerCommandBuilder withName(OwnerName name) {
            this.name = name;
            return this;
        }

        public CreateOwnerCommandBuilder withName(String name) {
            this.name = new OwnerName(name);
            return this;
        }

        public CreateOwnerCommandBuilder withEmail(Email email) {
            this.email = email;
            return this;
        }

        public CreateOwnerCommandBuilder withEmail(String email) {
            this.email = new Email(email);
            return this;
        }

        public CreateOwnerCommandBuilder withPhoneNumber(PhoneNumber phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public CreateOwnerCommandBuilder withPhoneNumber(String phoneNumber) {
            this.phoneNumber = new PhoneNumber(phoneNumber);
            return this;
        }

        public CreateOwnerUseCase.CreateOwnerCommand build() {
            return new CreateOwnerUseCase.CreateOwnerCommand(name, email, phoneNumber);
        }

    }

    public static class UpdateOwnerCommandBuilder {

        private OwnerId ownerId;
        private OwnerName name;
        private Email email;
        private PhoneNumber phoneNumber;

        private UpdateOwnerCommandBuilder() {
            this.ownerId = TestOwnerMother.DEFAULT_OWNER_ID;
            this.name = TestOwnerMother.OWNER_NAME_ROBERT;
            this.email = TestOwnerMother.EMAIL_ROBERT;
            this.phoneNumber = TestOwnerMother.PHONE_ROBERT;
        }

        public static UpdateOwnerCommandBuilder aUpdateOwnerCommand() {
            return new UpdateOwnerCommandBuilder();
        }

        public UpdateOwnerCommandBuilder withId(OwnerId ownerId) {
            this.ownerId = ownerId;
            return this;
        }

        public UpdateOwnerCommandBuilder withId(String ownerId) {
            this.ownerId = OwnerId.of(ownerId);
            return this;
        }

        public UpdateOwnerCommandBuilder withName(OwnerName name) {
            this.name = name;
            return this;
        }

        public UpdateOwnerCommandBuilder withName(String name) {
            this.name = new OwnerName(name);
            return this;
        }

        public UpdateOwnerCommandBuilder withEmail(Email email) {
            this.email = email;
            return this;
        }

        public UpdateOwnerCommandBuilder withEmail(String email) {
            this.email = new Email(email);
            return this;
        }

        public UpdateOwnerCommandBuilder withPhoneNumber(PhoneNumber phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public UpdateOwnerCommandBuilder withPhoneNumber(String phoneNumber) {
            this.phoneNumber = new PhoneNumber(phoneNumber);
            return this;
        }

        public UpdateOwnerUseCase.UpdateOwnerCommand build() {
            return new UpdateOwnerUseCase.UpdateOwnerCommand(ownerId, name, email, phoneNumber);
        }

    }

}
