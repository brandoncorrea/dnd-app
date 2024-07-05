(ns dnd.config
  (:require [c3kit.apron.app :as app]
            [c3kit.apron.env :as env]))

(def environment (app/find-env "me.env" "ME_ENV"))

(def base
  {:host       (env/env "HOST")
   :log-level  :trace
   :jwt-secret (env/env "JWT_SECRET")})

(def development
  {:host       "http://localhost:8282"
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
(def host (:host env))
