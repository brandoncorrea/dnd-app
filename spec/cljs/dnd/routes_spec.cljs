(ns dnd.routes-spec
  (:require-macros [dnd.spec-helperc :refer [it-routes]]
                   [speclj.core :refer [after before context describe]])
  (:require [dnd.config :as config]
            [dnd.page :as page]
            [dnd.router :as sut]
            [secretary.core :as secretary]
            [speclj.core]))

(describe "Router"
  (before (page/clear!)
          (secretary/reset-routes!)
          (sut/defroutes))

  (it-routes "/dnd-app" :home)

  (context "sandbox"
    (before (reset! config/environment "development")
            (sut/defroutes))
    (after (reset! config/environment nil))

    (it-routes "/dnd-app/sandbox" :sandbox)
    (it-routes "/dnd-app/sandbox/home" :sandbox/home)
    )
  )
