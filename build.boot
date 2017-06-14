(def project 'todos)
(def version "0.1.0-SNAPSHOT")

(set-env! :resource-paths #{"resources"}
          :source-paths   #{"test" "src/clj" "src/cljs"}
          :dependencies   '[[org.clojure/clojure "1.9.0-alpha15"]
                            [org.clojure/clojurescript "1.9.562" :scope "test"]
                            [adzerk/boot-cljs "2.0.0" :scope "test"]
                            [adzerk/boot-reload "0.5.1" :scope "test"]
                            [com.stuartsierra/component "0.3.2"]
                            [hiccup "1.0.5"]
                            [compojure "1.6.0"]
                            [http-kit "2.2.0"]
                            ;; [ring/ring-jetty-adapter "1.6.1"]
                            [ring/ring-defaults "0.3.0"]
                            [reloaded.repl "0.2.3" :scope "test"]
                            [adzerk/boot-test "RELEASE" :scope "test"]])

(require '[adzerk.boot-test :refer [test]]
         '[adzerk.boot-cljs :refer [cljs]]
         '[adzerk.boot-reload :refer [reload]]
         '[reloaded.repl :refer [system start stop go reset]])

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
      :file        (str "todos-" version "-standalone.jar")}
 reload {:on-jsload 'todos.core/init
         :asset-path "public"})

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

(deftask start-webserver
  [p port VAL int "webserver port."]
  (require '[todos.core]
           '[clojure.tools.namespace.repl])
  (let [initializer (resolve 'todos.core/new-system)
        state (atom nil)]
    (clojure.tools.namespace.repl/set-refresh-dirs "src/clj")
    (reloaded.repl/set-init! #(initializer {:port port}))
    (fn [next-handler]
     (fn [fileset]
       (if @state
         (do
           (boot.util/info "Skip starting system. It is already running.")
           (next-handler fileset))
         (do
           (boot.util/info "Starting system ...\n")
           (boot.util/info "System state is %s" (reset! state (go)))
           (next-handler fileset)))))))

(deftask dev
  "Run system in development mode."
  []
  (comp
   (watch :verbose true)
   (reload)
   (cljs)
   (start-webserver :port 8090)
   identity))
