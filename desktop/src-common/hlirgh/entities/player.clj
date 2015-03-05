(ns hlirgh.entities.player
  (:require
    [play-clj.g2d :refer :all] 
    [play-clj.core :refer :all]
    [hlirgh.world.core :refer [tile-walkable? cell-occupied?]]
    [hlirgh.entities.core :refer [create-entity]]))

(def hero
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
  (create-entity hero (first custom-stats)))

(def player (atom {}))

(defn move-player
  "Move an entity a given number of tiles (1 by default) towards a direction."
  ([direction layer entities]
  (move-player direction layer entities 1))
  ;(clojure.pprint/pprint @player)
  ;@player)
  ([direction layer entities steps]
   (let [{:keys [x y]} @player
         [dx dy] (map (partial * steps) direction)
         tile (.getTile (tiled-map-cell layer (+ x dx) (+ y dy)))]
     (when (and (tile-walkable? tile) (not (cell-occupied? [(+ x dx) (+ y dy)] entities)))
          (swap! player assoc :x (+ x dx) :y (+ y dy)))
       @player)))

