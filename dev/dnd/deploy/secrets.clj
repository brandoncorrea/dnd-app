(ns dnd.deploy.secrets
  (:require [dnd.deploy.core :refer :all]))

(defn -main []
  (println "Installing Secrets")
  (doseq [k #{"ME_ENV" "HOSTNAME"}]
    (sh (str "echo \"" k "=${{ secret." k " }}\" >> .env"))))
