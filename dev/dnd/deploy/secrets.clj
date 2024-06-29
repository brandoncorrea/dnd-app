(ns dnd.deploy.secrets
  (:require [dnd.deploy.core :refer :all]))

(defn export-key! [k]
  (sh "sh" "-c" (str "echo \"" k "=${{ secrets." k " }}\" >> .env")))

(defn -main []
  (println "Installing Secrets")
  (spit ".env" "")
  (run! export-key! #{"ME_ENV" "HOSTNAME"})
  (System/exit 0))
