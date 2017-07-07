(ns dev)

(enable-console-print!)

(println "dev.cljs loaded")

(defn on-reload []
  (println "dev.cljs reloaded"))
