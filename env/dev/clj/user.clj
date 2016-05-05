(ns user
  (:require [mount.core :as mount]
            metrics-server.core))

(defn start []
  (mount/start-without #'metrics-server.core/repl-server))

(defn stop []
  (mount/stop-except #'metrics-server.core/repl-server))

(defn restart []
  (stop)
  (start))


