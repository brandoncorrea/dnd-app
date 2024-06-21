(ns dnd.spec-helperc
  (:require [speclj.core #?(:clj :refer :cljs :refer-macros) [it should=]]
            #?(:cljs [dnd.page :as page])))

#?(:clj (defmacro it-routes
          "Tests a client side route"
          [path page-key & body]
          `(it ~path
             (secretary.core/dispatch! ~path)
             (should= ~page-key @page/current)
             ~@body)))
