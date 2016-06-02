(ns umm-downloader.core
  (:gen-class)
  (:require
            [clj-time.core :as t]
            [clj-time.format :as f]
            [clj-time.local :as l]))

(def nordpool-url "https://umm.nordpoolspot.com/export/messages/csv?state=C&state=S&state=R&order_by=-published&published_range_start=%s&published_range_stop=%s")
(def time-period-in-days 30)
(def custom-formatter (f/formatter "yyyy-MM-dd"))

(defn format-url [from, to]
  (format nordpool-url from to))

(defn format-date [datetime]
  (f/parse custom-formatter datetime))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
