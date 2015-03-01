(ns hlirgh.entities.core
  (:require [play-clj.g2d :refer :all] 
            [hlirgh.utils :refer [absolute-distance pixels-per-tile]]
            [hlirgh.world.core :refer [tile-walkable?]]
            [hlirgh.entities.utils :refer [cell-from-atlas]]
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

(defn create-entity
  [template & custom-stats]
  (merge (cell-from-atlas (texture "ascii_tileset.png") (:glyph template)) template (first custom-stats)))