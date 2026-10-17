CREATE TABLE IF NOT EXISTS health_schema.dewormings
(
    id             UUID         NOT NULL,
    pet_id         UUID         NOT NULL,
    deworming_date DATE         NOT NULL,
    drug_name      VARCHAR(100) NOT NULL,
    drug_dose      VARCHAR(100) NOT NULL,
    next_due_date  DATE         NOT NULL,
    created_at     TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at     TIMESTAMPTZ  NOT NULL DEFAULT now(),

    PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_dewormings_pet_id
    ON health_schema.dewormings (pet_id);