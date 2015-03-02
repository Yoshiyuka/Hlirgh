(ns hlirgh.core
  (:require [ns-tracker.core :refer :all]
            [play-clj.core :refer :all]
            [play-clj.ui :refer :all]
            [hlirgh.utils :refer [pixels-per-tile directions]]
            [hlirgh.entities.core :refer [move-entity]]
            [hlirgh.entities.player :refer [create-player]]
            [hlirgh.entities.ruffian :refer [create-ruffians]])
  (:import [com.badlogic.gdx.scenes.scene2d.ui Label]))

(declare overlay-screen)

(def modified-namespaces
  (ns-tracker ["src" "src-common"]))

(defscreen main-screen
  :on-show
  (fn [screen entities]
    (let [orthogonal-map (orthogonal-tiled-map "test.tmx" (/ 1 pixels-per-tile))
          camera (orthographic :translate (/ 800 (* 2 pixels-per-tile)) (/ 600 (* 2 pixels-per-tile)))
          player (create-player)
          ruffians (create-ruffians screen 4)]
          ;utility-layer (.get (.getLayers (.getMap orthogonal-map)) "Utility")
          ;utility-tiles (filter 
          ;                #(not= (:cell %) nil) 
          ;                (for [x (range (tiled-map-layer! utility-layer :get-width))
          ;                      y (range (tiled-map-layer! utility-layer :get-height))]
          ;                  {:cell (tiled-map-layer! utility-layer :get-cell x y) :x x :y y}))
          ;ruffian (create-ruffian {:x (:x (first utility-tiles)) :y (:y (first utility-tiles))})]

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
    (screen! overlay-screen :destroy-dialogs)
    (if (directions (:key screen))
      (vector (move-entity (first entities) (directions (:key screen)) (tiled-map-layer screen "Base"))
              (map (fn [entity] 
                     (move-entity entity (second (rand-nth (vec directions))) (tiled-map-layer screen "Base"))) 
                    (rest entities)))
      entities))
  
  :on-touch-down
  (fn [screen entities]
    (let[pos (input->screen screen (:input-x screen) (:input-y screen))
         entities-at-pos (filter (fn [entity]
                                   (and (= (:x entity) (int (:x pos))) (= (:y entity) (int (:y pos)))))
                                 entities)]
      (dorun (map #(screen! overlay-screen :display-entity-info :info {:name (:name %) :description (:description %) :x (:input-x screen) :y (:input-y screen)}) entities-at-pos)))
    entities)
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
  
  :display-entity-info
  (fn [screen entities]
    (let [ui-skin (skin "uiskin.json")
          popup (dialog (:name (:info screen)) ui-skin)
          {:keys [description x y]} (:info screen)
          content (label description ui-skin)]
      (->> (filter dialog? entities)
           (map #(dialog! % :hide))
           (dorun))

      (label! content :set-wrap true)
      (dialog! popup :text (:object content))
      
      (-> (first (table! (dialog! popup :get-content-table) :get-cells))
          (cell! :expand-y)
          (cell! :align (align :top))
          (cell! :width 150)
          (cell! :fill true true))
      
      (dialog! popup :button "Close")
      (actor! popup :set-position x (- (game :height) y))
      [(first entities) popup]))
  
  :destroy-dialogs
  (fn [screen entities]
    (->> (filter dialog? entities)
           (map #(dialog! % :hide))
           (dorun))
    entities)
  
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
