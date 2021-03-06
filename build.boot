(def project 'todos)
(def version "0.1.0-SNAPSHOT")

(set-env! :resource-paths #{"resources"}
          :source-paths   #{"test" "src/clj" "src/cljs"}
          :dependencies   '[[org.clojure/clojure "1.9.0-alpha15"]
                            [org.clojure/clojurescript "1.9.562" :scope "test"]
                            [org.clojure/core.async "0.3.443"]
                            [adzerk/boot-cljs "2.0.0" :scope "test"]
                            [adzerk/boot-reload "0.5.1" :scope "test"]
                            [com.stuartsierra/component "0.3.2"]
                            [hiccup "1.0.5"]
                            [reagent "0.6.0"
                             :exclusions [[cljsjs/react]
                                          [cljsjs/react-dom]
                                          #_[cljsjs/react-dom-server]]]
                            [compojure "1.6.0"]
                            [http-kit "2.2.0"]
                            [ring/ring-defaults "0.3.0"]
                            [reloaded.repl "0.2.3" :scope "test"]
                            [adzerk/boot-cljs-repl   "0.3.3" :scope "test"]
                            [com.cemerick/piggieback "0.2.1"  :scope "test"]
                            [weasel                  "0.7.0"  :scope "test"]
                            [org.clojure/tools.nrepl "0.2.12" :scope "test"]
                            [adzerk/boot-test "RELEASE" :scope "test"]])

(require '[adzerk.boot-test :refer [test]]
         '[adzerk.boot-cljs :refer [cljs]]
         '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
         '[adzerk.boot-reload :refer [reload]]
         '[clojure.tools.namespace.repl :as tns]
         '[reloaded.repl :as rr :refer [system reset start stop go]])

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

(deftask start-system
  [p port VAL int "webserver port."]
  (require '[todos.system])
  (let [initializer (resolve 'todos.system/new-system)]
    (apply tns/set-refresh-dirs (get-env :directories))
    (reloaded.repl/set-init! #(initializer {:port port}))
    (with-pass-thru _
      (boot.util/info "Started system. State is %s\n" (go)))))

(def cljs-files-re #".*\.cljs$")

(deftask frontend
  []
  (comp
   (watch :verbose true :include #{cljs-files-re})
   (reload)
   (cljs-repl)
   (cljs)
   identity))

(deftask backend
  [p port VAL int "webserver port."
   a auto     bool "automatically reset system on files change."]
  (comp
   (start-system :port port)
   #_(when auto
     (watch :verbose true :exclude #{cljs-files-re})
     (reset))
   identity))

(deftask dev
  "Run system in development mode."
  []
  (comp
   (backend :port 8090 :auto true)
   (frontend)
   identity))
