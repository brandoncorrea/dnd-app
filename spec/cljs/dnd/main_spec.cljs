(ns dnd.main-spec
  (:require-macros [c3kit.wire.spec-helperc :refer [should-select]]
                   [speclj.core :refer [before describe it redefs-around should-have-invoked should= stub with-stubs]])
  (:require [accountant.core :as accountant]
            [c3kit.apron.utilc :as utilc]
            [c3kit.wire.api :as api]
            [c3kit.wire.spec-helper :as wire]
            [dnd.config :as config]
            [dnd.main :as sut]
            [dnd.page :as page]
            [dnd.router :as router]))

(defmethod page/render :main [_]
  [:div#-main])

(describe "Main"
  (with-stubs)
  (wire/with-root-dom)
  (before (wire/render [:div#app-root])
          (reset! config/state {}))
  (redefs-around [router/defroutes             (stub :defroutes)
                  accountant/dispatch-current! (stub :accountant/dispatch-current!)])

  (it "main"
    (page/install! :main)
    (let [config {:api-version        "the version"
                  :anti-forgery-token "anti-forgery token"
                  :ws-csrf-token      "csrf token"}]
      (sut/main (utilc/->transit {:config config}))
      ;(wire/flush)
      (should= "the version" (:version @api/config))
      (should= "csrf token" (:ws-csrf-token @api/config))
      (should= config @config/state)
      (let [ajax-fn (:ajax-prep-fn @api/config)]
        (should= {:options {:headers {"X-CSRF-Token" "anti-forgery token"}}} (ajax-fn {})))
      (should-have-invoked :defroutes)
      (should-have-invoked :accountant/dispatch-current!)
      (should-select "#app-root #-main")))

  )
