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

  (it-routes "/" :home)
  )
