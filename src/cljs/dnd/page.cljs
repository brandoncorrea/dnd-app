(ns dnd.page
  (:require [reagent.core :as reagent]))

(def state (reagent/atom nil))
(defn clear! [] (reset! state nil))
(def current (reagent/track #(:current @state)))

(defn- update-state [state page options]
  (-> (assoc state :current page)
      (update page merge options)))

(defn install! [page & {:as options}]
  (swap! state update-state page options))

(defn cursor
  ([path] (cursor path nil))
  ([path value]
   (let [c (reagent/cursor state path)]
     (reset! c value)
     c)))

(defmulti render identity)
(defmethod render :default [_] [:h1 "404 – Not Found"])

(defn default [] [render @current])
