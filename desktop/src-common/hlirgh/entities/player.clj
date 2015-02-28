(ns hlirgh.entities.player
  (:require
    [play-clj.g2d :refer :all] 
    [hlirgh.entities.utils :refer [cell-from-atlas]]))

(def player
  { :hp 100
    :name "Hero"
    :description "Savior of distressed damsels."
    :x 1
    :y 1
    :width 1
    :height 1
    :glyph [0 2]})

(defn create-player
  [& custom-stats]
  (merge (cell-from-atlas (texture "ascii_tileset.png") (:glyph player)) player))

