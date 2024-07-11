(ns dnd.routes-spec
  (:require [dnd.routes :as routes]
            [dnd.user]
            [speclj.core :refer :all]))

(defmacro check-route [path method handler]
  `(let [stub-key# ~(keyword handler)]
     (require '~(symbol (namespace handler)))
     (with-redefs [~handler (stub stub-key#)]
       (routes/handler {:uri ~path :request-method ~method})
       (should-have-invoked stub-key#))))

(defmacro test-route [path method handler & body]
  `(it ~path
     (check-route ~path ~method ~handler)
     ~@body))

(describe "Routes"
  (with-stubs)
  (redefs-around [c3kit.wire.api/version (constantly "fake-api-version")])

  ;; web routes
  (test-route "/" :get dnd.layouts/web-rich-client)
  (test-route "/spells" :get dnd.layouts/web-rich-client)
  (test-route "/shapes" :get dnd.layouts/web-rich-client)

  ;; ajax routes
  (test-route "/ajax/user/csrf-token" :get dnd.user/ajax-csrf-token)

  (it "not-found"
    (let [response (routes/handler {:uri "/blah" :request-method :get})]
      (should-be-nil response)))

  )
