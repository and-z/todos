(def project 'todos)
(def version "0.1.0-SNAPSHOT")

(set-env! :resource-paths #{"resources"}
          :source-paths   #{"test" "src/clj"}
          :dependencies   '[[org.clojure/clojure "1.9.0-alpha15"]
                            [com.stuartsierra/component "0.3.2"]
                            [compojure "1.6.0"]
                            [ring/ring-jetty-adapter "1.6.1"]
                            [ring/ring-defaults "0.3.0"]
                            [reloaded.repl "0.2.3" :scope "test"]
                            [adzerk/boot-test "RELEASE" :scope "test"]])

(task-options!
 aot {:namespace   #{'todos.core}}
 pom {:project     project
      :version     version
      :description "FIXME: write description"
      :url         "http://example/FIXME"
      :scm         {:url "https://github.com/yourname/todos"}
      :license     {"Eclipse Public License"
                    "http://www.eclipse.org/legal/epl-v10.html"}}
 jar {:main        'todos.core
      :file        (str "todos-" version "-standalone.jar")})

(deftask build
  "Build the project locally as a JAR."
  [d dir PATH #{str} "the set of directories to write to (target)."]
  (let [dir (if (seq dir) dir #{"target"})]
    (comp (aot) (pom) (uber) (jar) (target :dir dir))))

(deftask run
  "Run the project."
  [a args ARG [str] "the arguments for the application."]
  (require '[todos.core :as app])
  (apply (resolve 'todos.core/-main) args))

(require '[adzerk.boot-test :refer [test]]
         '[reloaded.repl :refer [system start stop go reset]])

(defn init-dev []
  (require 'todos.core)
  (require '[clojure.tools.namespace.repl :as repl])
  (clojure.tools.namespace.repl/set-refresh-dirs "src/clj")
  (reloaded.repl/set-init! #((resolve 'todos.core/new-system) {:port 8080}))
  (cleanup (stop)))

(deftask dev
  "Run system in development mode."
  []
  (init-dev)
  (go)
  identity)
