(ns dnd.routes
  (:require-macros [secretary.core :refer [defroute]])
  (:require [accountant.core :as accountant]
            [dnd.page :as page]
            [secretary.core :as secretary]))

(defn- hook-browser-navigation! []
  (accountant/configure-navigation!
    {:nav-handler  secretary/dispatch!
     :path-exists? secretary/locate-route}))

(defn defroutes []
  (defroute "/" [] (page/install! :home :active-path "/"))
  (defroute "/:active-path" [active-path] (page/install! :home :active-path (str "/" active-path)))
  (hook-browser-navigation!)
  )
