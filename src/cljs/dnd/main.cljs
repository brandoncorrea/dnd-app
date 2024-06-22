(ns dnd.main
  (:require [accountant.core :as accountant]
            [c3kit.apron.utilc :as utilc]
            [c3kit.wire.ajax :as ajax]
            [c3kit.wire.api :as api]
            [c3kit.wire.js :as wjs]
            [dnd.config :as config]
            [dnd.home]
            [dnd.page :as page]
            [dnd.router :as router]
            [reagent.dom :as dom]))

(goog/exportSymbol "goog.require" goog/require)

(defn- configure-api! [{:keys [api-version anti-forgery-token ws-csrf-token]}]
  (api/configure! {:version       api-version
                   :ajax-prep-fn  (ajax/prep-csrf "X-CSRF-Token" anti-forgery-token)
                   :ws-csrf-token ws-csrf-token}))

(defn ^:export main [payload]
  (let [{:keys [config]} (utilc/<-transit payload)]
    (configure-api! config)
    (config/install! config)
    (router/defroutes)
    (accountant/dispatch-current!)
    (dom/render [page/default] (wjs/element-by-id "app-root"))))

(enable-console-print!)
