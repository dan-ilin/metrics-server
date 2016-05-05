(ns metrics-server.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [metrics-server.routes.services :refer [service-routes]]
            [compojure.route :as route]
            [metrics-server.env :refer [defaults]]
            [mount.core :as mount]
            [metrics-server.middleware :as middleware]))

(mount/defstate init-app
                :start ((or (:init defaults) identity))
                :stop  ((or (:stop defaults) identity)))

(def app-routes
  (routes
    #'service-routes
    (route/not-found
      "page not found")))


(defn app [] (middleware/wrap-base #'app-routes))
