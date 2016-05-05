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
    (let [response ((app) (request :get "/swagger-ui"))]
      (is (= 302 (:status response)))))

  (testing "swagger ui"
    (let [response ((app) (request :get "/swagger-ui/index.html"))]
      (is (= 200 (:status response)))))

  (testing "not-found route"
    (let [response ((app) (request :get "/invalid"))]
      (is (= 404 (:status response)))))

  (testing "insert metric route"
    (let [response ((app) (content-type
                            (request :post "/api/metric"
                                     (json/generate-string input-metric))
                            "application/json"))]
      (is (= 200 (:status response)))))

  (testing "get metric route"
    (let [response ((app) (request :get "/api/metric" {:page 0}))]
      (is (= 200 (:status response))))))
