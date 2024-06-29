(ns dnd.install
  (:require [c3kit.apron.env :as env]
            [clojure.java.shell :as shell]
            [clojure.string :as str]))

(defn sh [& args]
  (println (:out (apply shell/sh args))))

(defn- service-str [env]
  (let [alias (when (= "stage" env) ":test")]
    (-> (slurp "./bin/dnd.service")
        (str/replace "<:env>" env)
        (str/replace "<:alias>" alias))))

(defn sudo-spit [path content]
  (let [command (str "sh -c 'echo \"" content "\" > " path "'")]
    (sh "sudo" "-S" "sh" "-c" command)))

(defn -main []
  (println "Installing Service")
  (let [env (env/env! "ME_ENV")]
    (sudo-spit "/etc/systemd/system/dnd.service" (service-str env))
    (sh "sudo systemctl daemon-reload")
    (sh "sudo systemctl start dnd")
    (System/exit 0)))
