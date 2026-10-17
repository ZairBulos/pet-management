CREATE TABLE IF NOT EXISTS auth_schema.authentications
(
    id               UUID         NOT NULL,
    email            VARCHAR(254) NOT NULL,
    hashed_code      VARCHAR(255) NOT NULL,
    expires_at       TIMESTAMPTZ  NOT NULL,
    authenticated_at TIMESTAMPTZ           DEFAULT NULL,
    created_at       TIMESTAMPTZ  NOT NULL DEFAULT now(),

    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_authentications_active_email
    ON auth_schema.authentications (email)
    WHERE authenticated_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_authentications_email_created_at
    ON auth_schema.authentications (email, created_at DESC);