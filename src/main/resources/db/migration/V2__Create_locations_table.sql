CREATE TABLE locations
(
    id        BIGSERIAL PRIMARY KEY,
    name      VARCHAR(255)  NOT NULL,
    user_id   BIGSERIAL REFERENCES users (id) ON DELETE CASCADE,
    latitude  DECIMAL(9, 6) NOT NULL,
    longitude DECIMAL(9, 6) NOT NULL
);