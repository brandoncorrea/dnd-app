(ns dnd.main-spec
  (:require [c3kit.apron.app :as app]
            [c3kit.apron.env :as env]
            [c3kit.apron.log :as log]
            [c3kit.wire.api :as api]
            [dnd.config :as config]
            [dnd.core :as core]
            [dnd.http :as http]
            [dnd.main :as sut]
            [dnd.spec-helperc :as spec-helperc]
            [speclj.core :refer :all]))

(describe "Main"
  (with-stubs)
  (spec-helperc/capture-logs-around)
  (redefs-around [shutdown-agents          :shutdown-agents
                  c3kit.apron.refresh/init (stub :refresh/init)
                  app/start!               (stub :app/start!)
                  app/stop!                (stub :app/stop!)
                  env/env                  (stub :env/env {:return "the-env"})
                  core/add-shutdown-hook   (stub :add-shutdown-hook)])

  (it "main - non-development"
    (with-redefs [config/development? false]
      (sut/-main))
    (should= (str "----- STARTING D&D SERVER -----\n"
                  "D&D environment: " config/environment)
             (log/captured-logs-str))
    (should= (:log-level config/env) (log/level))
    (should= 'dnd.routes/ws-handlers (:ws-handlers @api/config))
    (should= (api/version-from-js-file "public/cljs/dnd.js") (:version @api/config))
    (should-have-invoked :add-shutdown-hook {:with [sut/stop-all]})
    (should-have-invoked :add-shutdown-hook {:with [shutdown-agents]})
    (should-have-invoked :app/start! {:with [[sut/env http/service]]})
    (should-not-have-invoked :refresh/init))

  (it "main - development"
    (with-redefs [config/development? true]
      (sut/-main))
    (should-have-invoked :refresh/init {:with [[] "dnd" ['dnd.http 'dnd.main]]}))

  (it "stop all"
    (sut/stop-all)
    (should-have-invoked :app/stop! {:with [[sut/env http/service]]}))

  (it "start-env"
    (let [app (sut/start-env {:foo :bar})]
      (should= :bar (:foo app))
      (should-have-invoked :env/env {:with ["me.env" "ME_ENV"]})
      (should= "the-env" (:env app))))

  )
