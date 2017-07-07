(ns user
  (:require [figwheel-sidecar.repl-api :as fw]))

(defn cljs-repl []
  (fw/cljs-repl "dev"))
