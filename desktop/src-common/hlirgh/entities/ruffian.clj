(ns hlirgh.entities.ruffian
  (:require
    [play-clj.core :refer :all]
    [play-clj.g2d :refer :all] 
    [hlirgh.entities.utils :refer [cell-from-atlas]]
    [hlirgh.entities.core :refer [create-entity]]))

(def ruffian
  { :hp 100
    :name "Ruffian"
    :description "This one looks up to no good. Better keep your distance."
    :x 2
    :y 1
    :width 1
    :height 1
    :glyph [0 1]
    :npc? true})

(defn create-ruffian
  [& custom-stats]
  (create-entity ruffian (first custom-stats)))

(defn create-ruffians
  [ortho-map amount & custom-stats]
  (let [spawn-layer (.get (.getLayers (.getMap ortho-map)) "Utility")
        spawn-points (filter #(not= (:cell %) nil)
                             (for [x (range (tiled-map-layer! spawn-layer :get-width))
                                   y (range (tiled-map-layer! spawn-layer :get-height))]
                               {:cell (tiled-map-layer! spawn-layer :get-cell x y) :x x :y y}))]

    (for [number (range amount)
          :let [spawn-point (rand-nth spawn-points)]]
      (create-ruffian {:x (:x spawn-point) :y (:y spawn-point)}))))





