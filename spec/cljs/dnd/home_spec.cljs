(ns dnd.home-spec
  (:require-macros [c3kit.wire.spec-helperc :refer [should-not-select should-select]]
                   [speclj.core :refer [before context describe it should-contain should-not-contain should=]])
  (:require [c3kit.wire.spec-helper :as wire]
            [dnd.home]
            [dnd.page :as page]
            [dnd.spec-helper :as spec-helper]
            [speclj.core]))

(describe "Home"
  (spec-helper/with-page :home)

  (context "navigation"

    (it "no path defined"
      (should-not-select "#-shapes-link.active")
      (should-not-select "#-spells-link.active")
      (should-contain "/shapes" (wire/href "#-shapes-link"))
      (should-contain "/spells" (wire/href "#-spells-link"))
      (should= "Shapes" (wire/text "#-shapes-link"))
      (should= "Spells" (wire/text "#-spells-link")))

    (it "/spells"
      (page/install! :home :active-path "/spells")
      (wire/flush)
      (should-not-select "#-shapes-link.active")
      (should-select "#-spells-link.active")
      (should-contain "/shapes" (wire/href "#-shapes-link"))
      (should-not-contain "/spells" (wire/href "#-spells-link")))

    (it "/shapes"
      (page/install! :home :active-path "/shapes")
      (wire/flush)
      (should-select "#-shapes-link.active")
      (should-not-select "#-spells-link.active")
      (should-not-contain "/shapes" (wire/href "#-shapes-link"))
      (should-contain "/spells" (wire/href "#-spells-link")))
    )

  )
