CREATE TABLE IF NOT EXISTS pets_schema.pets
(
    id         UUID         NOT NULL,
    owner_id   UUID         NOT NULL,
    name       VARCHAR(100) NOT NULL,
    species    VARCHAR(60)  NOT NULL,
    breed      VARCHAR(100),
    coat       VARCHAR(100) NOT NULL,
    sex        VARCHAR(6),
    birth_date DATE         NOT NULL,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT now(),

    PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_pets_owner_id
    ON pets_schema.pets (owner_id);