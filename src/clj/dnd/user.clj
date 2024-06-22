(ns dnd.user
  (:require [c3kit.wire.ajax :as ajax]
            [c3kit.wire.jwt :as jwt]))

(defn- ->csrf-payload [request]
  (let [{:keys [client-id]} (:jwt/payload request)]
    {:ws-csrf-token      client-id
     :anti-forgery-token client-id}))

(defn ajax-csrf-token [request]
  (-> (ajax/ok (->csrf-payload request))
      (jwt/copy-payload request)))
