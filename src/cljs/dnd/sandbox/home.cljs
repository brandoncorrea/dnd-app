(ns dnd.sandbox.home
  (:require [dnd.page :as page]))

(defmethod page/render :sandbox/home [_]
  [:h1 "This is a sandbox page."])
