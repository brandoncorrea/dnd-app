(ns dnd.styles.core
  (:refer-clojure :exclude [rem])
  (:require [garden.def :as def]
            [garden.units :as units]))

(defmacro defstyles
  "Convenience: `(garden.def/defstyles name styles*)"
  [name & styles] `(def/defstyles ~name ~@styles))

(defn px [n] (units/px n))
(defn em [n] (units/em n))
(defn rem [n] (units/rem n))
(defn percent [n] (units/percent n))

(defn font-family [face weight]
  (str "'" face "-" weight "', Helvetica, sans-serif"))

(defn font-load [face weight]
  (list
    ["@font-face" {
                   :font-family (str "'" face "-" weight "'")}
     {:src         (str "url('/fonts/" face "-" weight ".woff2') format('woff2'), "
                        "url('/fonts/" face "-" weight ".woff') format('woff')")
      :font-weight "normal"
      :font-style  "normal"
      }]

    ["@font-face" {
                   :font-family (str "'" face "-" weight "-italic'")}
     {:src         (str "url('/fonts/" face "-" weight "-italic.woff2') format('woff2'), "
                        "url('/fonts/" face "-" weight "-italic.woff') format('woff')")
      :font-weight "normal"
      :font-style  "normal"
      }]
    )
  )

(defn load-fonts [face weights]
  (map #(font-load face %) weights))

;region Colors

(def white "#fff")
(def black "#000")

(def red "#c73032")
(def barbarian "#e7623e")
(def gold "#b59e54")
(def rogue "#ab6dac")
(def ranger "#507f62")
(def silver "#91a1b2")
(def moss "#7a853b")
(def blood "#992e2e")
(def rust "#7f513e")
(def iris "#7b469b")
(def sky "#51a5c5")
(def cobalt "#2a50a1")

(def dark-white "#f8f9fa")
(def dark-ash "#4e514d")
(def dark-blood "#922829")
(def dark-rust "#784b39")
(def dark-moss "#737f36")
(def dark-iris "#744096")
(def dark-sky "#4a9fc0")
(def dark-cobalt "#234a9c")

;endregion
