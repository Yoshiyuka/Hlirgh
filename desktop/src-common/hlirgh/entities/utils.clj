(ns hlirgh.entities.utils
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]
            [hlirgh.utils :refer [pixels-per-tile]]))

(defn cell-from-atlas
  "Return the cell at a given position after splitting the atlas (tileset) into individual tiles."
  [atlas [x y]]
  (-> (texture! atlas :split (* 0.5 pixels-per-tile) (* 0.5 pixels-per-tile))
      (aget x y)
      (texture)))