(ns dnd.main
  (:require [c3kit.apron.app :as app]
            [c3kit.apron.corec :as ccc]
            [c3kit.apron.log :as log]
            [c3kit.apron.util :as util]
            [c3kit.wire.api :as api]
            [dnd.config :as config]
            [dnd.core :as core]
            [dnd.http :as http]))

(defn start-env [app] (app/start-env app "me.env" "ME_ENV"))
(def env (app/service 'dnd.main/start-env 'c3kit.apron.app/stop-env))

(def all-services [env http/service])
(def refresh-services [])
(def exclude-symbols ['dnd.http 'dnd.main])

(defn maybe-init-dev []
  (when config/development?
    (-> (util/resolve-var 'c3kit.apron.refresh/init)
        (ccc/invoke refresh-services "dnd" exclude-symbols))))

(defn start-all [] (app/start! all-services))
(defn stop-all [] (app/stop! all-services))

(defn configure-api! []
  (api/configure! {:ws-handlers 'dnd.routes/ws-handlers
                   :version     (api/version-from-js-file "public/cljs/dnd.js")}))

(defn -main []
  (log/report "----- STARTING D&D SERVER -----")
  (log/report "D&D environment:" config/environment)
  (log/set-level! (config/env :log-level :warn))
  (configure-api!)
  (maybe-init-dev)
  (run! core/add-shutdown-hook [stop-all shutdown-agents])
  (start-all))
