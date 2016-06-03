(ns umm-downloader.core
  (:gen-class)
  (:require
            [clj-time.core :as t]
            [clj-time.format :as f]
            [clj-time.local :as l]
            [clj-http.client :as http]))

(def nordpool-url "https://umm.nordpoolspot.com/export/messages/csv?state=C&state=S&state=R&order_by=-published&published_range_start=%s&published_range_stop=%s")
(def time-period-in-days (t/days 30))
(def custom-formatter (f/formatter "yyyy-MM-dd"))

(defn format-url [from, to]
  (format nordpool-url from to))

(defn format-date [datetime]
  (f/unparse custom-formatter datetime))

(defn add-day [datetime]
  (t/plus datetime (t/days 1)))

(defn generate-periods [start-datetime]
  (let [prev (t/minus start-datetime time-period-in-days)
        periods (lazy-seq (generate-periods prev))]
    (cons (add-day start-datetime) periods)))

(defn generate-periods-until [start, epoch]
  (let [ periods (take-while #(t/after? % epoch) (generate-periods start))]
    (conj (vec periods) epoch)))

(defn generate-url-from-pair [pair]
  (let [formated-pair (map format-date pair)]
    (format-url (first formated-pair) (second formated-pair))))

(defn generate-urls [periods]
    (let [ascending (sort periods)
          partitioned (partition 2 1 ascending)]
       (map generate-url-from-pair partitioned)))

(defn read-data [urls]
  (reduce http/get urls))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
