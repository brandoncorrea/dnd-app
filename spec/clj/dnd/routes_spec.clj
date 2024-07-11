(ns dnd.routes-spec
  (:require [c3kit.apron.corec :as ccc]
            [dnd.config :as config]
            [dnd.routes :as sut]
            [dnd.user]
            [speclj.core :refer :all]))

(defmacro check-route [path method handler]
  `(let [stub-key# ~(keyword handler)]
     (require '~(symbol (namespace handler)))
     (with-redefs [~handler (stub stub-key#)]
       (sut/handler {:uri ~path :request-method ~method})
       (should-have-invoked stub-key#))))

(defmacro test-route [path method handler & body]
  `(it ~path
     (check-route ~path ~method ~handler)
     ~@body))

(def found (constantly :found))
(def not-found (constantly :not-found))

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
    (let [response (sut/handler {:uri "/blah" :request-method :get})]
      (should-be-nil response)))

  (context "memoize-dev"
    (it "development"
      (with-redefs [config/development? true]
        (let [state      (atom 0)
              memoizable #(swap! state inc)
              memoized   (sut/memoize-dev memoizable)]
          (should= 1 (memoized))
          (should= 1 (memoized)))))

    (it "non-development"
      (with-redefs [config/development? false]
        (let [state      (atom 0)
              memoizable #(swap! state inc)
              memoized   (sut/memoize-dev memoizable)]
          (should= 1 (memoized))
          (should= 2 (memoized)))))
    )

  (context "wrap-prefix"

    (it "not-found when handler returns nothing"
      (let [handler (sut/wrap-prefix ccc/noop "/prefix" not-found)]
        (should= :not-found (handler {:uri "/prefix"}))))

    (it "handler returns a result"
      (let [handler (sut/wrap-prefix found "/prefix" not-found)]
        (should= :found (handler {:uri "/prefix"}))))

    (it "uses path-info if uri is missing"
      (let [handler (sut/wrap-prefix found "/prefix" not-found)]
        (should= :found (handler {:path-info "/prefix"}))))

    (it "updates the path-info to exclude the prefix"
      (let [handler (sut/wrap-prefix (stub :handler) "/prefix" not-found)]
        (handler {:uri "/prefix/blah"})
        (should-have-invoked :handler {:with [{:uri "/prefix/blah" :path-info "/blah"}]})))

    )

  )
