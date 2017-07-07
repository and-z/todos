(ns todos.system
  (:require [com.stuartsierra.component :as component]
            [todos.routes :as routes]
            [todos.webserver :as web]))

(defn new-system [conf]
  (component/system-map
   :routes (component/using (routes/new-routes {}) [])
   :web-server (component/using (web/new-server conf) [:routes])))
