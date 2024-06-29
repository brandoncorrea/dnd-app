(ns dnd.deploy.secrets
  (:require [c3kit.apron.corec :as ccc]
            [dnd.deploy.core :refer :all]))

(defn -main []
  (println "Installing Secrets")
  (spit ".env" "")
  (doseq [k #{"ME_ENV" "HOSTNAME"}]
    (sh (str "echo \"" k "=${{ secrets." k " }}\" >> .env"))))
