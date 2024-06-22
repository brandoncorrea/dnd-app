(ns dnd.core)

(defn add-shutdown-hook [^Runnable hook]
  (.addShutdownHook (Runtime/getRuntime) (Thread. hook)))
