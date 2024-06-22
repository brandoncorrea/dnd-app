(ns dnd.http
  (:require [c3kit.apron.app :as app]
            [c3kit.apron.log :as log]
            [c3kit.apron.time :as time]
            [c3kit.apron.util :as util]
            [c3kit.wire.assets :refer [wrap-asset-fingerprint]]
            [c3kit.wire.jwt :as jwt]
            [c3kit.wire.jwt :refer [wrap-jwt]]
            [compojure.core :refer [defroutes]]
            [compojure.route :as route]
            [dnd.config :as config]
            [dnd.layouts :as layouts]
            [org.httpkit.server :as server]
            [ring.middleware.anti-forgery :refer [wrap-anti-forgery]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.middleware.cookies :refer [wrap-cookies]]
            [ring.middleware.flash :refer [wrap-flash]]
            [ring.middleware.head :refer [wrap-head]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]
            [ring.middleware.nested-params :refer [wrap-nested-params]]
            [ring.middleware.not-modified :refer [wrap-not-modified]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.resource :refer [wrap-resource]]))

(defn refreshable [handler-sym]
  (if config/development?
    (fn [request] (@(util/resolve-var handler-sym) request))
    (util/resolve-var handler-sym)))

(defroutes web-handler
  (refreshable 'dnd.routes/handler)
  (route/not-found (layouts/not-found)))

(defn app-handler []
  (if config/development?
    (let [wrap-verbose    (util/resolve-var 'c3kit.apron.verbose/wrap-verbose)
          refresh-handler (util/resolve-var 'c3kit.apron.refresh/refresh-handler)]
      (-> 'dnd.http/web-handler refresh-handler wrap-verbose))
    (util/resolve-var 'dnd.http/web-handler)))

(defn wrap-errors [handler]
  (fn [request]
    (try
      (handler request)
      (catch Throwable e
        (log/error e)
        (layouts/error e)))))

(defonce root-handler
  (-> (app-handler)
      wrap-errors
      wrap-flash
      (wrap-anti-forgery {:strategy (jwt/create-strategy)})
      (wrap-jwt {:cookie-name "dnd-token" :secret (:jwt-secret config/env) :lifespan (when config/development? (time/hours 336))})
      wrap-keyword-params
      wrap-multipart-params
      wrap-nested-params
      wrap-params
      wrap-cookies
      (wrap-resource "public")
      wrap-asset-fingerprint
      wrap-content-type
      wrap-not-modified
      wrap-head
      ))

(defn start [app]
  (let [port (or (some-> "PORT" System/getenv Integer/parseInt) 8282)]
    (log/info (str "Starting HTTP server: http://localhost:" port))
    (let [server (server/run-server root-handler {:port port})]
      (assoc app :http server))))

(defn stop [app]
  (when-let [stop-server-fn (:http app)]
    (log/info "Stopping HTTP server")
    (stop-server-fn :timeout 1000))
  (dissoc app :http))

(def service (app/service 'dnd.http/start 'dnd.http/stop))
