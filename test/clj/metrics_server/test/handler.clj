(ns metrics-server.test.handler
  (:require [clojure.test :refer :all]
            [ring.mock.request :refer :all]
            [metrics-server.handler :refer :all]
            [cheshire.core :as json])
  (:import (java.util Date)))

(def input-metric
  {:name      "Test"
   :value     10.0
   :timestamp (new Date)})

(deftest test-app

  (testing "swagger redirect"
    (let [request (request :get "/swagger-ui")
          response ((app) request)]
      (is (= 302 (:status response)))))

  (testing "swagger ui"
    (let [request (request :get "/swagger-ui/index.html")
          response ((app) request)]
      (is (= 200 (:status response)))))

  (testing "not-found route"
    (let [request (request :get "/invalid")
          response ((app) request)]
      (is (= 404 (:status response)))))

  (testing "insert metric route"
    (let [request (content-type
                    (request :post
                             "/api/metrics"
                             (json/generate-string [input-metric]))
                    "application/json")
          response ((app) request)]
      (is (= 200 (:status response)))))

  (testing "get metric route"
    (let [request (request :get
                           "/api/metrics"
                           {:page 1})
          response ((app) request)]
      (is (= 200 (:status response)))))

  (testing "get metric by name and timestamp route"
    (let [request (request :get
                           (str "/api/metric/" (:name input-metric))
                           {:timestamp (json/generate-string (:timestamp input-metric))})
          response ((app) request)]
      (is (= 200 (:status response)))))

  (testing "get metric sum by name and time range route"
    (let [request (request :get
                           (str "/api/metric/" (:name input-metric) "/sum")
                           {:from (json/generate-string (:timestamp input-metric))
                            :to (json/generate-string (:timestamp input-metric))})
          response ((app) request)]
      (is (= 200 (:status response))))))
