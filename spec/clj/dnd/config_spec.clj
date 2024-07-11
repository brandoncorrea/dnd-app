(ns dnd.config-spec
  (:require [dnd.config :as sut]
            [speclj.core :refer :all]))

(describe "Config"

  (it "select-env"
    (should= sut/base (sut/select-env "production"))
    (should= sut/development (sut/select-env "development"))
    (should= sut/development (sut/select-env "blah"))
    (should= sut/development (sut/select-env nil))
    (should= (sut/select-env sut/environment) sut/env))

  (it "->host"
    (should= "https://example.com" (sut/->host {:domain "example.com" :tls? true}))
    (should= "http://example.com" (sut/->host {:domain "example.com" :tls? false}))
    (should= "http://foo.com" (sut/->host {:domain "foo.com" :tls? false}))
    (should= (sut/->host sut/env) sut/host))

  )
