(ns umm-downloader.core-test
  (:require [clojure.test :refer :all]
            [umm-downloader.core :refer :all]
            [clj-time.core :as t]
            [clj-time.local :as l]))

(deftest a-test
  (testing "FIXME, I fail."
    (let [formatted (format-url "x" "y")]
      (is (= formatted 1)))))

(deftest datetime-format
    (let [ today (t/date-time 2016 06 01)
          formatted (format-date today)]
      (is (= formatted "2016-06-01"))))

(deftest generate-periods-tests
    (let [ start-datetime (t/date-time 2016 06 01)
          periods (generate-periods start-datetime)
          results (take 3 periods)
          formatted (map format-date results)
          expected '("2016-06-01" "2016-05-02" "2016-04-02")]
      (is (= formatted expected))))

(deftest generate-periods-until-tests
    (let [ start-datetime (t/date-time 2016 06 01)
           epoch (t/date-time 2016 05 01)
           periods (generate-periods-until start-datetime epoch)
           formatted (map format-date periods)
           expected '("2016-06-01" "2016-05-02" "2016-05-01")]
      (is (= formatted expected))))

(deftest generate-periods-until-with-same-epoch-as-last-tests
    (let [ start-datetime (t/date-time 2016 06 01)
           epoch (t/date-time 2016 05 05)
           periods (generate-periods-until start-datetime epoch)
           formatted (map format-date periods)
           expected '("2016-06-01" "2016-05-02")]
      (is (= formatted expected))))

(deftest generate-urls-tests
    (let [ start-datetime (t/date-time 2016 06 01)
           epoch (t/date-time 2016 04 05)
           periods (generate-periods-until start-datetime epoch)
           urls (generate-urls periods)
           expected '("2016-06-01" "2016-05-02")]
      (is (= urls expected))))

(generate-periods-tests)
(datetime-format)
(generate-periods-until-tests)
(t/after? (t/date-time 2016 06 01) (t/date-time 2016 05 01))
