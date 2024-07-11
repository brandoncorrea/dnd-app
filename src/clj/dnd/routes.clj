(ns dnd.routes
  (:require [c3kit.apron.corec :as ccc]
            [c3kit.apron.util :as util]
            [c3kit.wire.ajax :as ajax]
            [clojure.string :as str]
            [compojure.core :as compojure :refer [defroutes routes]]
            [dnd.config :as config]))

(defn wrap-prefix [handler prefix not-found-handler]
  (fn [{:keys [path-info uri] :as request}]
    (let [path (or path-info uri)]
      (when (str/starts-with? path prefix)
        (let [request (assoc request :path-info (subs path (count prefix)))]
          (or (handler request)
              (not-found-handler request)))))))

(defn memoize-dev [f]
  (cond-> f config/development? memoize))

(def resolve-handler (memoize-dev util/resolve-var))

(defn lazy-handle
  "Reduces load burden of this ns, which is useful in development.
   Runtime errors will occur for missing handlers, but all the routes should be tested in routes_spec.
   Assumes all handlers take one parameter, request."
  [handler-sym request]
  (-> (resolve-handler handler-sym)
      (ccc/invoke request)))

(defmacro lazy-routes
  "Creates compojure route for each entry where the handler is lazily loaded.
   Why are params a hash-map instead of & args? -> Intellij nicely formats hash-maps as tables :-)"
  [table]
  `(routes
     ~@(for [[[path method] handler-sym] table]
         (let [method (when (not= :any method) method)]
           (compojure/compile-route method path 'req `((lazy-handle '~handler-sym ~'req)))))))

(def ajax-routes-handler
  (-> (lazy-routes
        {
         ["/user/csrf-token" :get] dnd.user/ajax-csrf-token
         })
      (wrap-prefix "/ajax" ajax/api-not-found-handler)
      ajax/wrap-ajax))

(def web-routes-handlers
  (lazy-routes
    {
     ["/" :get]       dnd.layouts/web-rich-client
     ["/spells" :get] dnd.layouts/web-rich-client
     ["/shapes" :get] dnd.layouts/web-rich-client
     }))

(defroutes handler
  ajax-routes-handler
  web-routes-handlers
  )
