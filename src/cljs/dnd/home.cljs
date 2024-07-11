(ns dnd.home
  (:require [dnd.page :as page]
            [reagent.core :as reagent]))

(def state (page/cursor [:home]))
(def active-path (reagent/track #(:active-path @state)))

(defn- nav-link [id href label]
  (let [active? (= @active-path href)
        href    (if active? "/" href)]
    [:a.nav-link
     {:id    id
      :href  href
      :class (when active? "active")}
     label]))

(defmethod page/render :home []
  [:div.container.mt-3
   [:nav.nav.nav-pills.nav-justified
    (nav-link "-shapes-link" "/shapes" "Shapes")
    (nav-link "-spells-link" "/spells" "Spells")]])
