(ns dnd.config
  (:require [reagent.core :as reagent]))

(def environment (reagent/atom nil))
(def development? (reagent/track #(= "development" @environment)))
(def production? (reagent/track #(= "production" @environment)))
(defn install! [env] (reset! environment env))

(def uri-prefix "/dnd-app")
(defn uri [path] (str uri-prefix path))
