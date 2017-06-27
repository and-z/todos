(ns todos.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [clojure.core.async :refer [<! >! put! chan close!]]
            [goog.events :as events]
            [reagent.core :as r]
            [cljsjs.react]
            [cljsjs.react-bootstrap])
  (:import [goog.net XhrIo EventType]
           [goog.events EventType]))

(enable-console-print!)

;; (println "Hello world!")

;; (def echo-chan (chan))
;; (go (println (<! echo-chan)))
;; (put! echo-chan "hello")

;; (defn foo []
;;   (let [c (chan)
;;         xhr (XhrIo.)]
;;     (events/listen )
;;     (.send XhrIo
;;      "http://localhost:8090/"
;;      (fn [e]
;;        (this-as this
;;          (println this)))
;;      "GET"
;;      #js{"Content-Type" "text/plain"})))

(def button (r/adapt-react-class js/ReactBootstrap.Button))
(def panel (r/adapt-react-class js/ReactBootstrap.Panel))

(defn foo [ch]
  [panel {:header "Foo-Header"}
   (r/as-element [:div "inside foo"])
   ch])

(defn hello-component []
  [:div "hello reagent!"
   [foo
    [button {:onClick #(println "btn clicked") :bsSize "xsmall" :bsStyle "success"} "Click Me"]]])

(r/render [hello-component] (.getElementById js/document "app"))

(defn init []
  (println "Reloaded!"))
