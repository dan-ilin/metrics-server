-- :name insert-metric! :! :n
-- :doc inserts a new metric record
INSERT INTO metrics
(timestamp, name, value)
VALUES (:timestamp, :name, :value)

-- :name get-metric-by-timestamp :? :*
-- :doc retrieve metric given the timestamp
SELECT * FROM metrics
WHERE name = :name AND timestamp = :timestamp

-- :name sum-metric-by-time-range :? :1
-- :doc sum metric values over time range
SELECT sum(value) AS sum FROM metrics
WHERE name = :name
AND timestamp BETWEEN :from AND :to

-- :name get-metrics :? :*
-- :doc retrieve a page of metrics ordered by timestamp
SELECT * FROM metrics
ORDER BY timestamp LIMIT :limit OFFSET :offset