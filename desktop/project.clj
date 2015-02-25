(defproject hlirgh "0.0.1-SNAPSHOT"
  :description "FIXME: write description"
  
  :dependencies [[com.badlogicgames.gdx/gdx "LATEST"]
                 [com.badlogicgames.gdx/gdx-backend-lwjgl "LATEST"]
                 [com.badlogicgames.gdx/gdx-box2d "LATEST"]
                 [com.badlogicgames.gdx/gdx-box2d-platform "LATEST"
                  :classifier "natives-desktop"]
                 [com.badlogicgames.gdx/gdx-bullet "LATEST"]
                 [com.badlogicgames.gdx/gdx-bullet-platform "LATEST"
                  :classifier "natives-desktop"]
                 [com.badlogicgames.gdx/gdx-platform "LATEST"
                  :classifier "natives-desktop"]
                 [org.clojure/clojure "LATEST"]
                 [play-clj "LATEST"]
                 [ns-tracker "LATEST"]]
  
  :source-paths ["src" "src-common"]
  :javac-options ["-target" "1.6" "-source" "1.6" "-Xlint:-options"]
  :aot [hlirgh.core.desktop-launcher]
  :main hlirgh.core.desktop-launcher)
