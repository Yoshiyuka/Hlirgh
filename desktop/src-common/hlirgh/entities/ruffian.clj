(ns hlirgh.entities.ruffian
  (:require
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
    :glyph [0 1]})

(defn create-ruffian
  [& custom-stats]
  (create-entity ruffian (first custom-stats)))

