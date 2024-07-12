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
  (defroute "/" [] (page/install! :home :active-class nil :active-path nil))
  (defroute "/:active-class" [active-class] (page/install! :home :active-class active-class :active-path nil))
  (defroute "/:active-class/:active-path" [active-class active-path] (page/install! :home :active-class active-class :active-path (str "/" active-class "/" active-path)))
  (hook-browser-navigation!)
  )
