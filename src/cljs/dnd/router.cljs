(ns dnd.router
  (:require-macros [secretary.core :refer [defroute]])
  (:require [accountant.core :as accountant]
            [dnd.page :as page]
            [secretary.core :as secretary]))

(defn- hook-browser-navigation! []
  (accountant/configure-navigation!
    {:nav-handler  secretary/dispatch!
     :path-exists? secretary/locate-route}))

(defn defroutes []
  (defroute "/" [] (page/install! :home))
  (hook-browser-navigation!)
  )
