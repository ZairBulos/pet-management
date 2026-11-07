CREATE TABLE IF NOT EXISTS reminders_schema.reminder_notifications
(
    id              UUID        NOT NULL,
    reminder_id     UUID        NOT NULL,
    days_before     SMALLINT    NOT NULL,
    scheduled_for   DATE        NOT NULL,
    status          VARCHAR(20) NOT NULL,
    sent_at         TIMESTAMPTZ,
    last_attempt_at TIMESTAMPTZ,
    attempts        INT         NOT NULL DEFAULT 0,
    last_error      TEXT,

    PRIMARY KEY (id),
    CONSTRAINT fk_reminder_notifications_reminder
        FOREIGN KEY (reminder_id) REFERENCES reminders_schema.reminders (id) ON DELETE CASCADE,
    CONSTRAINT uq_reminder_notifications_reminder_days
        UNIQUE (reminder_id, days_before)
);

CREATE INDEX IF NOT EXISTS idx_reminder_notifications_pending_scheduled_for
    ON reminders_schema.reminder_notifications (scheduled_for)
    WHERE status = 'PENDING';