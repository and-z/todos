(ns todos.core
  (:require [com.stuartsierra.component :as component]
            [hiccup.page :as h]
            [compojure.core :as c]
            [compojure.route :as route]
            [org.httpkit.server :as server]
            [ring.middleware.resource :as rr]
            [ring.middleware.defaults :as rd])
  (:gen-class))

(defn main-page []
  (h/html5
   {:lang "en"}
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport" :conten "width=device-width, initial-scale=1.0"}]
    [:title "Main page"]]
   [:body
    [:main
     [:div#app
      [:p "Loading ..."]]]
    (h/include-js "js/main.js")]))

(defn app-routes [ctx]
  (c/context "/app" _
    (c/routes
     (c/GET "/" _ (main-page))
    ;; (route/resources "/js" {:root "js"})
     (route/not-found "Nothing here"))))

(defn create-app-handler [ctx]
  (-> (app-routes ctx)
      (rr/wrap-resource "public")
      #_(c/wrap-routes #(rd/wrap-defaults % rd/site-defaults)))
  #_(c/wrap-routes (app-routes ctx) #(rd/wrap-defaults % rd/site-defaults)))

(defn- run [port]
  (server/run-server (create-app-handler {}) {:port port}))

(defrecord WebServer [port]
  component/Lifecycle
  (start [this]
    (assoc this :server (run port)))
  (stop [this]
    (when-let [server (:server this)]
      (server))
    (assoc this :server nil)))

(defn new-system [conf]
  (println conf)
  (component/system-map
   :web-server (map->WebServer conf)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
