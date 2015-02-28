(ns hlirgh.core
  (:require [ns-tracker.core :refer :all]
            [play-clj.core :refer :all]
            [play-clj.ui :refer :all]
            [hlirgh.utils :refer [pixels-per-tile directions]]
            [hlirgh.entities.core :refer [move-entity]]
            [hlirgh.entities.player :refer [create-player]]
            [hlirgh.entities.ruffian :refer [create-ruffian]])
  (:import [com.badlogic.gdx.scenes.scene2d.ui Label]))


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
          {:keys [description x y]} (:info screen)]
          ;content (new Label description ui-skin)]
      (->> (filter dialog? entities)
           (map #(dialog! % :hide))
           (dorun))
      ;(label! content :set-wrap true)
      ;(table! (dialog! popup :get-content-table) :add content)
      ;(dialog! popup :text description)
      ;(label! content :set-width (dialog! popup :get-width))
     ; (label! content :set-alignment (align :left))
     ; (label! content :set-width 10)
     ; (label! content :set-wrap true)
      (dialog! popup :text description)
      
      (table! (dialog! popup :get-content-table) :debug-all)
      (cell! (first (table! (dialog! popup :get-content-table) :get-cells)) :expand-y)
      (cell! (first (table! (dialog! popup :get-content-table) :get-cells)) :align (align :top))
      (dialog! popup :set-width (.getPrefWidth (first (table! (dialog! popup :get-content-table) :get-cells))))
      (dialog! popup :button "Close")
      (actor! popup :set-position x (- (game :height) y))
      [(first entities) popup]))
      ;(vector (first entities) popup)))
  
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
  
  :on-ui-changed
  (fn [screen entities]
    (let [actor (-> (actor! (:actor screen) :get-parent)
                    (actor! :get-parent))]
    (vector (filter #(not(identical? actor (:object %))) entities))))
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
