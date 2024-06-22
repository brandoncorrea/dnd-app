(ns dnd.http-spec
  (:require [c3kit.apron.log :as log]
            [dnd.http :as sut]
            [dnd.layouts :as layouts]
            [dnd.spec-helperc :as spec-helperc]
            [org.httpkit.server :as server]
            [speclj.core :refer :all]))

(describe "Http"
  (with-stubs)
  (spec-helperc/capture-logs-around)

  (context "service"
    (redefs-around [server/run-server (stub :run-server {:return :server})])

    (it "configuration"
      (should= 'dnd.http/start (:start sut/service))
      (should= 'dnd.http/stop (:stop sut/service)))

    (it "start"
      (let [app (sut/start {:foo :bar})]
        (should= :bar (:foo app))
        (should= :server (:http app))
        (should-have-invoked :run-server {:with [sut/root-handler {:port 8282}]})
        (should= "Starting HTTP server: http://localhost:8282" (log/captured-logs-str))))

    (it "stop - missing server"
      (let [app {:foo :bar :http nil}]
        (should= {:foo :bar} (sut/stop app))))

    (it "stop - stops server"
      (let [app {:foo :bar :http (stub :stop-server)}]
        (should= {:foo :bar} (sut/stop app))
        (should= "Stopping HTTP server" (log/captured-logs-str))
        (should-have-invoked :stop-server {:with [:timeout 1000]}))
      )
    )

  (context "wrap-errors"

    (it "no error"
      (let [handler (sut/wrap-errors (stub :handler {:return "bar"}))]
        (should= "bar" (handler :request))
        (should-have-invoked :handler {:with [:request]})
        (should= "" (log/captured-logs-str))))

    (it "throws"
      (let [ex      (Throwable.)
            handler (sut/wrap-errors (stub :handler {:throw ex}))]
        (should= (layouts/error ex) (handler :request))
        (should= (str ex) (log/captured-logs-str))))
    )

  )
