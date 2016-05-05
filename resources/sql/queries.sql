-- :name insert-metric! :! :n
-- :doc inserts a new metric record
INSERT INTO metrics
(timestamp, name, value)
VALUES (:timestamp, :name, :value)

-- :name get-metric-by-timestamp :? :*
-- :doc retrieve metric given the timestamp
SELECT name, value, timestamp FROM metrics
WHERE name = :name AND timestamp = :timestamp

-- :name sum-metric-by-time-range :? :1
-- :doc sum metric values over time range
SELECT sum(value) AS sum FROM metrics
WHERE name = :name
AND timestamp BETWEEN :from AND :to

-- :name avg-metric-by-time-range :? :1
-- :doc avg metric values over time range
SELECT avg(value) AS sum FROM metrics
WHERE name = :name
AND timestamp BETWEEN :from AND :to

-- :name count-metric-by-time-range :? :1
-- :doc count metric values over time range
SELECT count(value) AS sum FROM metrics
WHERE name = :name
AND timestamp BETWEEN :from AND :to

-- :name min-metric-by-time-range :? :1
-- :doc min metric value over time range
SELECT min(value) AS sum FROM metrics
WHERE name = :name
AND timestamp BETWEEN :from AND :to

-- :name max-metric-by-time-range :? :1
-- :doc max metric value over time range
SELECT max(value) AS sum FROM metrics
WHERE name = :name
AND timestamp BETWEEN :from AND :to

-- :name get-metrics :? :*
-- :doc retrieve a page of metrics ordered by timestamp
SELECT name, value, timestamp FROM metrics
ORDER BY timestamp LIMIT :limit OFFSET :offset