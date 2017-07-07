(defproject example.todos "0.1.0-SNAPSHOT"
  :description "Playground clj and cljs project"
  :url "https://github.com/and-z/todos"
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo
            :comments "same as Clojure"}
  :source-paths ["src/clj"]
  :resource-paths ["resources/public"]
  :dependencies [[org.clojure/clojure "1.9.0-alpha15"]
                 [org.clojure/clojurescript "1.9.562"]
                 [org.clojure/core.async "0.3.443"]
                 [com.stuartsierra/component "0.3.2"]
                 [hiccup "1.0.5"]
                 [http-kit "2.2.0"]
                 [compojure "1.6.0"]
                 [ring/ring-defaults "0.3.0"]
                 [reagent "0.6.0"
                  :exclusions [[cljsjs/react]
                               [cljsjs/react-dom]
                               #_[cljsjs/react-dom-server]]]]
  :profiles {:dev {:source-paths ["dev"]
                   :repl-options {:init-ns dev}
                   :dependencies [[reloaded.repl "0.2.3"]]}})
