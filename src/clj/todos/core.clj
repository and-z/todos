(ns todos.core
  (:require [com.stuartsierra.component :as component]
            [compojure.core :as c]
            [compojure.route :as route]
            [ring.middleware.defaults :as rd]
            [ring.adapter.jetty :as ring])
  (:gen-class))

(defn app-routes [ctx]
  (c/routes
   (c/GET "/" _ "index page")
   (c/GET "/app" _ (str ctx " : route matched"))
   (route/not-found "Nothing here")))

(defn create-app-handler [ctx]
  (c/wrap-routes (app-routes ctx) #(rd/wrap-defaults % rd/site-defaults)))

(defrecord HttpServer [port]
  component/Lifecycle
  (start [this]
    (if (:jetty this)
      this
      (assoc this :jetty (ring/run-jetty (create-app-handler {}) {:port port :join? false}))))
  (stop [this]
    (if (:jetty this)
      (do
        (.stop (:jetty this))
        (assoc this :jetty nil)))
    this))

(defn new-system [conf]
  (component/system-map
   :http-server (map->HttpServer conf)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
