(ns dnd.router
  (:require-macros [dnd.corec :refer [defroute]])
  (:require [accountant.core :as accountant]
            [dnd.config :as config]
            [dnd.page :as page]
            [secretary.core :as secretary]))

(defn- hook-browser-navigation! []
  (accountant/configure-navigation!
    {:nav-handler  secretary/dispatch!
     :path-exists? secretary/locate-route}))

(defn def-sandbox []
  (defroute "/sandbox" [] (page/install! :sandbox))
  (defroute "/sandbox/home" [] (page/install! :sandbox/home))
  )

(defn defroutes []
  (defroute "/" [] (page/install! :home))
  (when @config/development? (def-sandbox))
  (hook-browser-navigation!)
  )
