(ns dnd.home
  (:require [dnd.page :as page]))

(defmethod page/render :home []
  [:div
   [:h1 "Hello, Static World!"]])
