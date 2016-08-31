(ns umm-downloader.core-test
  (:require [clojure.test :refer :all]
            [umm-downloader.core :refer :all]
            [clj-time.core :as t]
            [clojure.tools.cli :refer [parse-opts]]
            [clj-time.local :as l]))

(deftest datetime-format
    (let [ today (t/date-time 2016 06 01)
          formatted (format-date today)]
      (is (= formatted "2016-06-01"))))

(deftest generate-periods-tests
    (let [ start-datetime (t/date-time 2016 06 01)
          periods (generate-periods start-datetime)
          results (take 3 periods)
          formatted (map format-date results)
          expected '("2016-06-02" "2016-05-03" "2016-04-03")]
      (is (= formatted expected))))

(deftest generate-periods-until-tests
    (let [ start-datetime (t/date-time 2016 06 01)
           epoch (t/date-time 2016 05 01)
           periods (generate-periods-until start-datetime epoch)
           formatted (map format-date periods)
           expected '("2016-06-02" "2016-05-03" "2016-05-01")]
      (is (= formatted expected))))

(deftest generate-periods-until-with-same-epoch-as-last-tests
    (let [ start-datetime (t/date-time 2016 06 01)
           epoch (t/date-time 2016 05 05)
           periods (generate-periods-until start-datetime epoch)
           formatted (map format-date periods)
           expected '("2016-06-02" "2016-05-05")]
      (is (= formatted expected))))

(deftest split-header-destruct
  (let [ input "header\nrest\nrest\nrest"
          split (split-header input)
          [header & rest] split]
    (is (= split '("header" "rest\nrest\nrest")))
    (is (= header "header"))
    (is (= (first rest) "rest\nrest\nrest"))))
