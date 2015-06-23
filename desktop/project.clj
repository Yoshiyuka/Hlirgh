(defproject hlirgh "0.0.1-SNAPSHOT"
  :description "FIXME: write description"
  
  :dependencies [[com.badlogicgames.gdx/gdx "1.6.0"]
                 [com.badlogicgames.gdx/gdx-backend-lwjgl "1.6.0"]
                 [com.badlogicgames.gdx/gdx-box2d "1.6.0"]
                 [com.badlogicgames.gdx/gdx-box2d-platform "1.6.0"
                  :classifier "natives-desktop"]
                 [com.badlogicgames.gdx/gdx-bullet "1.6.0"]
                 [com.badlogicgames.gdx/gdx-bullet-platform "1.6.0"
                  :classifier "natives-desktop"]
                 [com.badlogicgames.gdx/gdx-platform "1.6.0"
                  :classifier "natives-desktop"]
                 [org.clojure/clojure "1.7.0-alpha5"]
                 [play-clj "0.4.7-SNAPSHOT"]
                 [ns-tracker "LATEST"]]
  
  :source-paths ["src" "src-common"]
  :javac-options ["-target" "1.7" "-source" "1.7" "-Xlint:-options"]
  :aot [hlirgh.core.desktop-launcher]
  :main hlirgh.core.desktop-launcher)
