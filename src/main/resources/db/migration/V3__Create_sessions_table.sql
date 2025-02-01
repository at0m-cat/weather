CREATE TABLE sessions
(
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id    BIGSERIAL REFERENCES users (id) ON DELETE CASCADE,
    expires_at TIMESTAMP NOT NULL
);