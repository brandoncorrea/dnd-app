(ns dnd.home
  (:require [c3kit.apron.corec :as ccc]
            [dnd.creaturec :as creaturec]
            [dnd.page :as page]
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

(defn- ->header [[k v]]
  [:td [:strong {:class (str "-" (name v) "-title")} k]])

(defn- ->value [m [_ v]]
  [:td {:class (str "-" (name v))} (m v)])

(defn- ->tr [f coll] (into [:tr] (map f coll)))

(defn- kv-row [m title-attrs]
  (let [kvs (seq title-attrs)]
    [:<>
     (->tr ->header kvs)
     (->tr #(->value m %) kvs)]))

(def rows
  [{"AC" :ac "HP" :hp "Speed" :ground-speed}
   {"Strength" :strength "Constitution" :constitution "Dexterity" :dexterity}])

(defn ->fraction [challenge]
  (case challenge
    0.25 "1/4"
    0.5 "1/2"
    challenge))

(defn- shape [_creature]
  (let [open? (reagent/atom false)]
    (fn [{:keys [name challenge] :as creature}]
      [:div.-creature.accordion-item
       [:h2.accordion-header
        [:button.-toggle.accordion-button.position-relative
         {:type     "button"
          :class    (when-not @open? "collapsed")
          :on-click #(swap! open? not)}
         [:span.-name name]
         [:span.-challenge.position-absolute.start-50 (->fraction challenge)]]]
       [:div.-body.accordion-collapse.collapse {:class (when @open? "show")}
        [:div.accordion-body
         [:table.table.table-striped.text-center.align-middle
          (into [:tbody] (map #(kv-row creature %) rows))]
         ;(when (seq senses)
         ;  (into [:p.-senses] (interpose ", " (map (fn [v] [:small v]) senses))))
         ;[:h5 "Traits"]
         ;(ccc/for-all [[idx {:keys [name description]}] (map-indexed vector traits)]
         ;  [:p {:key idx} [:strong name] " " description])
         ;[:h5 "Actions"]
         ;(ccc/for-all [[idx {:keys [name description]}] (map-indexed vector actions)]
         ;  [:p {:key idx} [:strong name] " " description])
         ]]])))

(defn- druid-shapes []
  [:div.accordion.mt-3
   (ccc/for-all [creature creaturec/all]
     ^{:key (:id creature)}
     [shape creature])])

(defmethod page/render :home []
  [:div.container
   [:div.list-group.mt-3
    [:a#-druid.btn.moss.list-group-item.list-group-item-action
     {:href (if @active-class "/" "/druid/spells")} "Druid"]]
   (when @active-class
     [:div.btn-group.d-flex.mt-3
      (nav-link "-spells-link" "cobalt" "/druid/spells" "Spells")
      (nav-link "-shapes-link" "rust" "/druid/shapes" "Shapes")])
   (when (= "/druid/shapes" @active-path) (druid-shapes))])
