(ns dnd.routes-spec
  (:require-macros [dnd.spec-helperc :refer [it-routes]]
                   [speclj.core :refer [after before context describe should=]])
  (:require [dnd.page :as page]
            [dnd.routes :as sut]
            [secretary.core :as secretary]
            [speclj.core]))

(describe "Router"
  (before (page/clear!)
          (secretary/reset-routes!)
          (sut/defroutes))

  (it-routes "/" :home
    (should= "/" (-> @page/state :home :active-path)))

  (it-routes "/foo" :home
    (should= "/foo" (-> @page/state :home :active-path)))
  )
