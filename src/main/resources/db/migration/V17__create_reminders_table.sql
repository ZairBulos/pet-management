CREATE TABLE IF NOT EXISTS reminders_schema.reminders
(
    id          UUID         NOT NULL,
    source_type VARCHAR(20)  NOT NULL,
    source_id   UUID         NOT NULL,
    pet_id      UUID         NOT NULL,
    title       VARCHAR(100) NOT NULL,
    due_date    DATE         NOT NULL,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT now(),

    PRIMARY KEY (id),
    CONSTRAINT uq_reminders_source
        UNIQUE (source_id, source_type)
);

CREATE INDEX IF NOT EXISTS idx_reminders_pet_id
    ON reminders_schema.reminders (pet_id);