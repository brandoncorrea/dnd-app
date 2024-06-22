(ns dnd.corec
  "Macros may be defined here."
  #?(:cljs (:require-macros [dnd.corec :refer [defroute]]))
  (:require [secretary.core :as secretary]))

#?(:clj
   (defmacro defroute [route destruct & body]
     `(secretary/defroute (dnd.config/uri ~route) ~destruct ~@body)))
