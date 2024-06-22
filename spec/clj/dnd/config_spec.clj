(ns dnd.config-spec
  (:require [dnd.config :as sut]
            [speclj.core :refer :all]))

(describe "Config"

  (it "select-env"
    (should= sut/production (sut/select-env "production"))
    (should= sut/development (sut/select-env "development"))
    (should= sut/development (sut/select-env "blah"))
    (should= sut/development (sut/select-env nil)))

  )
