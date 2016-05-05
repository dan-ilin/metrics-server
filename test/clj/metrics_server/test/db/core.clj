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

(def metric
  {:timestamp (new Date 1462380001)
   :name      "Test"
   :value     10.0})

(deftest test-metrics
  (jdbc/with-db-transaction
    [t-conn *db*]
    (jdbc/db-set-rollback-only! t-conn)
    (testing "insert-metric"
      (is (= 1
             (db/insert-metric! t-conn metric))))
    (testing "get-metric-by-timestamp"
      (is (= metric
             (nth (db/get-metric-by-timestamp
                    t-conn
                    {:name      (:name metric)
                     :timestamp (:timestamp metric)}) 0))))
    (testing "sum-metric-by-time-range"
      (is (= {:sum 10.0}
             (db/sum-metric-by-time-range
               t-conn
               {:name (:name metric)
                :from (new Date 1462380000)
                :to   (new Date 1462390000)}))))
    (testing "get-metrics-page"
      (is (= metric
             (nth (db/get-metrics
                    t-conn
                    {:limit  1
                     :offset 0})
                  0))))
    (testing "empty-metrics-page "
      (is (empty? (db/get-metrics
                    t-conn
                    {:limit  0
                     :offset 0}))))))