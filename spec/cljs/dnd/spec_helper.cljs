(ns dnd.spec-helper
  (:require-macros [speclj.core :refer [before]])
  (:require [c3kit.wire.spec-helper :as wire]
            [dnd.page :as page]))

(defn with-page [page]
  (list
    (wire/with-root-dom)
    (before (page/clear!)
            (wire/render [page/default])
            (page/install! page)
            (wire/flush))))
