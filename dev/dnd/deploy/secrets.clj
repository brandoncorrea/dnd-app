(ns dnd.deploy.secrets
  (:require [dnd.deploy.core :refer :all]))

(defn -main []
  (doseq [k #{"ME_ENV" "HOSTNAME"}]
    (sh (str "echo \"" k "=${{ secret." k " }}\" >> .env"))))
