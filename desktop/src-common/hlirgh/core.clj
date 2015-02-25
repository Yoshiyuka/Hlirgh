(ns hlirgh.core
  (:require [ns-tracker.core :refer :all]
            [play-clj.core :refer :all]
            [play-clj.ui :refer :all]
            [hlirgh.utils :refer [pixels-per-tile directions]]
            [hlirgh.entities.core :refer [move-entity]]
            [hlirgh.entities.player :refer [create-player]]
            [hlirgh.entities.ruffian :refer [create-ruffian]])
  (:import [com.badlogic.gdx.scenes.scene2d.ui Dialog]))


(def modified-namespaces
  (ns-tracker ["src" "src-common"]))

(defscreen main-screen
  :on-show
  (fn [screen entities]
    (let [orthogonal-map (orthogonal-tiled-map "test.tmx" (/ 1 pixels-per-tile))
          camera (orthographic :translate (/ 800 (* 2 pixels-per-tile)) (/ 600 (* 2 pixels-per-tile)))
          player (create-player)
          ruffians (create-ruffian {:x (rand-int 24) :y (rand-int 24)})]
      (update! screen :renderer orthogonal-map :camera camera)   
      [player ruffians]))
  
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
      (vector (move-entity (first entities) (directions (:key screen)) (tiled-map-layer screen "Base"))
              (rest entities))
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
                      :debug)
          popup (dialog "I'm a dialog" (skin "uiskin.json"))]
      (actor! popup :set-position 400 300)
     popup))
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
