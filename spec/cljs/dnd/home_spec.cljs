(ns dnd.home-spec
  (:require-macros [c3kit.wire.spec-helperc :refer [should-not-select should-select]]
                   [speclj.core :refer [before context describe should-end-with it should-contain should-not-contain should=]])
  (:require [c3kit.wire.spec-helper :as wire]
            [dnd.home]
            [dnd.page :as page]
            [dnd.spec-helper :as spec-helper]
            [speclj.core]))

(describe "Home"
  (spec-helper/with-page :home)

  (context "navigation"

    (it "no class defined"
      (page/install! :home :active-path "/spells")
      (wire/flush)
      (should-end-with "/druid/spells" (wire/href "#-druid"))
      (should-not-select "#-shapes-link")
      (should-not-select "#-spells-link"))

    (it "viewing druid"
      (page/install! :home :active-class "druid")
      (wire/flush)
      (should-select "#-shapes-link")
      (should-select "#-spells-link")
      (should-not-select "#-shapes-link.active")
      (should-not-select "#-spells-link.active")
      (should-select "#-shapes-link.outline")
      (should-select "#-spells-link.outline")
      (should-end-with "/" (wire/href "#-druid"))
      (should-end-with "/druid/shapes" (wire/href "#-shapes-link"))
      (should-end-with "/druid/spells" (wire/href "#-spells-link")))

    (it "druid spells"
      (page/install! :home :active-class "druid" :active-path "/druid/spells")
      (wire/flush)
      (should-not-select "#-shapes-link.active")
      (should-select "#-spells-link.active")
      (should-select "#-shapes-link.outline")
      (should-not-select "#-spells-link.outline")
      (should-end-with "/" (wire/href "#-druid"))
      (should-end-with "/druid/shapes" (wire/href "#-shapes-link"))
      (should-not-contain "/druid/spells" (wire/href "#-spells-link")))

    (it "druid shapes"
      (page/install! :home :active-class "druid" :active-path "/druid/shapes")
      (wire/flush)
      (should-select "#-shapes-link.active")
      (should-not-select "#-spells-link.active")
      (should-not-select "#-shapes-link.outline")
      (should-select "#-spells-link.outline")
      (should-end-with "/" (wire/href "#-druid"))
      (should-not-contain "/druid/shapes" (wire/href "#-shapes-link"))
      (should-end-with "/druid/spells" (wire/href "#-spells-link")))
    )

  )
