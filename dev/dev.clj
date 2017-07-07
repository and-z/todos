(ns dev
  (:require [clojure.pprint :refer [pprint]]
            [reloaded.repl :as rr :refer [reset system]]
            [todos.system :refer [new-system]]))

(rr/set-init! #(new-system {:port 8090}))
