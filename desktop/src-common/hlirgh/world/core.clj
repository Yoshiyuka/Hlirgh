(ns hlirgh.world.core
  (:require [play-clj.core :refer :all]))

(defn tile-properties
  "Return a map containing the key/value pairs of the tile properties."
  [tile]
  (let [properties (.getProperties tile)]
    (zipmap (map keyword (.getKeys properties)) (.getValues properties))))

(def walkable-tiles
  "A set of keys associated with tiles normal entities can walk on."
  #{"floor"})

(def utility-tiles
  "A set of tile types used in misc. tasks such as spawning enemies."
  #{"spawn-point"})

(defn tile-walkable?
  "Test if the tile can be walked on by a normal entity."
  [tile]
  (walkable-tiles (:kind (tile-properties tile))))

(defn spawn-point?
  "Test if the tile is a spawn point."
  [tile]
  (utility-tiles (:kind (tile-properties tile))))
    