(ns dnd.config
  (:require [reagent.core :as reagent]))

(def state (reagent/atom {}))
(defn install! [config] (reset! state config))
