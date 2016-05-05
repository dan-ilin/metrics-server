(ns metrics-server.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[metrics-server started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[metrics-server has shutdown successfully]=-"))
   :middleware identity})
