CREATE TABLE IF NOT EXISTS auth_schema.sessions
(
    id                   UUID        NOT NULL,
    owner_id             UUID        NOT NULL,
    hashed_refresh_token VARCHAR(64) NOT NULL,
    expires_at           TIMESTAMPTZ NOT NULL,
    revoked_at           TIMESTAMPTZ          DEFAULT NULL,
    revocation_reason    VARCHAR(30),
    created_at           TIMESTAMPTZ NOT NULL DEFAULT now(),

    PRIMARY KEY (id),

    CONSTRAINT chk_sessions_revocation_reason
        CHECK (revocation_reason IN ('ROTATED', 'LOGOUT', 'REUSE_DETECTED'))
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_sessions_hashed_refresh_token
    ON auth_schema.sessions (hashed_refresh_token);

CREATE INDEX IF NOT EXISTS idx_sessions_owner_id_active
    ON auth_schema.sessions (owner_id)
    WHERE revoked_at IS NULL;