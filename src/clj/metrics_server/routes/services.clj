(ns metrics-server.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [metrics-server.db.core :as db]))

(s/defschema Metric
  {(s/optional-key :id) s/Int
   :name                s/Str
   :value               s/Num
   :timestamp           s/Inst})

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
                       :return Integer
                       :body [metric Metric]
                       :summary "Insert single metric into database."
                       (ok (db/insert-metric! metric)))

                 (GET "/metric" []
                      :return [Metric]
                      :query-params [page :- Long]
                      :summary "Get page of metrics."
                      (ok (db/get-metrics (get-page-params page))))))