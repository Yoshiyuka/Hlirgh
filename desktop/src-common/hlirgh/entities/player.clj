(ns hlirgh.entities.player
  (:require
    [play-clj.g2d :refer :all] 
    [hlirgh.entities.core :refer [create-entity]]))

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
  (create-entity player (first custom-stats)))

