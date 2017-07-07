(ns todos.webserver
  (:require [com.stuartsierra.component :as component]
            [org.httpkit.server :as http-kit]))

(defrecord WebServer [port routes]
  component/Lifecycle
  (start [this]
    (let [h (:create-handler-fn routes)]
      (assoc this :server (http-kit/run-server (h this) {:port port}))))
  (stop [this]
    (when-let [server (:server this)]
      (server))
    (assoc this :server nil)))

(defn new-server [opts]
  (map->WebServer opts))

