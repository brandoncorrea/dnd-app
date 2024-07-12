(ns dnd.routes-spec
  (:require-macros [dnd.spec-helperc :refer [it-routes]]
                   [speclj.core :refer [after before context describe should-be-nil should=]])
  (:require [dnd.page :as page]
            [dnd.routes :as sut]
            [secretary.core :as secretary]
            [speclj.core]))

(describe "Router"
  (before (page/clear!)
          (secretary/reset-routes!)
          (sut/defroutes))

  (it-routes "/" :home
    (should-be-nil (-> @page/state :home :active-class))
    (should-be-nil (-> @page/state :home :active-path)))

  (it-routes "/druid" :home
    (should= "druid" (-> @page/state :home :active-class))
    (should-be-nil (-> @page/state :home :active-path)))

  (it-routes "/druid/spells" :home
    (should= "druid" (-> @page/state :home :active-class))
    (should= "/druid/spells" (-> @page/state :home :active-path)))
  )
