(ns hlirgh.entities.core
  (:require [hlirgh.utils :refer [absolute-distance pixels-per-tile]]))

(defn move-entity
  "Move an entity a given number of tiles (1 by default) towards a direction."
  ([entity direction]
  (move-entity entity direction 1))
  ([entity direction steps]
   (let [{:keys [x y]} entity
         [dx dy] (map (partial * steps) direction)]
     (assoc entity :x (+ x dx) :y (+ y dy)))))