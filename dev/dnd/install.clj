(ns dnd.install
  (:require [c3kit.apron.env :as env]
            [clojure.java.shell :as shell]
            [clojure.string :as str]))

(defn sh [& args]
  (println (:out (apply shell/sh "sh" "-c" args))))

(defn- service-str [env]
  (let [alias (when (= "stage" env) ":test")]
    (-> (slurp "./resources/config/dnd.service")
        (str/replace "<:alias>" alias))))

(defn- install-service! [env]
  (println "Installing Service")
  (spit "dnd.service" (service-str env))
  (sh "sudo mv -f dnd.service /etc/systemd/system/dnd.service")

  (println "Reloading systemctl")
  (sh "sudo systemctl daemon-reload")

  (println "Starting App")
  (sh "sudo systemctl start dnd"))

(defn- site-str [domain]
  (-> (slurp "./resources/config/site.conf")
      (str/replace "<:domain>" domain)))

(defn- install-site! [domain]
  (println "Installing Nginx")
  (sh "sudo apt-get install -y nginx")

  (println "Installing site.conf")
  (spit "site.conf" (site-str domain))
  (sh "sudo mv -f site.conf /etc/nginx/sites-available/site.conf")
  (sh "sudo ln -s /etc/nginx/sites-available/site.conf /etc/nginx/sites-enabled/")

  (println "Reloading Nginx")
  (sh "sudo systemctl reload nginx"))

(defn -main []
  (let [env    (env/env! "ME_ENV")
        domain (env/env! "DOMAIN")]
    (install-service! env)
    (install-site! domain)
    (System/exit 0)))
