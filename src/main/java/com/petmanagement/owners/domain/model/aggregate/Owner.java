package com.petmanagement.owners.domain.model.aggregate;

import com.petmanagement.owners.domain.model.valueobject.Email;
import com.petmanagement.owners.domain.model.valueobject.OwnerId;
import com.petmanagement.owners.domain.model.valueobject.OwnerName;
import com.petmanagement.owners.domain.model.valueobject.PhoneNumber;
import org.jmolecules.ddd.annotation.AggregateRoot;

import java.time.Instant;
import java.util.Objects;

@AggregateRoot
public final class Owner {

    private final OwnerId id;
    private OwnerName name;
    private Email email;
    private PhoneNumber phone;
    private final Instant createdAt;
    private Instant updatedAt;

    private Owner(OwnerId id, OwnerName name, Email email, PhoneNumber phone, Instant createdAt, Instant updatedAt) {
        this.id = Objects.requireNonNull(id, "The owner's id cannot be null");
        this.name = Objects.requireNonNull(name, "The owner's name cannot be null");
        this.email = Objects.requireNonNull(email, "The email cannot be null");
        this.phone = Objects.requireNonNull(phone, "The phone number cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "The createdAt cannot be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "The updatedAt cannot be null");
    }

    // === Factory ===
    public static Owner create(
            OwnerName name,
            Email email,
            PhoneNumber phone
    ) {
        var now = Instant.now();

        return new Owner(
                OwnerId.generate(),
                name,
                email,
                phone,
                now,
                now
        );
    }

    public static Owner reconstitute(
            OwnerId id,
            OwnerName name,
            Email email,
            PhoneNumber phone,
            Instant createdAt,
            Instant updatedAt
    ) {
        return new Owner(id, name, email, phone, createdAt, updatedAt);
    }

    // === Business Operations ===
    public void rename(OwnerName newName) {
        Objects.requireNonNull(newName, "The owner's name cannot be null");

        if (this.name.equals(newName)) return;

        this.name = newName;
        touch();
    }

    public void updateEmail(Email newEmail) {
        Objects.requireNonNull(newEmail, "The email cannot be null");

        if (this.email.equals(newEmail)) return;

        this.email = newEmail;
        touch();
    }

    public void updatePhone(PhoneNumber newPhone) {
        Objects.requireNonNull(newPhone, "The phone number cannot be null");

        if (this.phone.equals(newPhone)) return;

        this.phone = newPhone;
        touch();
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }

    // === Getters ===
    public OwnerId getId() {
        return id;
    }

    public OwnerName getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }

    public PhoneNumber getPhone() {
        return phone;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    // === Object Methods ===
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Owner other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Owner{" +
                "id=" + id +
                ", name=" + name +
                ", email=" + email +
                ", phone=" + phone +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

}
