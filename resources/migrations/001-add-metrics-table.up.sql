CREATE TABLE metrics(
    id INTEGER PRIMARY KEY NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    name TEXT NOT NULL,
    value DECIMAL NOT NULL
);
--;;
CREATE INDEX metrics_timestamp_idx ON metrics(timestamp);
--;;
CREATE INDEX metrics_name_idx ON metrics(name);