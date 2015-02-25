(ns hlirgh.core.desktop-launcher
  (:require [hlirgh.core :refer :all])
  (:import [com.badlogic.gdx.backends.lwjgl LwjglApplication]
           [org.lwjgl.input Keyboard])
  (:gen-class))

(defn -main
  []
  (LwjglApplication. hlirgh "hlirgh" 800 600)
  (Keyboard/enableRepeatEvents true))
