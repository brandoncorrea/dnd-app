(ns dnd.layouts-spec
  (:require [c3kit.wire.api :as api]
            [dnd.config :as config]
            [dnd.layouts :as sut]
            [speclj.core :refer :all]))

(describe "Layouts"

  (it "rich-client-payload"
    (let [request {:jwt/payload {:client-id "client id"}
                   :flash       {:messages :flash-messages}}
          {:keys [flash config]} (sut/build-rich-client-payload request)]
      (should= :flash-messages flash)
      (should= "client id" (:anti-forgery-token config))
      (should= "client id" (:ws-csrf-token config))
      (should= (api/version) (:api-version config))
      (should= config/environment (:environment config))
      (should= config/host (:host config))))

  )
