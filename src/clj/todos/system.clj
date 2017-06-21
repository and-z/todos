(ns todos.system
  (:require [com.stuartsierra.component :as component]
            [todos.handler :as handler]
            [todos.routes :as routes]
            [todos.webserver :as web]))

(defn new-system [conf]
  (component/system-map
   :routes (component/using (routes/new-routes {}) [])
   :ring-handler (component/using (handler/new-handler {}) [:routes])
   :web-server (component/using (web/new-server conf) [:ring-handler])))
