(ns dnd.deploy.install
  (:require [c3kit.apron.env :as env]
            [c3kit.scaffold.cljs :as cljs]
            [c3kit.scaffold.css :as css]
            [clojure.string :as str]
            [dnd.deploy.core :refer :all]))

(defn- service-str [env]
  (let [alias (when (= "stage" env) ":test")]
    (-> (slurp "./bin/dnd.service")
        (str/replace "<:env>" env)
        (str/replace "<:alias>" alias))))

(defn- install-service! [env]
  (spit "dnd.service" (service-str env))
  (sh "sudo mv dnd.service /etc/systemd/system/dnd.service")
  (sh "sudo systemctl daemon-reload")
  (sh "sudo systemctl start dnd"))

(defn -main []
  (let [env (env/env "ME_ENV")]
    (println "Installing Service:" env)
    (cljs/-main "once")
    (css/-main "once")
    (install-service! env)
    (System/exit 0)))
