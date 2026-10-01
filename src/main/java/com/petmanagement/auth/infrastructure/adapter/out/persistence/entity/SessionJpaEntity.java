package com.petmanagement.auth.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "sessions", schema = "auth_schema")
public class SessionJpaEntity {

    @Id
    private UUID id;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @Column(name = "hashed_refresh_token", nullable = false, length = 64)
    private String hashedRefreshToken;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "revoked_at", nullable = true)
    private Instant revokedAt;

    @Column(name = "revocation_reason", length = 30)
    private String revocationReason;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // === Constructors ===

    protected SessionJpaEntity() {
        // required by JPA
    }

    public SessionJpaEntity(
            UUID id,
            UUID ownerId,
            String hashedRefreshToken,
            Instant expiresAt,
            Instant revokedAt,
            String revocationReason,
            Instant createdAt
    ) {
        this.id = id;
        this.ownerId = ownerId;
        this.hashedRefreshToken = hashedRefreshToken;
        this.expiresAt = expiresAt;
        this.revokedAt = revokedAt;
        this.revocationReason = revocationReason;
        this.createdAt = createdAt;
    }

    // === Getters & Setters ===

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public String getHashedRefreshToken() {
        return hashedRefreshToken;
    }

    public void setHashedRefreshToken(String hashedRefreshToken) {
        this.hashedRefreshToken = hashedRefreshToken;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Instant getRevokedAt() {
        return revokedAt;
    }

    public void setRevokedAt(Instant revokedAt) {
        this.revokedAt = revokedAt;
    }

    public String getRevocationReason() {
        return revocationReason;
    }

    public void setRevocationReason(String revocationReason) {
        this.revocationReason = revocationReason;
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
        return "SessionJpaEntity{" +
                "id=" + id +
                ", ownerId=" + ownerId +
                ", expiresAt=" + expiresAt +
                ", revokedAt=" + revokedAt +
                ", revocationReason='" + revocationReason + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

}
