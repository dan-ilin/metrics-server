(ns metrics-server.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [metrics-server.db.core :as db]
            [clojure.java.jdbc :as jdbc]))

(s/defschema Metric
  {:name      s/Str
   :value     s/Num
   :timestamp s/Inst})

(defn get-page-params [page-num]
  (let [page-size 50]
    {:limit  page-size
     :offset (* page-size page-num)}))

(defapi service-routes
        {:swagger {:ui   "/swagger-ui"
                   :spec "/swagger.json"
                   :data {:info {:version     "1.0.0"
                                 :title       "Metrics API"
                                 :description "Services for inserting and querying metrics."}}}}
        (context "/api" []
                 :tags ["metrics"]

                 (POST "/metric" []
                       :return s/Int
                       :body [metric Metric]
                       :summary "Insert single metric into database."
                       (ok (db/insert-metric! metric)))

                 (POST "/metrics" []
                       :return s/Int
                       :body [metrics [Metric]]
                       :summary "Insert multiple metrics into database."
                       (ok (jdbc/with-db-transaction
                             [t-conn db/*db*]
                             (reduce
                               #(+ %1 %2)
                               0
                               (map (partial db/insert-metric! t-conn) metrics)))))

                 (GET "/metrics" []
                      :return [Metric]
                      :query-params [page :- s/Int]
                      :summary "Get page of metrics ordered by time."
                      (ok (db/get-metrics (get-page-params page))))

                 (GET "/metric/:name" []
                      :return [Metric]
                      :path-params [name :- s/Str]
                      :query-params [timestamp :- s/Inst]
                      :summary "Get metric by name at given time."
                      (ok (db/get-metric-by-timestamp {:name      name
                                                       :timestamp timestamp})))

                 (GET "/metric/:name/sum" []
                      :return s/Num
                      :path-params [name :- s/Str]
                      :query-params [from :- s/Inst to :- s/Inst]
                      :summary "Get aggregate sum of metric value for given time range."
                      (ok (:sum (db/sum-metric-by-time-range {:name name
                                                              :from from
                                                              :to   to}))))))