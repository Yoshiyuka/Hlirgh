(ns hlirgh.core
  (:require [ns-tracker.core :refer :all]
            [play-clj.core :refer :all]
            [play-clj.ui :refer :all]
            [hlirgh.utils :refer [pixels-per-tile directions rand-dir]]
            [hlirgh.world.core :refer [cell-occupied?]]
            [hlirgh.entities.core :refer [move-entity move-entities]]
            [hlirgh.entities.player :refer [player create-player move-player]]
            [hlirgh.entities.ruffian :refer [create-ruffians]])
  (:import [com.badlogic.gdx.scenes.scene2d.ui Label]))

(declare overlay-screen)

;(def modified-namespaces
;  (ns-tracker ["src" "src-common"]))

(defscreen main-screen
  :on-show
  (fn [screen entities]
    (let [orthogonal-map (orthogonal-tiled-map "test.tmx" (/ 1 pixels-per-tile))
          camera (orthographic :translate (/ 800 (* 2 pixels-per-tile)) (/ 600 (* 2 pixels-per-tile)))
          ruffians (create-ruffians orthogonal-map 5)]
      (update! screen :renderer orthogonal-map :camera camera)
      (swap! player create-player)
      (add-watch player :player 
                     (fn [key atom old-state new-state]
                       (println (:x new-state))
                       (println (:y new-state))
                       (position! camera (:x new-state) (:y new-state))))
      [@player ruffians]))
  
  :on-render
  (fn [screen entities]
    (clear!)
    ;(doseq [ns-sym (modified-namespaces)]
    ;  (require ns-sym :reload))
    ;(position! screen (:x (first entities)) (:y (first entities)))
    (render! screen entities))
  
  :on-resize
  (fn [screen entities]
    (height! screen (/ (:height screen) pixels-per-tile)))
  
  :on-key-down
  (fn [screen entities]
      (screen! overlay-screen :destroy-dialogs)
      (if (directions (:key screen))
      ((fn []
         (let [t-entities (->>
                            (move-player (directions (:key screen)) (tiled-map-layer screen "Base") entities)
                            (assoc entities 0))]

              ;(loop [tmp-entities t-entities
              ;       index 1]
              ;  (if (< index (count tmp-entities))
              ;    (recur
              ;      (assoc tmp-entities index (move-entity (get tmp-entities index) (rand-dir) (tiled-map-layer screen "Base") tmp-entities))
              ;      (inc index))
              ;  tmp-entities))
            ;(reduce #(move-entities (tiled-map-layer screen "Base") %1 %2) [] t-entities)
           (->> t-entities
                (map (fn [entity]
                       (->> entity
                            (move-entity (rand-dir) (tiled-map-layer screen "Base") t-entities)
                            ))))
            )))
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
