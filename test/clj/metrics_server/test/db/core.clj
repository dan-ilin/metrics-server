(ns metrics-server.test.db.core
  (:require [metrics-server.db.core :refer [*db*] :as db]
            [luminus-migrations.core :as migrations]
            [clojure.test :refer :all]
            [clojure.java.jdbc :as jdbc]
            [metrics-server.config :refer [env]]
            [mount.core :as mount])
  (:import (java.util Date)))

(use-fixtures
  :once
  (fn [f]
    (mount/start
      #'metrics-server.config/env
      #'metrics-server.db.core/*db*)
    (migrations/migrate ["rollback"] (env :database-url))
    (migrations/migrate ["migrate"] (env :database-url))
    (f)))

(def input-metric
  {:timestamp (new Date 1462380001)
   :name      "Test"
   :value     10.0})

(def output-metric (assoc input-metric :id 1))

(deftest test-metrics
  (jdbc/with-db-transaction
    [t-conn *db*]
    (jdbc/db-set-rollback-only! t-conn)
    (is (= 1
           (db/insert-metric! t-conn input-metric)))
    (is (= output-metric
           (nth (db/get-metric-by-timestamp t-conn {:name      (:name input-metric)
                                                    :timestamp (:timestamp input-metric)}) 0)))
    (is (= {:sum 10.0}
           (db/sum-metric-by-time-range t-conn {:name (:name input-metric)
                                                :from (new Date 1462380000)
                                                :to   (new Date 1462390000)})))
    (is (= output-metric
           (nth (db/get-metrics t-conn {:limit 1 :offset 0}) 0)))
    (is (empty? (db/get-metrics t-conn {:limit 0 :offset 0})))))