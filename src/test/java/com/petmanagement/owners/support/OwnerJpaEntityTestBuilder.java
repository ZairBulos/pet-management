package com.petmanagement.owners.support;

import com.petmanagement.owners.infrastructure.adapter.out.persistence.entity.OwnerJpaEntity;

import java.time.Instant;
import java.util.UUID;

public final class OwnerJpaEntityTestBuilder {

    private UUID id;
    private String name;
    private String email;
    private String phoneNumber;
    private Instant createdAt;
    private Instant updatedAt;

    private OwnerJpaEntityTestBuilder() {
        this.id = TestOwnerMother.DEFAULT_OWNER_ID.value();
        this.name = TestOwnerMother.OWNER_NAME_JOHN.value();
        this.email = TestOwnerMother.EMAIL_JOHN.value();
        this.phoneNumber = TestOwnerMother.PHONE_JOHN.value();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public static OwnerJpaEntityTestBuilder anOwnerJpaEntity() {
        return new OwnerJpaEntityTestBuilder();
    }

    public OwnerJpaEntityTestBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public OwnerJpaEntityTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public OwnerJpaEntityTestBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public OwnerJpaEntityTestBuilder withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public OwnerJpaEntityTestBuilder withCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public OwnerJpaEntityTestBuilder withUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public OwnerJpaEntity build() {
        return new OwnerJpaEntity(id, name, email, phoneNumber, createdAt, updatedAt);
    }

}
