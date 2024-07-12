(ns dnd.home-spec
  (:require-macros [c3kit.wire.spec-helperc :refer [should-not-select should-select]]
                   [speclj.core :refer [before context describe it redefs-around should-contain should-end-with should-not-contain should=]])
  (:require [c3kit.wire.spec-helper :as wire]
            [dnd.creaturec :as creaturec]
            [dnd.home]
            [dnd.page :as page]
            [dnd.spec-helper :as spec-helper]
            [speclj.core]))

(def bear
  {:id           1
   :name         "Bear"
   :ac           12
   :challenge    0
   :hp           "the hp"
   :ground-speed 10
   :strength     13
   :constitution 15
   :dexterity    8})

(defn install-page! [active-class active-path]
  (page/install! :home :active-class active-class :active-path active-path)
  (wire/flush))

(describe "Home"
  (spec-helper/with-page :home)

  (context "navigation"

    (it "no class defined"
      (install-page! nil "/spells")
      (should-end-with "/druid/spells" (wire/href "#-druid"))
      (should-not-select "#-shapes-link")
      (should-not-select "#-spells-link"))

    (it "viewing druid"
      (install-page! "druid" nil)
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
      (install-page! "druid" "/druid/spells")
      (should-not-select "#-shapes-link.active")
      (should-select "#-spells-link.active")
      (should-select "#-shapes-link.outline")
      (should-not-select "#-spells-link.outline")
      (should-end-with "/" (wire/href "#-druid"))
      (should-end-with "/druid/shapes" (wire/href "#-shapes-link"))
      (should-not-contain "/druid/spells" (wire/href "#-spells-link")))

    (it "druid shapes"
      (install-page! "druid" "/druid/shapes")
      (should-select "#-shapes-link.active")
      (should-not-select "#-spells-link.active")
      (should-not-select "#-shapes-link.outline")
      (should-select "#-spells-link.outline")
      (should-end-with "/" (wire/href "#-druid"))
      (should-not-contain "/druid/shapes" (wire/href "#-shapes-link"))
      (should-end-with "/druid/spells" (wire/href "#-spells-link")))
    )

  (context "druid shapes"

    (it "no creatures"
      (with-redefs [creaturec/all nil]
        (install-page! "druid" "/druid/shapes")
        (should-not-select ".-creature")))

    (it "one creatures"
      (with-redefs [creaturec/all [bear]]
        (install-page! "druid" "/druid/shapes")
        (should= 1 (wire/count-all ".-creature"))
        (should= "Bear" (wire/text ".-creature .-name"))
        (should= "AC" (wire/text ".-creature .-ac-title"))
        (should= "HP" (wire/text ".-creature .-hp-title"))
        (should= "Speed" (wire/text ".-creature .-ground-speed-title"))
        (should= "Strength" (wire/text ".-creature .-strength-title"))
        (should= "Constitution" (wire/text ".-creature .-constitution-title"))
        (should= "Dexterity" (wire/text ".-creature .-dexterity-title"))
        (should= "12" (wire/text ".-creature .-ac"))
        (should= "the hp" (wire/text ".-creature .-hp"))
        (should= "10" (wire/text ".-creature .-ground-speed"))
        (should= "13" (wire/text ".-creature .-strength"))
        (should= "15" (wire/text ".-creature .-constitution"))
        (should= "8" (wire/text ".-creature .-dexterity"))))

    (for [[challenge fraction] [[0 "0"]
                                [0.25 "1/4"]
                                [0.5 "1/2"]
                                [1 "1"]]]
      (it (str "challenge of " fraction)
        (with-redefs [creaturec/all [(assoc bear :challenge challenge)]]
          (install-page! "druid" "/druid/shapes")
          (should= fraction (wire/text ".-creature .-challenge")))))

    (it "toggles accordion"
      (with-redefs [creaturec/all [bear]]
        (install-page! "druid" "/druid/shapes")
        (should-select ".-toggle.collapsed")
        (should-not-select ".-body.show")
        (wire/click! ".-creature .-toggle")
        (should-not-select ".-toggle.collapsed")
        (should-select ".-body.show")))
    )
  )
