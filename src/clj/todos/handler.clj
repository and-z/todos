(ns todos.handler
  (:require [com.stuartsierra.component :as component]))

(defrecord Handler [routes]
  component/Lifecycle
  (start [this]
    (assoc this :handler ((resolve (:create-handler-fn routes)) this)))
  (stop [this]
    (assoc this :handler nil)))

(defn new-handler [opts]
  (map->Handler opts))
