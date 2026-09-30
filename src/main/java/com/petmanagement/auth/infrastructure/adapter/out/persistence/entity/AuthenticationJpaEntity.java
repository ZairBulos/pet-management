package com.petmanagement.auth.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "authentications", schema = "auth_schema")
public class AuthenticationJpaEntity {

    @Id
    private UUID id;

    @Column(name = "email", nullable = false, length = 254)
    private String email;

    @Column(name = "hashed_code", nullable = false, length = 255)
    private String hashedCode;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "authenticated_at", nullable = true)
    private Instant authenticatedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // === Constructors ===

    protected AuthenticationJpaEntity() {
        // required by JPA
    }

    public AuthenticationJpaEntity(
            UUID id,
            String email,
            String hashedCode,
            Instant expiresAt,
            Instant authenticatedAt,
            Instant createdAt
    ) {
        this.id = id;
        this.email = email;
        this.hashedCode = hashedCode;
        this.expiresAt = expiresAt;
        this.authenticatedAt = authenticatedAt;
        this.createdAt = createdAt;
    }

    // === Getters & Setters ===

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashedCode() {
        return hashedCode;
    }

    public void setHashedCode(String hashedCode) {
        this.hashedCode = hashedCode;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Instant getAuthenticatedAt() {
        return authenticatedAt;
    }

    public void setAuthenticatedAt(Instant authenticatedAt) {
        this.authenticatedAt = authenticatedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    // === Object Methods ===

    @Override
    public String toString() {
        return "AuthenticationJpaEntity{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", expiresAt=" + expiresAt +
                ", authenticatedAt=" + authenticatedAt +
                ", createdAt=" + createdAt +
                '}';
    }

}
