(ns dnd.home
  (:require [dnd.page :as page]
            [reagent.core :as reagent]))

(def state (page/cursor [:home]))
(def active-class (reagent/track #(:active-class @state)))
(def active-path (reagent/track #(:active-path @state)))

(defn- nav-link [id class href label]
  (let [active? (= @active-path href)]
    [:a.btn
     {:id    id
      :href  (if active? "/druid" href)
      :class (str class (if active? " active" " outline"))}
     label]))

(defmethod page/render :home []
  [:div.container
   [:div.mt-3.mb-3.list-group
    [:a#-druid.btn.moss.list-group-item.list-group-item-action
     {:href (if @active-class "/" "/druid/spells")} "Druid"]]
   (when @active-class
     [:div.btn-group.d-flex
      (nav-link "-spells-link" "cobalt" "/druid/spells" "Spells")
      (nav-link "-shapes-link" "rust" "/druid/shapes" "Shapes")])])
