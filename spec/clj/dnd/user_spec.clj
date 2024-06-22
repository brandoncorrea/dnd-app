(ns dnd.user-spec
  (:require [c3kit.wire.ajax :as ajax]
            [speclj.core :refer :all]
            [dnd.user :as sut]))

(describe "User"

  (context "ajax-csrf-token"

    (it "returns the csrf-token"
      (let [response (sut/ajax-csrf-token {:jwt/payload {:client-id "abc123"}})]
        (should= 200 (:status response))
        (should= :ok (ajax/status response))
        (should= "abc123" (:ws-csrf-token (ajax/payload response)))
        (should= "abc123" (:anti-forgery-token (ajax/payload response)))))

    (it "puts something in the session to make sure one exists"
      (let [response (sut/ajax-csrf-token {:jwt/payload {:foo :bar}})]
        (should= :bar (-> response :jwt/payload :foo))))
    )

  )
