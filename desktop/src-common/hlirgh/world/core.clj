(ns hlirgh.world.core
  (:require [play-clj.core :refer :all]))


(def walkable-tiles
  "A set of keys associated with tiles normal entities can walk on."
  #{"floor"})

(defn tile-walkable?
  "Test if the tile can be walked on by a normal entity."
  [tile]
  (let [properties (.getProperties tile)
        tile-properties (zipmap (map keyword (.getKeys properties)) (.getValues properties))]
      (walkable-tiles (:kind tile-properties))))