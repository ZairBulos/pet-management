CREATE TABLE IF NOT EXISTS health_schema.weight_records
(
    id          UUID           NOT NULL,
    pet_id      UUID           NOT NULL,
    weight_date DATE           NOT NULL,
    weight      NUMERIC(10, 2) NOT NULL,
    created_at  TIMESTAMPTZ    NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ    NOT NULL DEFAULT now(),

    PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_weight_records_pet_id
    ON health_schema.weight_records (pet_id);