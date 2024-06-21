(ns dnd.main
  (:require [accountant.core :as accountant]
            [c3kit.wire.js :as wjs]
            [dnd.config :as config]
            [dnd.home]
            [dnd.page :as page]
            [dnd.router :as router]
            [dnd.sandbox.core]
            [reagent.dom :as dom]))

(goog/exportSymbol "goog.require" goog/require)

(defn ^:export main []
  (config/install! "production")
  ;(config/install! "development")
  (router/defroutes)
  (accountant/dispatch-current!)
  (dom/render [page/default] (wjs/element-by-id "app-root")))

(enable-console-print!)
