(ns dnd.deploy.core
  (:require [clojure.java.shell :as shell]))

(defn sh [& args]
  (println (:out (apply shell/sh args))))
