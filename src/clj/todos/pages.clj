(ns todos.pages
  (:require [hiccup.page :as h]))

(defn main-page []
  (h/html5
   {:lang "en"}
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport" :conten "width=device-width, initial-scale=1.0"}]
    [:title "Main page"]
    (h/include-css "css/bootstrap.min.css")]
   [:body
    [:main
     [:div#app
      [:p "Loading ..."]]]
    (h/include-js "/js/main.js")]))
