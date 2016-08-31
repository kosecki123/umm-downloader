(ns umm-downloader.core
  (:gen-class)
  (:require
            [clojure.string :as str]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [clj-time.local :as l]
            [clj-http.client :as http]
            [com.climate.claypoole.lazy :as lazy]
            [clojure.tools.cli :refer [parse-opts]]))

(def nordpool-url "http://umm.nordpoolspot.com/export/messages/csv?state=C&state=S&state=R&order_by=-published&published_range_start=%s&published_range_stop=%s")
(def time-period-in-days (t/days 7))
(def custom-formatter (f/formatter "yyyy-MM-dd"))

(defn format-url [from, to]
  (format nordpool-url from to))

(defn format-date [datetime]
  (f/unparse custom-formatter datetime))

(defn parse-date [datetime-string]
  (f/parse custom-formatter datetime-string))

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

(def cli-options
  ;; An option with a required argument
  [["-s" "--start START" "Start date, now as default"
    :default (l/local-now)
    :parse-fn parse-date]
   ;; A non-idempotent option
   ["-e" "--end END" "End date, 2013-04-20 as default"
    :default (t/date-time 2013 04 30)
    :parse-fn parse-date]
   ;; A boolean option defaulting to nil
   ["-h" "--help"]])

(def remaining (atom 0))
(def header (atom ""))

(defn split-header [content]
  (str/split content #"\n" 2))

(defn set-header [h]
  (compare-and-set! header "" h))

(defn print-progress[]
  (println @remaining "to go"))

(defn set-remaining [to-download]
  (reset! remaining to-download))

(defn complete-one []
  (swap! remaining dec)
  (print-progress))

(defn generate-urls [periods]
  (let [ascending (sort periods)
        partitioned (partition 2 1 ascending)
        items-count (count partitioned)]
     (println "Generated" items-count "periods to download")
     (set-remaining items-count)
     (map generate-url-from-pair partitioned)))

(defn read-body-from-url [urls]
  (let [ get (fn [url]
              (let [[head & lines] (split-header (time (:body (http/get url))))]
                  (complete-one)
                  (set-header head)
                  (first lines)))]
    (doall (lazy/pmap 4 get urls))))

(defn to-file [data]
  (let [with-header (conj data @header)
        joined (str/join "\n" with-header)]
    (spit "umm.csv" joined)))

(defn -main
  [& args]
  (time
    (let [opts (parse-opts args cli-options)
          start (get-in opts [:options :start])
          end (get-in opts [:options :end])]
        (println "Start:" (format-date start))
        (println "End:" (format-date end))
        (let [
              periods (generate-periods-until start end)
              urls (generate-urls periods)
              data (read-body-from-url urls)]
            (to-file data)))))
