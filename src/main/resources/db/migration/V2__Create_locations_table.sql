CREATE TABLE locations
(
    id        SERIAL PRIMARY KEY,
    name      VARCHAR(255)  NOT NULL,
    user_id   INT REFERENCES users (id) ON DELETE CASCADE,
    latitude  DECIMAL(9, 6) NOT NULL,
    longitude DECIMAL(9, 6) NOT NULL
);