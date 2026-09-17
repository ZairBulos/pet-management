CREATE TABLE IF NOT EXISTS owners_schema.owners
(
    id           UUID         NOT NULL,
    name         VARCHAR(150) NOT NULL,
    email        VARCHAR(254) NOT NULL UNIQUE,
    phone_number VARCHAR(15)  NOT NULL,
    created_at   TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at   TIMESTAMPTZ  NOT NULL DEFAULT now(),

    PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_owners_email
    ON owners_schema.owners (email);