(ns dnd.config
  (:require [c3kit.apron.app :as app]
            [c3kit.apron.env :as env]))

(def environment (app/find-env "me.env" "ME_ENV"))

(def base
  {:domain     (env/env "DOMAIN")
   :tls?       true
   :log-level  :trace
   :jwt-secret (env/env "JWT_SECRET")})

(def development
  {:domain     "http://localhost:8282"
   :tls?       false
   :log-level  :trace
   :jwt-secret "secret"})

(def stage base)
(def production base)

(def development? (= "development" environment))

(defn select-env [environment]
  (case environment
    "production" production
    "stage" stage
    development))

(def env (select-env environment))
(def host (str (if (:tls? env) "https://" "http://") (:domain env)))
