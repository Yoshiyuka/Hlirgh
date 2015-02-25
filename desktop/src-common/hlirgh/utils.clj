(ns hlirgh.utils)

(def ^:const pixels-per-tile 32)

(defn abs [n] (max n (- n)))

(defn absolute-distance
  "Return the absolute distance between two points. Useful for 'range to target'."
  [[ax ay] [bx by]]
  (max (abs (- ax bx))
       (abs (- ay by))))

(def directions
  { 19 [0 1]
    20 [0 -1]
    21 [-1 0]
    22 [1 0]})

