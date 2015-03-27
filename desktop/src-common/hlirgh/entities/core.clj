(ns hlirgh.entities.core
  (:require [play-clj.g2d :refer :all] 
            [hlirgh.utils :refer [absolute-distance pixels-per-tile]]
            [hlirgh.world.core :refer [tile-walkable? cell-occupied?]]
            [hlirgh.entities.utils :refer [cell-from-atlas]]
            [play-clj.core :refer :all]))

(defn move-entity
  "Move an entity a given number of tiles (1 by default) towards a direction."
  ([entity direction layer entities]
  (move-entity entity direction layer entities 1))
  ([entity direction layer entities steps]
   (let [{:keys [x y]} entity
         [dx dy] (map (partial * steps) direction)
         newX (+ x dx)
         newY (+ y dy)
         tile (.getTile (tiled-map-cell layer newX newY))]
     (if (and (tile-walkable? tile) (not (cell-occupied? [newX newY] entities)))
       (assoc entity :x newX :y newY)
       entity))))

(defn prevent-move
  "Revert entity to original position if the position moved to is occupied."
  [entity layer entities]
    (let [{:keys [x y]} entity]))

(defn create-entity
  [template & custom-stats]
  (merge (cell-from-atlas (texture "ascii_tileset.png") (:glyph template)) template (first custom-stats)))