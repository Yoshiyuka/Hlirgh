(ns hlirgh.core
  (:require [ns-tracker.core :refer :all]
            [play-clj.core :refer :all]
            [play-clj.ui :refer :all]
            [hlirgh.utils :refer [pixels-per-tile directions]]
            [hlirgh.entities.core :refer [move-entity]]
            [hlirgh.entities.player :refer [create-player]]))


(def modified-namespaces
  (ns-tracker ["src" "src-common"]))

(defscreen main-screen
  :on-show
  (fn [screen entities]
    (let [orthogonal-map (orthogonal-tiled-map "test.tmx" (/ 1 pixels-per-tile))
          camera (orthographic :translate (/ 800 (* 2 pixels-per-tile)) (/ 600 (* 2 pixels-per-tile)))
          player (create-player)]
      (update! screen :renderer orthogonal-map :camera camera)   
      [player]))
  
  :on-render
  (fn [screen entities]
    (clear!)
    (doseq [ns-sym (modified-namespaces)]
      (require ns-sym :reload))
    (position! screen (:x (first entities)) (:y (first entities)))
    (render! screen entities))
  
  :on-resize
  (fn [screen entities]
    (height! screen (/ (:height screen) pixels-per-tile)))
  
  :on-key-down
  (fn [screen entities]
    (if (directions (:key screen))
      (move-entity (first entities) (directions (:key screen)))
      entities))
  )

(defscreen overlay-screen
  :on-show
  (fn [screen entities]
    (update! screen :camera (orthographic) :renderer (stage))
    (let [ui-skin (skin "uiskin.json")
          body (table [:row
                 (label "I'm a Label" ui-skin)]
                  :align (align :center)
                  :set-width (width screen)
                  :set-height 30
                  :debug)]
      body))
  
  :on-render
  (fn [screen entities]
    (render! screen entities))

  :on-resize
  (fn [screen entities]
    (height! screen (:height screen)))
  
  :on-resume
  (fn [screen entities]
    (let [ui-skin (skin "uiskin.json")
          body (table [:row
                  (label "ok" ui-skin)]
                      :align (align :center)
                      :set-width (width screen)
                      :set-height 30
                      :debug)]
      body))
  )

(defgame hlirgh
  :on-create
  (fn [this]
    (set-screen! this main-screen overlay-screen)))

(defscreen blank-screen
  :on-render
  (fn [screen entities]
    (clear!)))

(set-screen-wrapper! (fn [screen screen-fn]
                       (try (screen-fn)
                         (catch Exception e
                           (.printStackTrace e)
                           (set-screen! hlirgh blank-screen)))))
