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
              (route/not-found "Nothing here"))
   (route/resources "/js" {:root "js"})
   (route/resources "/css" {:root "css"})
   (c/GET "*" _ "good night")))

(defn create-handler [ctx]
  (-> (app-routes ctx)
      (c/wrap-routes
       #(-> % (rd/wrap-defaults rd/site-defaults)))))

(defrecord Routes []
  component/Lifecycle
  (start [this]
    (assoc this :create-handler-fn create-handler))
  (stop [this]
    (assoc this :create-handler-fn nil)))

(defn new-routes [opts]
  (map->Routes opts))
