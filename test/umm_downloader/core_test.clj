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
  (testing "FIXME, I fail."
    (let [ today (l/local-now)
          formatted (format-date today)]
      (is (= formatted 1)))))
