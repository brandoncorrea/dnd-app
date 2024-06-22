(ns dnd.spec-helperc
  (:require [speclj.core #?(:clj :refer :cljs :refer-macros) [it around should=]]
            [c3kit.apron.log :as log]
            #?(:cljs [dnd.page :as page])))

#?(:clj (defmacro it-routes
          "Tests a client side route"
          [path page-key & body]
          `(it ~path
             (secretary.core/dispatch! ~path)
             (should= ~page-key @page/current)
             ~@body)))

(defn capture-logs-around []
  (around [it] (log/capture-logs (it))))
