(ns dnd.install
  (:require [c3kit.apron.env :as env]
            [c3kit.scaffold.cljs :as cljs]
            [c3kit.scaffold.css :as css]
            [clojure.java.shell :as shell]
            [clojure.string :as str]))

(def environment (env/env "ME_ENV"))

(defn sh [& args]
  (println (:out (apply shell/sh args))))

(def aliases {"stage" :test})

(defn- service-str [env]
  (-> (slurp "./bin/dnd.service")
      (str/replace "<:env>" env)
      (str/replace "<:alias>" (get aliases env))))

(defn- service [env]
  (spit "dnd.service" (service-str env))
  (sh "sudo mv dnd.service /etc/systemd/system/dnd.service")
  (sh "sudo systemctl daemon-reload")
  (sh "sudo systemctl start dnd"))

(defn -main []
  (let [env (env/env "ME_ENV")]
    (cljs/-main "once")
    (css/-main "once")
    (service env)))
