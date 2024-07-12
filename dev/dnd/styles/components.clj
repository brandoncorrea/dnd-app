(ns dnd.styles.components
  (:require [dnd.styles.core :refer :all]))

(defn color-buttons [class-name bg-color bg-color-dark]
  (let [selector (keyword (str "." class-name))]
    [selector {
      :background-color bg-color
      :color white
      }

      [:&.btn

        [:&.outline {
          :background-color "transparent"
          :color bg-color
          :border-color bg-color
        }]

        [:&:hover :&.active {
          :background-color bg-color-dark
          :color white
          :border-color bg-color
        }]
      ]
    ]
    ))

(defstyles screen

  (color-buttons "moss" moss dark-moss)
  (color-buttons "cobalt" cobalt dark-cobalt)
  (color-buttons "rust" rust dark-rust)

  )
