(ns todos.webserver
  (:require [com.stuartsierra.component :as component]
            [org.httpkit.server :as server]))

(defrecord WebServer [port ring-handler]
  component/Lifecycle
  (start [this]
    (println "starting webserver")
    (let [h (:handler ring-handler)]
      (assoc this :server (server/run-server h {:port port}))))
  (stop [this]
    (when-let [server (:server this)]
      (println "stopping webserver")
      (server))
    (assoc this :server nil)))

(defn new-server [opts]
  (map->WebServer opts))

