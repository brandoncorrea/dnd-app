(ns dnd.install
  (:require [c3kit.apron.env :as env]
            [c3kit.scaffold.cljs :as cljs]
            [c3kit.scaffold.css :as css]
            [clojure.java.shell :as shell]
            [clojure.string :as str]))

(defn sh [& args]
  (println (:out (apply shell/sh args))))

(defn- service-str [env]
  (let [alias (when (= "stage" env) ":test")]
    (-> (slurp "./bin/dnd.service")
        (str/replace "<:env>" env)
        (str/replace "<:alias>" alias))))

(defn- install-service! [env]
  (println "Installing Service")
  (spit "dnd.service" (service-str env))
  (sh "sudo mv -f dnd.service /etc/systemd/system")
  (sh "sudo systemctl daemon-reload")
  (sh "sudo systemctl start dnd"))

(defn -main []
  (let [env (env/env "ME_ENV")]
    (println "Installing Application:" env)
    (cljs/-main "once")
    (css/-main "once")
    (install-service! env)
    (System/exit 0)))
