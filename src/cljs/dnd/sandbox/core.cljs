(ns dnd.sandbox.core
  (:require [c3kit.apron.corec :as ccc]
            [dnd.page :as page]
            [dnd.sandbox.home]))

(defmethod page/render :sandbox [_]
  (let [pages (filter (comp #{"sandbox"} namespace) (keys (methods page/render)))]
    [:div
     [:h1 "Sandbox"]
     [:ul
      (ccc/for-all [page pages]
        (let [title (name page)]
          [:li {:key page}
           [:a {:href (str "/sandbox/" title)} title]]))]]))
