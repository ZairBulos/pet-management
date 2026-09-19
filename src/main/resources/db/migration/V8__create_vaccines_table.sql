CREATE TABLE IF NOT EXISTS health_schema.vaccines
(
    id               UUID         NOT NULL,
    pet_id           UUID         NOT NULL,
    vaccination_date DATE         NOT NULL,
    vaccine_name     VARCHAR(100) NOT NULL,
    next_due_date    DATE         NOT NULL,
    created_at       TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at       TIMESTAMPTZ  NOT NULL DEFAULT now(),

    PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_vaccines_pet_id
    ON health_schema.vaccines (pet_id);