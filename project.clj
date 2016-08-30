(defproject umm-downloader "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [
                  [org.clojure/clojure "1.8.0"]
                  [proto-repl "0.3.1"]
                  [clj-time "0.12.0"]
                  [clj-http "3.2.0"]
                  [org.clojure/tools.cli "0.3.5"]
                  [com.climate/claypoole "1.1.3"]]
  :main ^:skip-aot umm-downloader.core
  :target-path "target/%s"
  :profiles {
              :uberjar {:aot :all}
              :dev {
                    :source-paths ["dev" "src" "test"]
                    :dependencies [[org.clojure/tools.namespace "0.2.11"]]}})
