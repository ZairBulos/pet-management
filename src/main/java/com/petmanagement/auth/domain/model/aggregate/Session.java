package com.petmanagement.auth.domain.model.aggregate;

import com.petmanagement.auth.domain.event.SessionCreated;
import com.petmanagement.auth.domain.event.SessionRevoked;
import com.petmanagement.auth.domain.exception.SessionExpiredException;
import com.petmanagement.auth.domain.exception.SessionReuseDetectedException;
import com.petmanagement.auth.domain.exception.SessionRevokedException;
import com.petmanagement.auth.domain.model.enums.SessionRevocationReason;
import com.petmanagement.auth.domain.model.valueobject.*;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.event.types.DomainEvent;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

@AggregateRoot
public final class Session {

    private final SessionId id;
    private final OwnerId ownerId;
    private final SessionHashedRefreshToken hashedRefreshToken;
    private final SessionExpiresAt expiresAt;
    private Instant revokedAt;
    private SessionRevocationReason revocationReason;
    private final Instant createdAt;

    private final List<DomainEvent> events = new ArrayList<>();

    public Session(
            SessionId id,
            OwnerId ownerId,
            SessionHashedRefreshToken hashedRefreshToken,
            SessionExpiresAt expiresAt,
            Instant revokedAt,
            SessionRevocationReason revocationReason,
            Instant createdAt
    ) {
        this.id = Objects.requireNonNull(id, "Session id cannot not be null");
        this.ownerId = Objects.requireNonNull(ownerId, "Owner id cannot not be null");
        this.hashedRefreshToken = Objects.requireNonNull(hashedRefreshToken, "HashedRefreshToken cannot not be null");
        this.expiresAt = Objects.requireNonNull(expiresAt, "ExpiresAt at cannot not be null");
        this.revokedAt = revokedAt;
        this.revocationReason = revocationReason;
        this.createdAt = Objects.requireNonNull(createdAt, "CreatedAt cannot not be null");
    }

    // === Factory ===

    public static Session create(
            OwnerId ownerId,
            SessionRefreshToken refreshToken,
            UnaryOperator<String> hasher
    ) {
        var now = Instant.now();
        var expiresAt = SessionExpiresAt.generate();
        var hashedRefreshToken = SessionHashedRefreshToken.from(refreshToken.value(), hasher);

        var session = new Session(
                SessionId.generate(),
                ownerId,
                hashedRefreshToken,
                expiresAt,
                null,
                null,
                now
        );

        session.events.add(new SessionCreated(
                session.id.value(),
                session.ownerId.value(),
                now
        ));

        return session;
    }

    public static Session reconstitute(
            SessionId id,
            OwnerId ownerId,
            SessionHashedRefreshToken hashedRefreshToken,
            SessionExpiresAt expiresAt,
            Instant revokedAt,
            SessionRevocationReason revocationReason,
            Instant createdAt
    ) {
        return new Session(id, ownerId, hashedRefreshToken, expiresAt, revokedAt, revocationReason, createdAt);
    }

    // === Business Operations ===

    public Session rotate(
            SessionRefreshToken refreshToken,
            UnaryOperator<String> hasher
    ) {
        var now = Instant.now();

        if (isRevoked())
            throw revocationReason == SessionRevocationReason.ROTATED
                    ? new SessionReuseDetectedException()
                    : new SessionRevokedException();

        if (isExpired(now))
            throw new SessionExpiredException();

        revoke(SessionRevocationReason.ROTATED);

        return create(ownerId, refreshToken, hasher);
    }

    public void revoke(SessionRevocationReason revocationReason) {
        if (isRevoked())
            return;

        this.revokedAt = Instant.now();
        this.revocationReason = revocationReason;
        events.add(new SessionRevoked(
                id.value(),
                ownerId.value(),
                revocationReason.name(),
                revokedAt
        ));
    }

    public boolean isRevoked() {
        return revokedAt != null;
    }

    public boolean isExpired(Instant now) {
        return expiresAt.isExpired(now);
    }

    public boolean isActive(Instant now) {
        return !isRevoked() && !isExpired(now);
    }

    // === Events ===

    public List<DomainEvent> pullEvents() {
        var pendingEvents = List.copyOf(events);
        events.clear();
        return pendingEvents;
    }

    // === Getters ===

    public SessionId getId() {
        return id;
    }

    public OwnerId getOwnerId() {
        return ownerId;
    }

    public SessionHashedRefreshToken getHashedRefreshToken() {
        return hashedRefreshToken;
    }

    public SessionExpiresAt getExpiresAt() {
        return expiresAt;
    }

    public Instant getRevokedAt() {
        return revokedAt;
    }

    public SessionRevocationReason getRevocationReason() {
        return revocationReason;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    // === Object Methods

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Session other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", ownerId=" + ownerId +
                ", expiresAt=" + expiresAt +
                ", revokedAt=" + revokedAt +
                ", revocationReason=" + revocationReason +
                ", createdAt=" + createdAt +
                '}';
    }

}
