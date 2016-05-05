CREATE TABLE metrics(
    id BIGSERIAL PRIMARY KEY,
    timestamp TIMESTAMP NOT NULL,
    name TEXT NOT NULL,
    value DOUBLE PRECISION NOT NULL
);
--;;
CREATE INDEX metrics_timestamp_idx ON metrics(timestamp);
--;;
CREATE INDEX metrics_name_idx ON metrics(name);