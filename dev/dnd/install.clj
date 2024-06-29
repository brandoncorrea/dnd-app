(ns dnd.install
  (:require [c3kit.apron.env :as env]
            [clojure.java.shell :as shell]
            [clojure.string :as str]))

(defn sh [& args]
  (println (:out (apply shell/sh "sh" "-c" args))))

(defn- service-str [env]
  (let [alias (when (= "stage" env) ":test")]
    (-> (slurp "./bin/dnd.service")
        (str/replace "<:alias>" alias))))

(defn -main []
  (println "Installing Service")
  (let [env (env/env! "ME_ENV")]
    (spit "dnd.service" (service-str env))
    (sh "sudo mv -f dnd.service /etc/systemd/system/dnd.service")
    (sh "sudo systemctl daemon-reload")
    (sh "sudo systemctl start dnd")
    (System/exit 0)))
