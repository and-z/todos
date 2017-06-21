(ns todos.routes
  (:require [compojure.core :as c]
            [compojure.route :as route]
            [com.stuartsierra.component :as component]
            [todos.pages :as pages]
            [ring.middleware.resource :as rr]
            [ring.middleware.defaults :as rd]))

(defn app-routes [ctx]
  (c/routes
   (c/context "/app" _
              (c/GET "*" _ (pages/main-page))
              ;; (route/resources "/js" {:root "js"})
              (route/not-found "Nothing here"))
   (c/GET "*" _ "good night")))

(defn create-handler [ctx]
  (println "create handler with" ctx)
  (-> (app-routes ctx)
      (rr/wrap-resource "public")
      #_(c/wrap-routes #(rd/wrap-defaults % rd/site-defaults)))
  #_(c/wrap-routes (app-routes ctx) #(rd/wrap-defaults % rd/site-defaults)))

(defrecord Routes []
  component/Lifecycle
  (start [this]
    (assoc this :create-handler-fn `create-handler))
  (stop [this]
    this))

(defn new-routes [opts]
  (map->Routes opts))
