(ns dnd.styles.main
  (:refer-clojure :exclude [rem])
  (:require [dnd.styles.components :as components]
            [dnd.styles.core :refer :all]))

(defstyles screen
  components/screen
  )
