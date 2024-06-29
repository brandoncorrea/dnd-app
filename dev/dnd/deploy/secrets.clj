(ns dnd.deploy.secrets
  (:require [c3kit.apron.corec :as ccc]
            [dnd.deploy.core :refer :all]))

(defn -main []
  (println "Installing Secrets")
  (doseq [k #{"ME_ENV" "HOSTNAME"}]
    (sh (ccc/->inspect (str "echo \"" k "=${{ secrets." k " }}\" >> .env")))))
