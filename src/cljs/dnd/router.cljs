(ns dnd.router
  (:require-macros [secretary.core :refer [defroute]])
  (:require [accountant.core :as accountant]
            [dnd.config :as config]
            [dnd.page :as page]
            [secretary.core :as secretary]))

(defn- hook-browser-navigation! []
  (accountant/configure-navigation!
    {:nav-handler  secretary/dispatch!
     :path-exists? secretary/locate-route}))

(defn def-sandbox []
  (defroute "/dnd-app/sandbox" [] (page/install! :sandbox))
  (defroute "/dnd-app/sandbox/home" [] (page/install! :sandbox/home))
  )

(defn defroutes []
  (defroute "/dnd-app/" [] (page/install! :home))
  (when @config/development? (def-sandbox))
  (hook-browser-navigation!)
  )
