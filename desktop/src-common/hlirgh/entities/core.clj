(ns hlirgh.entities.core
  (:require [hlirgh.utils :refer [absolute-distance pixels-per-tile]]
            [hlirgh.world.core :refer [tile-walkable?]]
            [play-clj.core :refer :all]))

(defn move-entity
  "Move an entity a given number of tiles (1 by default) towards a direction."
  ([entity direction layer]
  (move-entity entity direction layer 1))
  ([entity direction layer steps]
   (let [{:keys [x y]} entity
         [dx dy] (map (partial * steps) direction)
         cell (.getTile (tiled-map-cell layer (+ x dx) (+ y dy)))]
     (if (tile-walkable? cell)
       (assoc entity :x (+ x dx) :y (+ y dy))
       entity))))