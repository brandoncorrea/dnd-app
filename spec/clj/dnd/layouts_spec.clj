(ns dnd.layouts-spec
  (:require [c3kit.apron.utilc :as utilc]
            [c3kit.wire.api :as api]
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

  (context "web-rich-client"

    (it "non-development"
      (let [{:keys [status headers body]} (sut/web-rich-client {})
            payload {:flash  nil
                     :config {
                              :anti-forgery-token nil
                              :ws-csrf-token      nil
                              :api-version        (api/version)
                              :environment        config/environment
                              :host               config/host
                              }}]
        (should= 200 status)
        (should= {"Content-Type" "text/html; charset=UTF-8"} headers)
        (should-not-contain "/cljs/goog/base.js" body)
        (should-contain "goog.require('dnd.main');" body)
        (should-contain "src=\"/cljs/dnd.js\"" body)
        (should-not-contain "src=\"/cljs/goog/base.js\"" body)
        (should-contain "/css/dnd.css" body)
        (should-contain (str "<script type=\"text/javascript\">\n"
                             "//<![CDATA[\n"
                             "dnd.main.main(" (pr-str (utilc/->transit payload)) ");\n"
                             "//]]>\n"
                             "</script>")
                        body)))

    (it "development"
      (with-redefs [config/development? true]
        (let [response (sut/web-rich-client {})]
          (should-contain "/cljs/goog/base.js" (:body response)))))

    (it "client id"
      (let [response (sut/web-rich-client {:jwt/payload {:client-id "the-client-id"}})
            payload  {:flash  nil
                      :config {
                               :anti-forgery-token "the-client-id"
                               :ws-csrf-token      "the-client-id"
                               :api-version        (api/version)
                               :environment        config/environment
                               :host               config/host
                               }}]
        (should-contain (pr-str (utilc/->transit payload)) (:body response))))

    (it "flash messages"
      (let [response (sut/web-rich-client {:flash {:messages "the-messages"}})
            payload  {:flash  "the-messages"
                      :config {
                               :anti-forgery-token nil
                               :ws-csrf-token      nil
                               :api-version        (api/version)
                               :environment        config/environment
                               :host               config/host
                               }}]
        (should-contain (pr-str (utilc/->transit payload)) (:body response))))
    )

  )
